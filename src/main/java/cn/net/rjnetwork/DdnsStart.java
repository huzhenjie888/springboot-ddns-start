package cn.net.rjnetwork;

import cn.hutool.core.io.FileUtil;
import cn.net.rjnetwork.entity.DdnsTaskInfo;
import cn.net.rjnetwork.service.TaskService;
import cn.net.rjnetwork.task.manager.TaskManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;
import java.io.*;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2022/12/2 08:38
 * @desc
 */
@SpringBootApplication(scanBasePackages = {"cn.net.rjnetwork.*"})
@EnableConfigurationProperties
@EnableScheduling
@Slf4j
public class DdnsStart implements ApplicationRunner {

    private static final String baseResourcePath = DdnsStart.class.getProtectionDomain().getCodeSource().getLocation().getPath();

    @Autowired
    private ApplicationContext context;

    @Autowired
    TaskService taskService;



    public static void main(String[] args) throws IOException {

        SpringApplication application =  new SpringApplicationBuilder(DdnsStart.class).build(args);
        application.addListeners(new ApplicationPidFileWriter());
        //先判断sqllite文件是否已存在，如果不存在，则把resources的sqllite文件copy一份。
//        if(!FileUtil.exist("/www/web/dbs/ddns.db")){
//            File source = new File(baseResourcePath+"dbs/ddns.db");
//            File dist = FileUtil.touch("/www/web/dbs/ddns.db");
//            copyFileUsingStream(source,dist);
//            //FileUtil.copy(FileUtil.file(baseResourcePath+"dbs/ddns.db"),dist,false);
//        }
        application.run(DdnsStart.class);


    }



    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        initTask(TaskManager.getScheduledTaskRegistrar());
    }


    private void initTask(ScheduledTaskRegistrar scheduledTaskRegistrar){
        List<DdnsTaskInfo> list =  taskService.getEnabledDdnsTaskInfoList();
        Class<?> clazz;
        Object task;
        for(DdnsTaskInfo ddnsTaskInfo :list){
            try {
                clazz = Class.forName(ddnsTaskInfo.getClassName());
                task = context.getBean(clazz);
                Field taskId = task.getClass().getDeclaredField("taskId");
                taskId.setAccessible(true);
                taskId.set(task, ddnsTaskInfo.getId());
                TriggerTask triggerTask =  new TriggerTask((Runnable) task, triggerContext -> {
                    String cronExpression = ddnsTaskInfo.getCron();
                    return new CronTrigger(cronExpression).nextExecutionTime(triggerContext);
                });
                //添加初始任务
                TaskScheduler taskScheduler = scheduledTaskRegistrar.getScheduler();
                ScheduledFuture<?> future =taskScheduler.schedule(triggerTask.getRunnable(), triggerTask.getTrigger());
                TaskManager.getScheduledFutures().add(future);
                TaskManager.getTaskFutures().put(ddnsTaskInfo.getId(),future);
            } catch (Exception e){
                log.error("任务启动化失败{}",e.getMessage(),e);
            }
        }
    }
}

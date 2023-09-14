package cn.net.rjnetwork;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.net.rjnetwork.entity.DdnsTaskInfo;
import cn.net.rjnetwork.init.Aria2cInit;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
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

    private static final String baseResourcePath = DdnsStart.class.getProtectionDomain().getCodeSource().getLocation().getPath()+"aria2c/";
    private static final String  projectRootPath = System.getProperty("user.dir");

    @Autowired
    private ApplicationContext context;
    private static String aria2cDist = null;
    public static String getAria2cInfPath(){
      return  aria2cDist;
    }
    @Autowired
    TaskService taskService;

    public static void main(String[] args) throws IOException {
        log.info("开始启动应用----start-----");
        Aria2cInit.copyAria2c();
        killApplyInfo();
        SpringApplication application =  new SpringApplicationBuilder(DdnsStart.class).build(args);
        application.addListeners(new ApplicationPidFileWriter());
        application.run(DdnsStart.class);
    }

    private static void killApplyInfo()  {
        //读取项目目录下面的application.pid文件
       try{
           String pid =   FileUtil.readString(projectRootPath+"/application.pid","UTF-8");
           String killCmd = "taskkill /f /t /im  "+pid;
           Process process =  Runtime.getRuntime().exec(killCmd);
           log.info("执行命令结果为{}",IoUtil.readUtf8(process.getInputStream()));
       }catch (Exception e){
          log.error("杀死应用失败{}",e.getMessage(),e);
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
                String cronExpression = ddnsTaskInfo.getCron();
                Trigger trigger = new CronTrigger(cronExpression);
                TriggerTask triggerTask =  new TriggerTask((Runnable) task, trigger);
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

package cn.net.rjnetwork.task.manager;

import cn.net.rjnetwork.entity.DdnsTaskInfo;
import cn.net.rjnetwork.utils.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2023/7/15 23:48
 * @desc
 */
@Slf4j
public class TaskManager {

    private static Set<ScheduledFuture<?>> scheduledFutures = null;

    private static Map<Integer, ScheduledFuture<?>> taskFutures = new ConcurrentHashMap<>();

    private static final String FIELD_SCHEDULED_FUTURES = "scheduledTasks";

    private static ScheduledTaskRegistrar scheduledTaskRegistrar = null;

    public static void setScheduledTaskRegistrar(ScheduledTaskRegistrar scheduledTaskRegistrar){
        TaskManager.scheduledTaskRegistrar = scheduledTaskRegistrar;
    }

    public static ScheduledTaskRegistrar getScheduledTaskRegistrar(){
        return scheduledTaskRegistrar;
    }

    public static Map<Integer, ScheduledFuture<?>> getTaskFutures(){
        return taskFutures;
    }

    public static Set<ScheduledFuture<?>> getScheduledFutures() throws Exception{
        if(scheduledFutures==null){
            scheduledFutures = (Set<ScheduledFuture<?>>) BeanUtils.getProperty(scheduledTaskRegistrar, FIELD_SCHEDULED_FUTURES);
        }
        return scheduledFutures;
    }

    /**
     * 添加定时任务
     *
     */
    public static void addTask(DdnsTaskInfo ddnsTaskInfo){
       try{
           TriggerTask triggerTask = getTriggerTask(ddnsTaskInfo);
           if(taskFutures.containsKey(ddnsTaskInfo.getId())){
               throw new RuntimeException("该任务已经在线程中，禁止重复添加");
           }
           TaskScheduler scheduler = scheduledTaskRegistrar.getScheduler();
           if (scheduler != null){
               ScheduledFuture<?> future = scheduler.schedule(triggerTask.getRunnable(), triggerTask.getTrigger());
               getScheduledFutures().add(future);
               taskFutures.put(ddnsTaskInfo.getId(),future);
           }
       }catch (Exception e){
        log.error("添加失败{}",e.getMessage(),e);
       }
    }



    public static void removeTask(Integer id){
       try{
           ScheduledFuture<?> future = taskFutures.get(id);
           if(future!=null){
               future.cancel(true);
               getScheduledFutures().remove(future);
           }
           taskFutures.remove(id);
       }catch (Exception e){
         log.error("移除失败{}",e.getMessage(),e);
       }
    }


    public static TriggerTask getTriggerTask(DdnsTaskInfo ddnsTaskInfo){

        Class<?> clazz;
        Object task;
        TriggerTask triggerTask = null;
        ApplicationContext context = SpringContextUtil.getApplicationContext();
            try {
                clazz = Class.forName(ddnsTaskInfo.getClassName());
                task = context.getBean(clazz);
                Field taskId = task.getClass().getDeclaredField("taskId");
                taskId.setAccessible(true);
                taskId.set(task, ddnsTaskInfo.getId());
                String cronExpression = ddnsTaskInfo.getCron();
                Trigger trigger = new CronTrigger(cronExpression);
                triggerTask =  new TriggerTask((Runnable) task,  trigger);
            } catch (Exception e){
                log.error("任务启动化失败{}",e.getMessage(),e);
            }finally {
                return triggerTask;
            }

    }




}

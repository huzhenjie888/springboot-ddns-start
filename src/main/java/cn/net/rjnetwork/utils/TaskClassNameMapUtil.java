package cn.net.rjnetwork.utils;

import cn.net.rjnetwork.entity.TaskClassNameEntity;
import java.util.ArrayList;
import java.util.List;

/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2023/7/15 22:50
 * @desc
 */

public class TaskClassNameMapUtil {

    private static final List<TaskClassNameEntity> taskClassNames = new ArrayList<>();

    static {
        TaskClassNameEntity taskClassNameEntity1 = new TaskClassNameEntity();
        taskClassNameEntity1.setClassName("Ipv4TencentDdnsTask");
        taskClassNameEntity1.setClassNamePath("cn.net.rjnetwork.task.Ipv4TencentDdnsTask");
        taskClassNameEntity1.setName("腾讯云IPV4定时更新任务");
        taskClassNames.add(taskClassNameEntity1);
        TaskClassNameEntity taskClassNameEntity2 = new TaskClassNameEntity();
        taskClassNameEntity2.setClassName("Ipv6TencentDdnsTask");
        taskClassNameEntity2.setClassNamePath("cn.net.rjnetwork.task.Ipv6TencentDdnsTask");
        taskClassNameEntity2.setName("腾讯云IPV6定时更新任务");
        taskClassNames.add(taskClassNameEntity2);
    }

    public static List<TaskClassNameEntity> getTaskClassNames(){
        return taskClassNames;
    }
}

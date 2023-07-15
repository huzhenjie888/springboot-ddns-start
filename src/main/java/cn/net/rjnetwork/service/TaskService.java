package cn.net.rjnetwork.service;

import cn.net.rjnetwork.entity.DdnsTaskInfo;
import java.util.List;

public interface TaskService {


    List<DdnsTaskInfo> getEnabledDdnsTaskInfoList();

    DdnsTaskInfo getDdnsTaskInf(Integer taskId);
}

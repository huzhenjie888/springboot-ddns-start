package cn.net.rjnetwork.service.impl;

import cn.net.rjnetwork.entity.DdnsTaskInfo;
import cn.net.rjnetwork.mapper.DdnsAppInfoMapper;
import cn.net.rjnetwork.mapper.DdnsDomainInfoMapper;
import cn.net.rjnetwork.mapper.DdnsRecordInfoMapper;
import cn.net.rjnetwork.mapper.DdnsTaskInfoMapper;
import cn.net.rjnetwork.service.TaskService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2023/7/15 23:29
 * @desc
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    DdnsAppInfoMapper ddnsAppInfoMapper;

    @Autowired
    DdnsDomainInfoMapper ddnsDomainInfoMapper;

    @Autowired
    DdnsRecordInfoMapper ddnsRecordInfoMapper;

    @Autowired
    DdnsTaskInfoMapper ddnsTaskInfoMapper;


    @Override
    public List<DdnsTaskInfo> getEnabledDdnsTaskInfoList() {
        //查询值为1的所有定时任务信息
        LambdaQueryWrapper<DdnsTaskInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DdnsTaskInfo::getStatus,"1");
        List<DdnsTaskInfo> tasks =  ddnsTaskInfoMapper.selectList(lambdaQueryWrapper);
        return tasks;
    }

    @Override
    public DdnsTaskInfo getDdnsTaskInf(Integer taskId) {
        DdnsTaskInfo ddnsTaskInfo = ddnsTaskInfoMapper.selectById(taskId);
        ddnsTaskInfo.setDdnsRecordInfo(ddnsRecordInfoMapper.selectById(ddnsTaskInfo.getRecordId()));
        ddnsTaskInfo.setDdnsDomainInfo(ddnsDomainInfoMapper.selectById(ddnsTaskInfo.getDdnsRecordInfo().getDomainId()));
        ddnsTaskInfo.setDdnsAppInfo(ddnsAppInfoMapper.selectById(ddnsTaskInfo.getDdnsDomainInfo().getAppid()));
        return ddnsTaskInfo;
    }
}

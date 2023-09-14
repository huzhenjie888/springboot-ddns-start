package cn.net.rjnetwork.task;

import cn.hutool.json.JSONUtil;
import cn.net.rjnetwork.config.ScheduledOfTask;
import cn.net.rjnetwork.dns.TencentDnspodManager;
import cn.net.rjnetwork.entity.*;
import cn.net.rjnetwork.ips.Ipv4Utils;
import cn.net.rjnetwork.service.TaskService;
import com.tencentcloudapi.dnspod.v20210323.DnspodClient;
import com.tencentcloudapi.dnspod.v20210323.models.RecordListItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2022/12/2 08:40
 * @desc
 */
@Component
@Slf4j
public class Ipv4TencentDdnsTask implements ScheduledOfTask {




    //执行任务taskId
    private Integer taskId;

    @Autowired
    TaskService taskService;


    @Override
    public void execute() {
        //根据任务id 获取详细信息

        try {
            log.info("执行任务 taskId为{}",taskId);
            DdnsTaskInfo ddnsTaskInfo  = taskService.getDdnsTaskInf(taskId);
            DdnsAppInfo ddnsAppInfo = ddnsTaskInfo.getDdnsAppInfo();
            DdnsDomainInfo ddnsDomainInfo = ddnsTaskInfo.getDdnsDomainInfo();
            DdnsRecordInfo ddnsRecordInfo = ddnsTaskInfo.getDdnsRecordInfo();
            log.info("本次定时任务只是定时更新已经存在的解析记录信息，所以需要先在运营商处进行第一次解析，否则下面会报错，无法进行更新");
            DnspodClient dnspodClient = TencentDnspodManager.getDnspodClient(ddnsAppInfo.getSecretId(),ddnsAppInfo.getSecretKey(),ddnsAppInfo.getRegion());
            log.info("当前需要更新的域名信息为{},{}",ddnsDomainInfo,ddnsRecordInfo);
            String ip = Ipv4Utils.getIp();
            log.info("本机器的ipv4公网IP为{}",ip);
            RecordListItem item = TencentDnspodManager.getSubRecordInfo(dnspodClient,ddnsDomainInfo.getDomain(),ddnsRecordInfo.getRecordName());
            log.info("从服务商获取的解析记录信息为{}",JSONUtil.toJsonStr(item));
            log.info("本次更新只做记录值更新，其他信息保持不变");
            if(item == null){
                throw new RuntimeException("请检查当前记录名称是否已在服务商出添加，如没有添加，请先添加");
            }
            TencentDnspodManager.updateRecordIp(dnspodClient,item,ip,ddnsDomainInfo.getDomain());
        }catch (Exception e){
           log.error("本次更新失败{}",e.getMessage(),e);
        }


    }
}

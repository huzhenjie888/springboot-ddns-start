package cn.net.rjnetwork.task;

import cn.hutool.json.JSONUtil;
import cn.net.rjnetwork.config.ScheduledOfTask;
import cn.net.rjnetwork.dns.TencentDnspodManager;
import cn.net.rjnetwork.entity.*;
import cn.net.rjnetwork.ips.Ipv6Util;
import cn.net.rjnetwork.service.TaskService;
import com.tencentcloudapi.dnspod.v20210323.DnspodClient;
import com.tencentcloudapi.dnspod.v20210323.models.RecordListItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2023/7/8 10:47
 * @desc
 */
@Component
@Slf4j
public class Ipv6TencentDdnsTask implements ScheduledOfTask {


    //执行任务taskId
    private Integer taskId;

    @Autowired
    TaskService taskService;


    @Override
    public void execute()  {
        try {
            log.info("执行任务 taskId为{}",taskId);
            DdnsTaskInfo ddnsTaskInfo  = taskService.getDdnsTaskInf(taskId);
            DdnsAppInfo ddnsAppInfo = ddnsTaskInfo.getDdnsAppInfo();
            DdnsDomainInfo ddnsDomainInfo = ddnsTaskInfo.getDdnsDomainInfo();
            DdnsRecordInfo ddnsRecordInfo = ddnsTaskInfo.getDdnsRecordInfo();
            Map<String,String> maps = Ipv6Util.getLocalIpv6();
            String ipv6 = maps.get("internetIpv6");
            log.info("获取到的本机ipv6为{}",maps);
            if(maps.isEmpty()||ipv6 == null){
                throw new RuntimeException("未获取到公网ipv6，请检测电脑是否具备ipv6网络访问权限");
            }
            DnspodClient dnspodClient = TencentDnspodManager.getDnspodClient(ddnsAppInfo.getSecretId(),ddnsAppInfo.getSecretKey(),ddnsAppInfo.getRegion());
            RecordListItem item = TencentDnspodManager.getSubRecordInfo(dnspodClient,ddnsDomainInfo.getDomain(),ddnsRecordInfo.getRecordName());;
            log.info("从服务商获取的解析记录信息为{}",JSONUtil.toJsonStr(item));
            log.info("本次更新只做记录值更新，其他信息保持不变");
            if(item == null){
                throw new RuntimeException("请检查当前记录名称是否已在服务商出添加，如没有添加，请先添加");
            }
            TencentDnspodManager.updateRecordIp(dnspodClient,item,ipv6,ddnsDomainInfo.getDomain());
        }catch (Exception e){
            log.error("本次更新失败{}",e.getMessage(),e);
        }

    }
}

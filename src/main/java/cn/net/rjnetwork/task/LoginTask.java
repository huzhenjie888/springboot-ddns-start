package cn.net.rjnetwork.task;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.net.rjnetwork.entity.DdnsSessionInfo;
import cn.net.rjnetwork.mapper.DdnsSessionInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2023/7/19 09:42
 * @desc session登录 从登录开始，半小时后将自动过期，需要重新登录。
 */
@Configuration
@EnableScheduling
@Slf4j
public class LoginTask {


    @Autowired
    DdnsSessionInfoMapper ddnsSessionInfoMapper;

    //每1分钟检测一次，查看session是否已过期，如果已过期，则直接删除即可
    @Scheduled(cron = "0 */1 * * * ?")
    public  void exec(){
        LambdaQueryWrapper<DdnsSessionInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<DdnsSessionInfo> ddnsSessionInfoList = ddnsSessionInfoMapper.selectList(lambdaQueryWrapper);
        ddnsSessionInfoList.stream().map((m)->{
            Date time = m.getLoginTime();
           Long cz =  DateUtil.between(time,new Date(), DateUnit.MINUTE);
           if(cz>30){
               ddnsSessionInfoMapper.deleteById(m.getId());
           }
            return null;
        }).collect(Collectors.toList());
    }


    public static void main(String[] args) {
      Long ti =   DateUtil.between(DateUtil.parse("2023-07-19 09:00:00"),new Date(), DateUnit.MINUTE);
        System.out.println(ti);
    }

}

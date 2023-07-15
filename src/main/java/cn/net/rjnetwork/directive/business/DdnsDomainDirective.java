package cn.net.rjnetwork.directive.business;

import cn.net.rjnetwork.ano.RjnetworkDircetive;
import cn.net.rjnetwork.directive.RjnetworkDirectiveBase;
import cn.net.rjnetwork.entity.DdnsAppInfo;
import cn.net.rjnetwork.entity.DdnsDomainInfo;
import cn.net.rjnetwork.mapper.DdnsAppInfoMapper;
import cn.net.rjnetwork.mapper.DdnsDomainInfoMapper;
import cn.net.rjnetwork.utils.SpringContextUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RjnetworkDircetive("ddnsDomainDirective")
public class DdnsDomainDirective  extends RjnetworkDirectiveBase {


    @Override
    public void exec(Env env, Scope scope, Writer writer) {
        DdnsAppInfoMapper ddnsAppInfoMapper = SpringContextUtil.getBean(DdnsAppInfoMapper.class);
        DdnsDomainInfoMapper ddnsDomainInfoMapper = SpringContextUtil.getBean(DdnsDomainInfoMapper.class);
        //Integer appid = getPara(0,scope);
        LambdaQueryWrapper<DdnsDomainInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(DdnsDomainInfo::getCreateTime);
//        if(appid!=null){
//            lambdaQueryWrapper.eq(DdnsDomainInfo::getAppid,appid);
//        }
        List<DdnsDomainInfo> list = ddnsDomainInfoMapper.selectList(lambdaQueryWrapper);
        list.stream().map((m)->{
          DdnsAppInfo appInfo =  ddnsAppInfoMapper.selectById(m.getAppid());
            m.setAppName(appInfo.getName());
            m.setAppkey(appInfo.getSecretId());
            m.setAppsecret(appInfo.getSecretKey());
            m.setStatusName(m.getStatus().equals("1")?"启用":"禁用");
            m.setAppType(appInfo.getType());
            return m;
        }).collect(Collectors.toList());
        scope.set("ddnsDomainList",list==null?new ArrayList<>() :list);
        this.stat.exec(env, scope, writer);
    }

    /**
     * 有body的自定义指令必须覆写这个方法并返回true
     */
    public boolean hasEnd() {
        return true;
    }
}

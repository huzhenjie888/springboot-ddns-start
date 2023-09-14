package cn.net.rjnetwork.directive.business;

import cn.hutool.core.util.StrUtil;
import cn.net.rjnetwork.ano.RjnetworkDircetive;
import cn.net.rjnetwork.directive.RjnetworkDirectiveBase;
import cn.net.rjnetwork.entity.DdnsAppInfo;
import cn.net.rjnetwork.entity.DdnsDomainInfo;
import cn.net.rjnetwork.entity.DdnsRecordInfo;
import cn.net.rjnetwork.entity.DdnsTaskInfo;
import cn.net.rjnetwork.mapper.DdnsAppInfoMapper;
import cn.net.rjnetwork.mapper.DdnsDomainInfoMapper;
import cn.net.rjnetwork.mapper.DdnsRecordInfoMapper;
import cn.net.rjnetwork.mapper.DdnsTaskInfoMapper;
import cn.net.rjnetwork.utils.SpringContextUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RjnetworkDircetive("ddnsTaskDirective")
public class DdnsTaskDirective  extends RjnetworkDirectiveBase {

    @Override
    public void exec(Env env, Scope scope, Writer writer) {

        DdnsTaskInfoMapper ddnsTaskInfoMapper = SpringContextUtil.getBean(DdnsTaskInfoMapper.class);

        DdnsRecordInfoMapper ddnsRecordInfoMapper = SpringContextUtil.getBean(DdnsRecordInfoMapper.class);

        DdnsDomainInfoMapper ddnsDomainInfoMapper = SpringContextUtil.getBean(DdnsDomainInfoMapper.class);

        DdnsAppInfoMapper ddnsAppInfoMapper = SpringContextUtil.getBean(DdnsAppInfoMapper.class);


        LambdaQueryWrapper<DdnsTaskInfo> taskInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();

        HttpServletRequest httpServletRequest = SpringContextUtil.getRequest();
        String recordIdStr= httpServletRequest.getParameter("recordId");
        if(StrUtil.isBlankOrUndefined(recordIdStr)){
            throw new RuntimeException("记录id不能为空");
        }
        Integer recordId = Integer.valueOf(recordIdStr);

        DdnsRecordInfo ddnsRecordInfo = ddnsRecordInfoMapper.selectById(recordId);
        DdnsDomainInfo ddnsDomainInfo = ddnsDomainInfoMapper.selectById(ddnsRecordInfo.getDomainId());
        DdnsAppInfo ddnsAppInfo = ddnsAppInfoMapper.selectById(ddnsDomainInfo.getAppid());
        scope.set("taskDdnsRecordInfo",ddnsRecordInfo);
        scope.set("taskDdnsDomainInfo",ddnsDomainInfo);
        scope.set("taskDdnsAppInfo",ddnsAppInfo);
        taskInfoLambdaQueryWrapper.eq(DdnsTaskInfo::getRecordId,recordId);

        List<DdnsTaskInfo> ddnsTaskInfoList =  ddnsTaskInfoMapper.selectList(taskInfoLambdaQueryWrapper);

        ddnsTaskInfoList.stream().map((m)->{
            m.setStatusName(m.getStatus().equals("1")?"启用":"禁用");
            return m;
        }).collect(Collectors.toList());
        scope.set("ddnsTaskInfoList",ddnsTaskInfoList==null?new ArrayList<>():ddnsTaskInfoList);
        this.stat.exec(env, scope, writer);
    }

    /**
     * 有body的自定义指令必须覆写这个方法并返回true
     */
    public boolean hasEnd() {
        return true;
    }
}

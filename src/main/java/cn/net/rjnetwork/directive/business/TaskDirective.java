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
import cn.net.rjnetwork.utils.TaskClassNameMapUtil;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;

/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2023/7/15 22:40
 * @desc
 */
@Component
@RjnetworkDircetive("taskDirective")
public class TaskDirective extends RjnetworkDirectiveBase {


    @Override
    public void exec(Env env, Scope scope, Writer writer) {
        DdnsDomainInfoMapper ddnsDomainInfoMapper = SpringContextUtil.getBean(DdnsDomainInfoMapper.class);
        DdnsRecordInfoMapper ddnsRecordInfoMapper = SpringContextUtil.getBean(DdnsRecordInfoMapper.class);
        DdnsAppInfoMapper ddnsAppInfoMapper = SpringContextUtil.getBean(DdnsAppInfoMapper.class);
        DdnsTaskInfoMapper ddnsTaskInfoMapper = SpringContextUtil.getBean(DdnsTaskInfoMapper.class);

        HttpServletRequest httpServletRequest = SpringContextUtil.getRequest();

        String recordIdStr= httpServletRequest.getParameter("recordId");
        String domainIdStr= httpServletRequest.getParameter("domainId");
        String appIdStr= httpServletRequest.getParameter("appId");

        if(StrUtil.isBlankOrUndefined(recordIdStr)){
            throw new RuntimeException("recordId is null");
        }

        if(StrUtil.isBlankOrUndefined(domainIdStr)){
            throw new RuntimeException("domainId is null");
        }

        if(StrUtil.isBlankOrUndefined(appIdStr)){
            throw new RuntimeException("appId is null");
        }

        DdnsRecordInfo ddnsRecordInfo = ddnsRecordInfoMapper.selectById(recordIdStr);
        DdnsDomainInfo ddnsDomainInfo = ddnsDomainInfoMapper.selectById(domainIdStr);
        DdnsAppInfo ddnsAppInfo = ddnsAppInfoMapper.selectById(appIdStr);
        scope.set("taskAeDdnsRecordInfo",ddnsRecordInfo);
        scope.set("taskAeDdnsDomainInfo",ddnsDomainInfo);
        scope.set("taskAeDdnsAppInfo",ddnsAppInfo);
        scope.set("classNames", TaskClassNameMapUtil.getTaskClassNames());
        String idStr = httpServletRequest.getParameter("id");
        if(!StrUtil.isBlankOrUndefined(idStr)){
           DdnsTaskInfo ddnsTaskInfo =  ddnsTaskInfoMapper.selectById(idStr);
            scope.set("taskAeDdnsTaskInfo",ddnsTaskInfo);

        }
        this.stat.exec(env, scope, writer);
    }

    /**
     * 有body的自定义指令必须覆写这个方法并返回true
     */
    public boolean hasEnd() {
        return true;
    }
}

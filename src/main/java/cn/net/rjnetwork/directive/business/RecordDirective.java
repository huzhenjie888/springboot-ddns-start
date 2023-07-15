package cn.net.rjnetwork.directive.business;

import cn.hutool.core.util.StrUtil;
import cn.net.rjnetwork.ano.RjnetworkDircetive;
import cn.net.rjnetwork.directive.RjnetworkDirectiveBase;
import cn.net.rjnetwork.entity.DdnsRecordInfo;
import cn.net.rjnetwork.mapper.DdnsDomainInfoMapper;
import cn.net.rjnetwork.mapper.DdnsRecordInfoMapper;
import cn.net.rjnetwork.utils.SpringContextUtil;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;

/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2023/7/15 22:10
 * @desc
 */
@Component
@RjnetworkDircetive("recordDirective")
public class RecordDirective extends RjnetworkDirectiveBase {

    @Override
    public void exec(Env env, Scope scope, Writer writer) {
        DdnsDomainInfoMapper ddnsDomainInfoMapper = SpringContextUtil.getBean(DdnsDomainInfoMapper.class);
        DdnsRecordInfoMapper ddnsRecordInfoMapper = SpringContextUtil.getBean(DdnsRecordInfoMapper.class);
        HttpServletRequest httpServletRequest = SpringContextUtil.getRequest();
        String domain= httpServletRequest.getParameter("domain");
        if(StrUtil.isBlankOrUndefined(domain)){
            throw new RuntimeException("domain is null");
        }
        String domainIdStr = httpServletRequest.getParameter("domainId");
        if(StrUtil.isBlankOrUndefined(domainIdStr)){
            throw new RuntimeException("domainId is null");
        }
        Integer domainId = Integer.valueOf(domainIdStr);
        String idStr = httpServletRequest.getParameter("id");
        if(!StrUtil.isBlankOrUndefined(idStr)){
            Integer id = Integer.valueOf(idStr);
            DdnsRecordInfo ddnsRecordInfo = ddnsRecordInfoMapper.selectById(id);
            scope.set("ddnsRecordInfo",ddnsRecordInfo==null?new DdnsRecordInfo():ddnsRecordInfo);
        }
        scope.set("domainId",domainId);
        scope.set("domain",domain);
        this.stat.exec(env, scope, writer);
    }

    /**
     * 有body的自定义指令必须覆写这个方法并返回true
     */
    public boolean hasEnd() {
        return true;
    }
}

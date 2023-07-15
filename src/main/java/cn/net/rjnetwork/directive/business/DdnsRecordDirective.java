package cn.net.rjnetwork.directive.business;

import cn.hutool.core.util.StrUtil;
import cn.net.rjnetwork.ano.RjnetworkDircetive;
import cn.net.rjnetwork.directive.RjnetworkDirectiveBase;
import cn.net.rjnetwork.entity.DdnsRecordInfo;
import cn.net.rjnetwork.mapper.DdnsRecordInfoMapper;
import cn.net.rjnetwork.utils.SpringContextUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RjnetworkDircetive("ddnsRecordDirective")
public class DdnsRecordDirective  extends RjnetworkDirectiveBase {

    @Override
    public void exec(Env env, Scope scope, Writer writer) {
        DdnsRecordInfoMapper ddnsRecordInfoMapper = SpringContextUtil.getBean(DdnsRecordInfoMapper.class);
        //第一个参数是域名主键id 根据域名id获取所有解析记录列表

        HttpServletRequest httpServletRequest = SpringContextUtil.getRequest();
        String idStr= httpServletRequest.getParameter("id");
        String domain = httpServletRequest.getParameter("domain");
        if(StrUtil.isBlankOrUndefined(idStr)){
            throw new RuntimeException("id is null");
        }
        Integer domainId = Integer.valueOf(idStr);
        LambdaQueryWrapper<DdnsRecordInfo> ddnsRecordInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ddnsRecordInfoLambdaQueryWrapper.eq(DdnsRecordInfo::getDomainId,domainId);
        List<DdnsRecordInfo> list =  ddnsRecordInfoMapper.selectList(ddnsRecordInfoLambdaQueryWrapper);
        list.stream().map((m)->{
            m.setStatusName(m.getStatus().equals("1")?"启用":"禁用");
            m.setDomain(domain);
            return m;
        }).collect(Collectors.toList());

        scope.set("ddnsRecordList",list==null?new ArrayList<>():list);
        scope.set("mainDomain",domain);
        scope.set("mainDomainId",domainId);
        this.stat.exec(env, scope, writer);
    }

    /**
     * 有body的自定义指令必须覆写这个方法并返回true
     */
    public boolean hasEnd() {
        return true;
    }
}

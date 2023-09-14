package cn.net.rjnetwork.directive.business;

import cn.hutool.core.util.StrUtil;
import cn.net.rjnetwork.ano.RjnetworkDircetive;
import cn.net.rjnetwork.directive.RjnetworkDirectiveBase;
import cn.net.rjnetwork.entity.DdnsDomainInfo;
import cn.net.rjnetwork.mapper.DdnsDomainInfoMapper;
import cn.net.rjnetwork.utils.SpringContextUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;


/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2023/7/15 20:32
 * @desc
 */
@Component
@RjnetworkDircetive("domainEditDirective")
public class DomainEditDirective extends RjnetworkDirectiveBase {


    @Override
    public void exec(Env env, Scope scope, Writer writer) {
        DdnsDomainInfoMapper ddnsDomainInfoMapper = SpringContextUtil.getBean(DdnsDomainInfoMapper.class);
        HttpServletRequest httpServletRequest = SpringContextUtil.getRequest();
        String idStr= httpServletRequest.getParameter("id");
        if(StrUtil.isBlankOrUndefined(idStr)){
            throw new RuntimeException("id is null");
        }
        Integer id = Integer.valueOf(idStr);
        LambdaQueryWrapper<DdnsDomainInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        DdnsDomainInfo ddnsDomainInfo = ddnsDomainInfoMapper.selectById(id);
        scope.set("ddnsDomainInfo",ddnsDomainInfo==null?new DdnsDomainInfo() :ddnsDomainInfo);
        this.stat.exec(env, scope, writer);
    }

    /**
     * 有body的自定义指令必须覆写这个方法并返回true
     */
    public boolean hasEnd() {
        return true;
    }
}

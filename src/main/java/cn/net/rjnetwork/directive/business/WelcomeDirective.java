package cn.net.rjnetwork.directive.business;

import cn.net.rjnetwork.ano.RjnetworkDircetive;
import cn.net.rjnetwork.directive.RjnetworkDirectiveBase;
import cn.net.rjnetwork.ips.Ipv6Util;
import cn.net.rjnetwork.utils.SpringContextUtil;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2023/7/16 02:41
 * @desc
 */
@Component
@RjnetworkDircetive("welcomeDirective")
public class WelcomeDirective  extends RjnetworkDirectiveBase {


    @Override
    public void exec(Env env, Scope scope, Writer writer) {
        String ipv4 = cn.net.rjnetwork.qixiaozhu.plugins.ddns.ips.Ipv4Utils.getIp();
        scope.set("ipv4Internet",ipv4);

        Map<String,String> ipv6s = Ipv6Util.getLocalIpv6();
        scope.set("ipv6s",ipv6s);
       HttpServletRequest request = SpringContextUtil.getRequest();
        scope.set("port",request.getLocalPort());


        this.stat.exec(env, scope, writer);
    }

    /**
     * 有body的自定义指令必须覆写这个方法并返回true
     */
    public boolean hasEnd() {
        return true;
    }
}

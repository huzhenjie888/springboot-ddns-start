package cn.net.rjnetwork.directive;

import cn.net.rjnetwork.ano.RjnetworkDircetive;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;


/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2022/10/17 15:45
 * @desc
 */
@RjnetworkDircetive("testDirective")
public class TestDirective extends RjnetworkDirectiveBase{

    @Override
    public void exec(Env env, Scope scope, Writer writer) {
        scope.set("hello","你好");
        this.stat.exec(env, scope, writer);
    }

    /**
     * 有body的自定义指令必须覆写这个方法并返回true
     */
    public boolean hasEnd() {
        return true;
    }


}

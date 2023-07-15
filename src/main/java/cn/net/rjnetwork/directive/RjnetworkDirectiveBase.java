package cn.net.rjnetwork.directive;
import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import lombok.extern.slf4j.Slf4j;


/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2022/10/17 15:01
 * @desc
 */

@Slf4j
public  class RjnetworkDirectiveBase extends Directive  {


    

    public void exec(Env env, Scope scope, Writer writer) {
        log.info("进入执行器{},{},{}",env,scope,writer);
        scope = new Scope(scope);
        scope.getCtrl().setLocalAssignment();
        this.exprList.eval(scope);
        this.stat.exec(env, scope, writer);
    }


    public <T> T getPara(String key, Scope scope) {
        return (T) this.getPara(key, scope, (Object)null);
    }

    public <T> T getPara(String key, Scope scope, T defaultValue) {
        Object data = scope.getLocal(key);
        return data == null ? defaultValue : (T) data;
    }

    public <T> T getPara(int index, Scope scope) {
        return (T) this.getPara(index, scope, (Object)null);
    }
    public <T> T getPara(int index, Scope scope, T defaultValue) {
        if (index >= 0 && index < this.exprList.length()) {
            Object data = this.exprList.getExpr(index).eval(scope);
            return data == null ? defaultValue : (T) data;
        } else {
            return defaultValue;
        }
    }


}

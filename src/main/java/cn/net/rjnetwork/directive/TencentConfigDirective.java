package cn.net.rjnetwork.directive;

import cn.net.rjnetwork.ano.RjnetworkDircetive;
import cn.net.rjnetwork.entity.DdnsAppInfo;
import cn.net.rjnetwork.mapper.DdnsAppInfoMapper;
import cn.net.rjnetwork.utils.SpringContextUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import org.springframework.stereotype.Component;

@RjnetworkDircetive("tencentConfigDirective")
@Component
public class TencentConfigDirective extends RjnetworkDirectiveBase{



    @Override
    public void exec(Env env, Scope scope, Writer writer) {
        DdnsAppInfoMapper ddnsAppInfoMapper = SpringContextUtil.getBean(DdnsAppInfoMapper.class);
        LambdaQueryWrapper<DdnsAppInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DdnsAppInfo::getType,"tencent");
        DdnsAppInfo ddnsAppInfo =  ddnsAppInfoMapper.selectOne(lambdaQueryWrapper);
        scope.set("ddnsAppInfo",ddnsAppInfo==null?new DdnsAppInfo():ddnsAppInfo);
        this.stat.exec(env, scope, writer);
    }

    /**
     * 有body的自定义指令必须覆写这个方法并返回true
     */
    public boolean hasEnd() {
        return true;
    }

}

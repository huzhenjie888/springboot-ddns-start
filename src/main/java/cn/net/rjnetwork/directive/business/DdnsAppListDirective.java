package cn.net.rjnetwork.directive.business;

import cn.net.rjnetwork.ano.RjnetworkDircetive;
import cn.net.rjnetwork.directive.RjnetworkDirectiveBase;
import cn.net.rjnetwork.entity.DdnsAppInfo;
import cn.net.rjnetwork.mapper.DdnsAppInfoMapper;
import cn.net.rjnetwork.utils.SpringContextUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2023/7/15 20:24
 * @desc
 */
@RjnetworkDircetive("ddnsAppListDirective")
@Component
public class DdnsAppListDirective  extends RjnetworkDirectiveBase {



    @Override
    public void exec(Env env, Scope scope, Writer writer) {
        DdnsAppInfoMapper ddnsAppInfoMapper = SpringContextUtil.getBean(DdnsAppInfoMapper.class);
        LambdaQueryWrapper<DdnsAppInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DdnsAppInfo::getStatus,"1");
        List<DdnsAppInfo> list =  ddnsAppInfoMapper.selectList(lambdaQueryWrapper);
        scope.set("ddnsAppInfoList",list==null?new ArrayList<>():list);
        this.stat.exec(env, scope, writer);
    }

    /**
     * 有body的自定义指令必须覆写这个方法并返回true
     */
    public boolean hasEnd() {
        return true;
    }

}

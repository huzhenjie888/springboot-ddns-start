package cn.net.rjnetwork.interceptor;

import cn.net.rjnetwork.entity.DdnsSessionInfo;
import cn.net.rjnetwork.mapper.DdnsSessionInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2023/7/18 08:56
 * @desc
 */
@Slf4j
@Order(1001)
public class LoginInterceptor implements HandlerInterceptor {


    @Autowired
    DdnsSessionInfoMapper ddnsSessionInfoMapper;




    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

       String sessionId = request.getSession().getId();
        LambdaQueryWrapper<DdnsSessionInfo> ddnsSessionInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ddnsSessionInfoLambdaQueryWrapper.eq(DdnsSessionInfo::getSession,sessionId);
        ddnsSessionInfoLambdaQueryWrapper.eq(DdnsSessionInfo::getLoginFlag,1);
        Long s = ddnsSessionInfoMapper.selectCount(ddnsSessionInfoLambdaQueryWrapper);
        if(s==0){
            response.sendRedirect("/login");
            return false;
        }else{
            return true;
        }
    }
}

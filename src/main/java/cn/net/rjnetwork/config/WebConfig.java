package cn.net.rjnetwork.config;

import cn.net.rjnetwork.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2023/7/18 08:54
 * @desc
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public LoginInterceptor getLoginInterceptor(){
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
          registry.addInterceptor(getLoginInterceptor()).addPathPatterns("/**").excludePathPatterns("/login/**","/static/**","/layui/**","/api/doLogin");
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //registry.addResourceHandler(resourceHandler.split(",")).addResourceLocations(resourceLocation.split(","));
    }
}

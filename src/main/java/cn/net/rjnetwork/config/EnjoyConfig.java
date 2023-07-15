package cn.net.rjnetwork.config;

import cn.net.rjnetwork.directive.DirectiveConstFunc;
import com.jfinal.template.Engine;
import com.jfinal.template.ext.spring.JFinalViewResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnjoyConfig {


    @Autowired
    DirectiveConstFunc directiveConstFunc;

    @Bean(name = "jfinalViewResolver")
    public JFinalViewResolver getJFinalViewResolver()  {

        // 创建用于整合 spring boot 的 ViewResolver 扩展对象
        JFinalViewResolver jfr = new JFinalViewResolver();

        // 对 spring boot 进行配置
        jfr.setSuffix(".html");
        jfr.setContentType("text/html;charset=UTF-8");
        jfr.setOrder(0);

        // 设置在模板中可通过 #(session.value) 访问 session 中的数据
        jfr.setSessionInView(true);

        // 获取 engine 对象，对 enjoy 模板引擎进行配置，配置方式与前面章节完全一样
        Engine engine  = JFinalViewResolver.engine;

        // 热加载配置能对后续配置产生影响，需要放在最前面
        engine.setDevMode(true);

        // 使用 ClassPathSourceFactory 从 class path 与 jar 包中加载模板文件
        engine.setToClassPathSourceFactory();

        // 在使用 ClassPathSourceFactory 时要使用 setBaseTemplatePath
        // 代替 jfr.setPrefix("/view/")
        engine.setBaseTemplatePath("/views/");

        // 添加模板函数

        engine.addSharedFunction("_layout.html");
        engine.addSharedFunction("form.html");
        directiveConstFunc.addDircetives(jfr);
       // engine.addSharedFunction("/views/_paginate.html");

        // 更多配置与前面章节完全一样
        // engine.addDirective(...)
        // engine.addSharedMethod(...);

        return jfr;
    }


}

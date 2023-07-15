package cn.net.rjnetwork.directive;

import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.hutool.extra.template.engine.enjoy.EnjoyEngine;


import cn.net.rjnetwork.ano.RjnetworkDircetive;
import cn.net.rjnetwork.ano.RjnetworkEnjoyMethod;
import cn.net.rjnetwork.ano.RjnetworkEnjoyTag;
import com.jfinal.template.Engine;
import com.jfinal.template.ext.spring.JFinalViewResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import java.util.Map;


/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2022/10/17 15:52
 * @desc enjoy 自定义指令封装
 *
 */
@Component
@Slf4j
public class DirectiveConstFunc {


    @Autowired
    WebApplicationContext applicationContext;


    /**
     * @desc  enjoy 添加自定义指令 基于springboot项目，如果需要引用，则需要项目为springboot项目
     * @param  jfr  enjoy 模版对象
     */
    public  void addDircetives( JFinalViewResolver jfr ) {
        Engine engine =  jfr.getEngine();
        Map<String, Object> directives =  applicationContext.getBeansWithAnnotation(RjnetworkDircetive.class);
        log.info("获取的指令列表为{}",directives);
        for (Map.Entry<String, Object> entry : directives.entrySet()) {
            Object value = entry.getValue();
            Class aClass = AopUtils.getTargetClass(value);
            engine.addDirective(entry.getKey(),aClass);
        }
    }


    /**
     * @desc  enjoy 添加标签
     * @param jfr
     */
    public void addTags(JFinalViewResolver jfr ) throws Exception{
        Engine engine =  jfr.getEngine();
        Map<String, Object> directives =  applicationContext.getBeansWithAnnotation(RjnetworkEnjoyTag.class);
        log.info("获取的指令列表为{}",directives);
        for (Map.Entry<String, Object> entry : directives.entrySet()) {
            Object value = entry.getValue();
            Class aClass = AopUtils.getTargetClass(value);
            engine.addSharedObject(entry.getKey(),aClass);
        }

    }



    /**
     * @desc  enjoy 分享方法
     * @param jfr
     */
    public void addMethods(JFinalViewResolver jfr ) throws Exception{
        Engine engine =  jfr.getEngine();
        Map<String, Object> directives =  applicationContext.getBeansWithAnnotation(RjnetworkEnjoyMethod.class);
        log.info("获取的指令列表为{}",directives);
        for (Map.Entry<String, Object> entry : directives.entrySet()) {
            Object value = entry.getValue();
            Class aClass = AopUtils.getTargetClass(value);
            engine.addSharedMethod(aClass);
        }

    }

    /**
     * @desc  根据classpath获取模板对象
     * @param basePath 文件基础目录 eg: /static/
     * @param templatePath 模板路径 eg: test.html
     */
    public Template getEnjoyTemplate(String basePath,String templatePath){
        TemplateConfig templateConfig =  new TemplateConfig(basePath, TemplateConfig.ResourceMode.CLASSPATH);
        templateConfig.setCustomEngine(EnjoyEngine.class);
        TemplateEngine engine  = TemplateUtil.createEngine(templateConfig);
        return engine.getTemplate(templatePath);
    }


    /**
     * @desc  根据字符串获取模板对象。
     * @param basePath 文件基础目录 eg: /static/
     * @param content 模板内容  eg: #(test??)  内容123
     */
    public Template getEnjoyTemplateStr(String basePath,String content){
        TemplateConfig templateConfig =  new TemplateConfig(basePath,TemplateConfig.ResourceMode.STRING);
        templateConfig.setCustomEngine(EnjoyEngine.class);
        TemplateEngine engine  = TemplateUtil.createEngine(templateConfig);
        return engine.getTemplate(content);
    }

}

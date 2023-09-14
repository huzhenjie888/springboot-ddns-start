package cn.net.rjnetwork.controller;

import cn.hutool.core.io.IoUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@Slf4j
public class IndexController {

    @Autowired
    HttpServletResponse response;

    @RequestMapping("/welcome")
    public String index(){
        return "pages/welcome";
    }

    @RequestMapping("/")
    public String index1() throws IOException {
        response.sendRedirect("/welcome");
        return null;
    }

    @RequestMapping("/config/tencent")
    public String configTencent(){
        return "pages/config/tencent";
    }

    @RequestMapping("/domainList/list")
    public String domainList(){
        return "pages/ddns/domain/list";
    }

    @RequestMapping("/domainList/add")
    public String domainListAdd(){
        return "pages/ddns/domain/add";
    }

    @RequestMapping("/domainList/edit")
    public String domainListEdit(){
        return "pages/ddns/domain/edit";
    }



    @RequestMapping("/recordList/list")
    public String recordList(){
        return "pages/ddns/record/list";
    }

    @RequestMapping("/recordList/add")
    public String recordListAdd(){
        return "pages/ddns/record/add";
    }


    @RequestMapping("/recordList/edit")
    public String recordListEdit(){
        return "pages/ddns/record/edit";
    }



    @RequestMapping("/taskList/list")
    public String taskList(){
        return "pages/ddns/task/list";
    }

    @RequestMapping("/taskList/add")
    public String taskListAdd(){
        return "pages/ddns/task/add";
    }


    @RequestMapping("/taskList/edit")
    public String taskListEdit(){
        return "pages/ddns/task/edit";
    }
    @RequestMapping("/login")
    public String login(){
        return "login";
    }
    @RequestMapping("/fileDownload/download")
    public String fileDownload(){
        return "aria2cui/index";
    }

    @GetMapping("/public/**")
    public void getStaticResource(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestPath = request.getRequestURI();
        String filePath = requestPath.split("public/")[1];
        log.info("获取的文件路径信息为....||{}",filePath);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        log.info("当前线程的classLoader为....||{}",classLoader);
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(classLoader);
        //URL url = PluginContextHolder.getPluginClassLoader().getResource("/webs/"+insidePluginDescriptor.getPluginId()+"/views/"+filePath);

        Resource[]  resources = resolver.getResources("/views/"+filePath);
        log.info("获取的资源列表为{},{}",resources.length,resources);
        Resource resource = resources[0];
        if(resource==null){
            throw new RuntimeException("资源不存在");
        }
        //response.getWriter().
        IoUtil.write(response.getOutputStream(),true, IoUtil.readBytes(resource.getInputStream()));
        //return Files.readAllBytes(Paths.get(resource.getURI()));
    }
}

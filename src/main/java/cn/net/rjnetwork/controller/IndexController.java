package cn.net.rjnetwork.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class IndexController {

    @RequestMapping("/")
    public String index(){
        return "pages/welcome";
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
}

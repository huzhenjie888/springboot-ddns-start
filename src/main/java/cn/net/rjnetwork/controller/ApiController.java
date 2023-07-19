package cn.net.rjnetwork.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.net.rjnetwork.entity.*;
import cn.net.rjnetwork.mapper.*;
import cn.net.rjnetwork.result.ResponseWrapper;
import cn.net.rjnetwork.task.manager.TaskManager;
import cn.net.rjnetwork.utils.DownloadUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2023/7/12 12:00
 * @desc
 */
@RestController
@RequestMapping("/api/")
public class ApiController {

    @Autowired
    HttpServletRequest request;

    @Autowired
    DdnsUserInfoMapper ddnsUserInfoMapper;
    @Autowired
    DdnsSessionInfoMapper ddnsSessionInfoMapper;

    @Autowired
    HttpServletResponse response;

    @Autowired
    DdnsAppInfoMapper ddnsAppInfoMapper;

    @Autowired
    DdnsDomainInfoMapper ddnsDomainInfoMapper;
    @Autowired
    DdnsRecordInfoMapper ddnsRecordInfoMapper;
    @Autowired
    DdnsTaskInfoMapper ddnsTaskInfoMapper;

    @RequestMapping("testTask")
    public String testTask(){

        return "你好";
    }
    @RequestMapping("saveOrUpdateTencentConfig")
    public ResponseWrapper saveOrUpdateTencentConfig(DdnsAppInfo ddnsAppInfo){
        if(StrUtil.isBlankOrUndefined(ddnsAppInfo.getStatus())){
            ddnsAppInfo.setStatus("0");
        }
        if(ddnsAppInfo.getId()!=null){
            ddnsAppInfoMapper.updateById(ddnsAppInfo);
        }else{
            ddnsAppInfo.setCreateTime(new Date());
            ddnsAppInfoMapper.insert(ddnsAppInfo);
        }
        return ResponseWrapper.OK("保存/更新成功");
    }

    @RequestMapping("saveOrUpdateDomain")
    public ResponseWrapper saveOrUpdateDomain(DdnsDomainInfo ddnsDomainInfo){
        if(StrUtil.isBlankOrUndefined(ddnsDomainInfo.getStatus())){
            ddnsDomainInfo.setStatus("0");
        }
        if(ddnsDomainInfo.getId()!=null){
            ddnsDomainInfoMapper.updateById(ddnsDomainInfo);
        }else{
            ddnsDomainInfo.setCreateTime(new Date());
            int i = ddnsDomainInfoMapper.insert(ddnsDomainInfo);

        }
        return ResponseWrapper.OK("保存/更新成功");
    }


    @RequestMapping("DelDomainById")
    public ResponseWrapper DelDomainById(Integer id){
        LambdaQueryWrapper<DdnsRecordInfo> ddnsRecordInfoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ddnsRecordInfoLambdaQueryWrapper.eq(DdnsRecordInfo::getDomainId,id);
        Long count = ddnsRecordInfoMapper.selectCount(ddnsRecordInfoLambdaQueryWrapper);
        if(count>0){
            return ResponseWrapper.ERROR("该域名下面存在记录信息，请先删除相关记录信息后再删除此域名信息");
        }
        ddnsDomainInfoMapper.deleteById(id);
        return ResponseWrapper.OK("删除成功");
    }




    @RequestMapping("saveOrUpdateRecord")
    public ResponseWrapper saveOrUpdateRecord(DdnsRecordInfo ddnsRecordInfo){
        if(StrUtil.isBlankOrUndefined(ddnsRecordInfo.getStatus())){
            ddnsRecordInfo.setStatus("0");
        }
        if(ddnsRecordInfo.getId()!=null){
            ddnsRecordInfoMapper.updateById(ddnsRecordInfo);
        }else{
            ddnsRecordInfo.setCreateTime(new Date());
            ddnsRecordInfoMapper.insert(ddnsRecordInfo);
        }
        return ResponseWrapper.OK("保存/更新成功");
    }



    @RequestMapping("DelRecordById")
    public ResponseWrapper DelRecordById(Integer id){
        //判断当前记录是否存在任务，如果存在，则禁止删除。
        LambdaQueryWrapper<DdnsTaskInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DdnsTaskInfo::getRecordId,id);
        Long count = ddnsTaskInfoMapper.selectCount(lambdaQueryWrapper);
        if(count>0){
            return ResponseWrapper.ERROR("该记录下面存在任务，请先删除任务后再进行删除记录");
        }

        ddnsRecordInfoMapper.deleteById(id);
        return ResponseWrapper.OK("删除成功");
    }


    @RequestMapping("saveOrUpdateTask")
    public ResponseWrapper saveOrUpdateTask(DdnsTaskInfo ddnsTaskInfo){
        if(StrUtil.isBlankOrUndefined(ddnsTaskInfo.getStatus())){
            ddnsTaskInfo.setStatus("0");
        }
        if(ddnsTaskInfo.getId()!=null){
            ddnsTaskInfoMapper.updateById(ddnsTaskInfo);
        }else{
            ddnsTaskInfo.setCreateTime(new Date());
            ddnsTaskInfoMapper.insert(ddnsTaskInfo);
        }
        return ResponseWrapper.OK("保存/更新成功");
    }


    @RequestMapping("DelTaskById")
    public ResponseWrapper DelTaskById(Integer id){
        ddnsTaskInfoMapper.deleteById(id);
        return ResponseWrapper.OK("删除成功");
    }

    @RequestMapping("stopTask")
    public ResponseWrapper stopTask(Integer id){
        DdnsTaskInfo temp = new DdnsTaskInfo();
        temp.setStatus("0");
        temp.setId(id);
        ddnsTaskInfoMapper.updateById(temp);
        TaskManager.removeTask(id);
        return ResponseWrapper.OK("停止成功");
    }

    @RequestMapping("addTask")
    public ResponseWrapper addTask(Integer id){
        DdnsTaskInfo temp = new DdnsTaskInfo();
        temp.setStatus("1");
        temp.setId(id);
        ddnsTaskInfoMapper.updateById(temp);
        TaskManager.addTask(ddnsTaskInfoMapper.selectById(id));
        return ResponseWrapper.OK("启动成功");
    }


    @RequestMapping("doDownload")
    public ResponseWrapper doDownload(String link,String trackers,String fileSavePath) throws IOException {
        DownloadUtil.startDownload(link,trackers,fileSavePath);
        return ResponseWrapper.OK("启动成功，下载中，请不要关闭当前页面");
    }

    @RequestMapping("doLogin")
    public ResponseWrapper doLogin(String userName,String password){
        if(StrUtil.isBlankOrUndefined(userName)){
            return ResponseWrapper.ERROR("用户名不能为空");
        }
        if(StrUtil.isBlankOrUndefined(password)){
            return ResponseWrapper.ERROR("密码不能为空");
        }
        String pwd = SecureUtil.md5(password);
        LambdaQueryWrapper<DdnsUserInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DdnsUserInfo::getUsername,userName).eq(DdnsUserInfo::getPassword,pwd);
        lambdaQueryWrapper.last("limit 1");
        DdnsUserInfo inf = ddnsUserInfoMapper.selectOne(lambdaQueryWrapper);
        if(inf==null){
            return  ResponseWrapper.ERROR("用户名或密码错误");
        }
        String sessionId = request.getSession().getId();
        DdnsSessionInfo ddnsSessionInfo = new DdnsSessionInfo();
        ddnsSessionInfo.setSession(sessionId);
        ddnsSessionInfo.setUserId(inf.getId());
        ddnsSessionInfo.setLoginFlag(1);
        ddnsSessionInfo.setLoginTime(new Date());
        ddnsSessionInfoMapper.insert(ddnsSessionInfo);
        return ResponseWrapper.OK("登录成功");
    }




}

package cn.net.rjnetwork.init;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2023/7/21 17:27
 * @desc
 */
@Slf4j
public class Aria2cInit {

    private static final String  projectRootPath = System.getProperty("user.dir");

    private static final String winDistDir = projectRootPath + "/aria2c/";

    public static final String winAria2cExe = winDistDir  + "aria2c-win64.exe";

    private static final String baseResourcePath = Aria2cInit.class.getProtectionDomain().getCodeSource().getLocation().getPath()+"aria2c/";

    public static void copyAria2c(){
        //将此目录下的所有文件copy一份
        try {
        List<String> fileNames = FileUtil.listFileNames(baseResourcePath);
        for(String fileName :fileNames){
            String dist = winDistDir + fileName;
            String classPathDist = baseResourcePath + fileName;
            if(!FileUtil.exist(winDistDir)){
                    FileUtil.mkdir(winDistDir);//如果不存在创建目录
            }
            ClassPathResource resource = null;
            FileOutputStream outputStream = null;
            resource = new ClassPathResource(classPathDist);
            InputStream fis = resource.getInputStream();
            outputStream = new FileOutputStream(  new File(dist) );
            IoUtil.copy(fis,outputStream);
            outputStream.close();
        }
        }catch (Exception e){
            log.error("复制失败{}",e.getMessage(),e);
        }finally {
            //复制成功；启动rpc服务。先关闭 在启动
            killAria2cWinRpc();
            startAria2cWinRpc();
        }

    }


    public static void startAria2cWinRpc()  {
        try{
            StringBuffer sb = new StringBuffer();
            sb.append(winAria2cExe).append(" --conf-path=./aria2.conf > aria2.out.txt").append("\n").append("pause");
            Runtime.getRuntime().exec(sb.toString());
        }catch (Exception e){
            log.error("启动rpc服务失败{}",e.getMessage(),e);
        }
    }

    public static void killAria2cWinRpc() {
       try {
           String pidByPortCmd = "netstat -aon|findstr  6800 ";
           Process process = Runtime.getRuntime().exec(pidByPortCmd);
           String pid = IoUtil.readUtf8(process.getInputStream());
           log.info("查找到的pid信息为{}",pid);
           if((isNumeric(pid))){
               String killCmd = "taskkill /f /t /im  "+pid;
               Process process1 =  Runtime.getRuntime().exec(killCmd);
               log.info("执行命令结果为{}",IoUtil.readUtf8(process1.getInputStream()));
           }
       } catch (Exception e){
           log.error("杀死失败{}",e.getMessage(),e);
       }
    }


    private static Boolean isNumeric(String str){
        Pattern pa  = Pattern.compile("[0-9]*");
        Matcher ma = pa.matcher(str);
        if(ma.matches()){
            return true;
        }else{
            return false;
        }
    }

}

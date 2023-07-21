package cn.net.rjnetwork.init;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

    public static void copyAria2c(){
        //将此目录下的所有文件copy一份
        try {

            Resource[] resources =  new PathMatchingResourcePatternResolver().getResources(ResourceUtils.CLASSPATH_URL_PREFIX+"aria2c/*.*");
            for(Resource resource :resources){
                String dist = winDistDir + resource.getFilename();
                if(!FileUtil.exist(winDistDir)){
                        FileUtil.mkdir(winDistDir);//如果不存在创建目录
                }
                FileOutputStream outputStream = null;
                InputStream fis = resource.getInputStream();
                outputStream = new FileOutputStream(  new File(dist) );
                IoUtil.copy(fis,outputStream);
                outputStream.close();
            }
            //复制完毕，以后台形式启动aria2c rpc服务。
            String cmd ="cmd /c "+projectRootPath+ "/aria2c/RunHide.vbs";
            Process process = Runtime.getRuntime().exec(cmd);
            String res = IoUtil.readUtf8(process.getInputStream());
            log.info("执行的结果为{}",res);
        }catch (Exception e){
            log.error("复制失败{}",e.getMessage(),e);
        }

    }





}

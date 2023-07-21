package cn.net.rjnetwork.init;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ResourceUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

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
            //执行vbs脚本
            StringBuffer sb = new StringBuffer();
            sb.append(" cmd /c  ").append(winDistDir).append("RunHide.vbs").append(" & ");
            log.info("执行的cmd命令为{}",sb);
            Process	process = Runtime.getRuntime().exec(sb.toString());
            process.waitFor();
            String res = IoUtil.readUtf8(process.getInputStream());
            String err = IoUtil.readUtf8(process.getErrorStream());
            log.info("执行的结果为{},{}",res,err);
        }catch (Exception e){
            log.error("复制失败{}",e.getMessage(),e);
        }

    }





}

package cn.net.rjnetwork.utils;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.net.rjnetwork.DdnsStart;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2023/7/19 16:58
 * @desc
 */
@Slf4j
public class DownloadUtil {
    //private static final String baseResourcePath = DownloadUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    private static final String  projectRootPath = System.getProperty("user.dir");
    private static final List<String> trackersList = new ArrayList<>();
    static  {
        //初始化list数据。
        trackersList.addAll(FileUtil.readLines(projectRootPath+"/aria2c/trackers.txt","UTF-8"));
        log.info("trackers==={}",trackersList);
    }

    public static void startDownload(String link,String trackers,String dist) throws IOException, InterruptedException {
        StringBuffer sb = new StringBuffer();
        sb.append(DdnsStart.getAria2cInfPath());
        sb.append(" ");
        if(!StrUtil.isBlankOrUndefined(dist)){
            sb.append(" --conf-path=").append(dist);
        }
        if(StrUtil.isBlankOrUndefined(trackers)){
            sb.append(" --bt-tracker=");
            for(int i=0;i<trackersList.size();i++){
                sb.append(trackersList.get(i));
                if(i!= trackersList.size()-1){
                    sb.append(",");
                }
            }
        }else {
            sb.append(" --bt-tracker=").append(trackers);
        }

        if(StrUtil.isBlankOrUndefined(link)){
            throw new RuntimeException("请添加磁力链接信息");
        }else{
            sb.append(" ").append(link).append(" > ").append("process").append(".txt");
        }
         log.info("拼接好的命令为{}",sb);
        Process process = RuntimeUtil.exec(sb.toString());
        int i = process.waitFor();
        String err =IoUtil.readUtf8(process.getErrorStream());
        String result = IoUtil.readUtf8(process.getInputStream());
         log.info("下载启动完毕{},{},err={}",result,i,err);

    }


}

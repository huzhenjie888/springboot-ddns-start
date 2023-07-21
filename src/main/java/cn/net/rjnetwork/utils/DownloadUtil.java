package cn.net.rjnetwork.utils;
import cn.hutool.core.io.FileUtil;
import cn.net.rjnetwork.entity.Aria2Option;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2023/7/19 16:58
 * @desc
 */
@Slf4j
public class DownloadUtil {
    private static final String  projectRootPath = System.getProperty("user.dir");
    private static final List<String> trackersList = new ArrayList<>();
    static  {
        //初始化list数据。
        trackersList.addAll(FileUtil.readLines(projectRootPath+"/aria2c/trackers.txt","UTF-8"));
        log.info("trackers==={}",trackersList);
    }

    public static void startDownload(String link,String dist,String fileName) throws IOException, InterruptedException {
        Aria2Option aria2Option = new Aria2Option();
        aria2Option.setDir(dist==null?"./download/":dist);
        aria2Option.setReferer("*");
        aria2Option.setOut(fileName);
        log.info("组合参数为 {}",aria2Option);
        Aria2Json aria2Json = new Aria2Json(UUID.randomUUID().toString());
        aria2Json.setMethod(Aria2Json.METHOD_ADD_URI)
                .addParam(new String[]{link})
                .addParam(aria2Option);
        String send = aria2Json.send(null);
        log.info("send=={}",send);
    }

    public static void main(String[] args) {
        String s = "magnet:?xt=urn:btih:3a87bac211fd5cd49b81c33eca69919c4a5a74fd&dn=zh-cn_windows_11_business_editions_version_22h2_updated_june_2023_x64_dvd_8e846a1e.iso&xl=5844441088\n" +
                "   ";
        String[] arr = s.split(":");
        String a = arr[arr.length-1];
        String[] ab = a.split("=");
        String c = ab[ab.length-2];
        System.out.println(c);
    }


}

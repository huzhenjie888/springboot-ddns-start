package cn.net.rjnetwork.qixiaozhu.plugins.ddns.ips;

import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2022/11/30 13:06
 * @desc 向百度查询当前机器所对应的公网ip
 */
@Slf4j
public class Ipv4Utils {

    private final static String searchUrl =  "https://ip.chinaz.com/";
    private final static String   rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

    public static String getIp() {
        try{
            Document doc = Jsoup.connect(searchUrl).get();
            Elements elements = doc.body().getElementsByClass("WhwtdWrap bor-b1s col-gray03");
            String ip = elements.get(0).getElementsByTag("span").get(0).text();
            if(isIP(ip)){
                return ip;
            }
        }catch (Exception e){
            log.error("获取ipv4报错{}",e.getMessage(),e);
        }
        return null;

    }

    public static boolean  isIP(String addr) {
        if(addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
            return false;
        }

        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(addr);

        boolean ipAddress = mat.find();
        return ipAddress;
    }


    public static void main(String[] args) throws IOException {
        String source = "112.10.214";
        StringBuffer sb = new StringBuffer();

        for(int i =97;i<255;i++){
            sb.append(source).append(".").append(i).append(",");
        }
        System.out.println(sb.toString());
//       String ip = Ipv4Utils.getIp();
//        System.out.println(ip);
    }
}

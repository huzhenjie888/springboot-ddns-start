package cn.net.rjnetwork.ips;

import cn.hutool.http.HttpUtil;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2022/11/30 13:06
 * @desc 向百度查询当前机器所对应的公网ip
 */

public class Ipv4Utils {

    //http://myip.ipip.net  "http://myip.ipip.net";//
    private final static String searchUrl =  "https://members.3322.org/dyndns/getip";
    private final static String   rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

    public static String getIp(){
        String result = HttpUtil.get(searchUrl);
        //判断是否是ip地址。39.185.226.191
        if(isIP(result)){
            return result;
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


    public static void main(String[] args) {
       String ip = Ipv4Utils.getIp();
        System.out.println(ip);
    }
}

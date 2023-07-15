package cn.net.rjnetwork.ips;

import cn.hutool.core.net.NetUtil;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2023/7/5 16:26
 * @desc 获取本地的ipv6的公网ipv6
 */

public class Ipv6Util {


    public static Map<String,String> getLocalIpv6(){
        LinkedHashSet<String> linkedHashSet = NetUtil.localIpv6s();
        Map<String,String> map = new HashMap<>();
        for(String str :linkedHashSet){
            //移动网络公网访问是 24开头。
            if(str.startsWith("24")){
                if(str.contains("0:0:0")){
                    continue;
                }
                String[] arr = str.split(":");
                  if(arr.length==8){
                      map.put("internetIpv6",str.split("%")[0]);
                      break;
                  }
            }
        }
       return map;
    }


    public static void main(String[] args) {
      Map<String,String> ipv6 =  Ipv6Util.getLocalIpv6();
//        inet6 240a:42a8:1400:245:14bf:122:2ebd:1a4f prefixlen 64 autoconf secured
//        inet6 240a:42a8:1400:245:29b8:a6f8:358:705b prefixlen 64 autoconf temporary
        System.out.println(ipv6);
    }
}

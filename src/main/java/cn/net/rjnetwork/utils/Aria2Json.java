package cn.net.rjnetwork.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2023/7/21 16:59
 * @desc
 */
@Data
//开启setter方法的链式调用
@Accessors(chain = true)
//无参构造方法
@NoArgsConstructor
@Slf4j
public class Aria2Json {

    /**
     * 方法名常量
     */
    public final static String METHOD_TELL_ACTIVE = "aria2.tellActive";
    public final static String METHOD_ADD_URI = "aria2.addUri";
    public final static String METHOD_GET_GLOBAL_STAT = "aria2.getGlobalStat";
    public final static String METHOD_TELL_STOPPED = "aria2.tellStopped";
    public final static String METHOD_TELL_WAITING = "aria2.tellWaiting";
    public final static String METHOD_REMOVE_DOWNLOAD_RESULT = "aria2.removeDownloadResult";
    private final static String[] PARAM_ARRAY_OF_FILED =
            new String[]{"totalLength", "completedLength", "files", "status", "errorCode", "gid"};
    /**
     * id随机生成，也可以手动设置
     */
    private String id = UUID.randomUUID().toString();
    private String jsonrpc = "2.0";
    private String method = METHOD_TELL_ACTIVE;
    private String url;
    private List<Object> params = new ArrayList<>();
    //暂存下载参数

    /**
     * 添加下载参数
     * @return
     */
    public Aria2Json addParam(Object obj) {
        params.add(obj);
        return this;
    }

    public static String tellActive() {
        Aria2Json aria2Json = new Aria2Json();
        aria2Json.setMethod(METHOD_TELL_ACTIVE)
                .addParam(PARAM_ARRAY_OF_FILED);
        return aria2Json.send(null);
    }

    public static String tellStopped() {
        Aria2Json aria2Json = new Aria2Json();
        aria2Json.setMethod(METHOD_TELL_STOPPED)
                .addParam(-1)
                .addParam(1000)
                .addParam(PARAM_ARRAY_OF_FILED);
        return aria2Json.send(null);
    }

    public static String tellWaiting() {
        Aria2Json aria2Json = new Aria2Json();
        aria2Json.setMethod(METHOD_TELL_WAITING)
                .addParam(0)
                .addParam(1000)
                .addParam(PARAM_ARRAY_OF_FILED);
        return aria2Json.send(null);
    }

    public static String removeDownloadResult(String gid) {
        Aria2Json aria2Json = new Aria2Json();
        aria2Json.setMethod(METHOD_REMOVE_DOWNLOAD_RESULT)
                .addParam(gid);
        return aria2Json.send(null);
    }

    public Aria2Json(String id) {
        this.id = id;
    }

    public String send(String jsonRpcUrl) {
        //rpcurl 默认为本地默认地址
        jsonRpcUrl = StringUtils.isEmpty(jsonRpcUrl) ? "http://localhost:6800/jsonrpc" : jsonRpcUrl;
        //创建post请求对象
        HttpPost httpPost = new HttpPost(jsonRpcUrl);
        //设置content type（正文类型） 为json格式
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        //将 this 对象解析为 json字符串 并用UTF-8编码(重要)将其设置为 entity （正文）
        httpPost.setEntity(new StringEntity(JSONObject.toJSONString(this), StandardCharsets.UTF_8));
        //发送请求并获取返回对象
        CloseableHttpResponse response;
        try {
            response = HttpClients.createDefault().execute(httpPost);
        } catch (HttpHostConnectException e) {
            log.debug("Aria2 无法连接");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        //返回的状态码
        int statusCode = response.getStatusLine().getStatusCode();
        HttpEntity entity = response.getEntity();
        //请求结果字符串
        String result = null;
        try {
            //用UTF-8解码返回字符串
            result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            //如果状态码为200表示请求成功，返回结果
            if (statusCode == HttpStatus.SC_OK) {
                EntityUtils.consume(entity);
                return result;
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        //请求失败 打印状态码和提示信息 返回null
        System.out.println("statusCode = " + statusCode);
        System.out.println("result = " + result);
        return null;
    }


}

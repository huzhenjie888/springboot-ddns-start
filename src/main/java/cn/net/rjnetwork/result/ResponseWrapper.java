package cn.net.rjnetwork.result;
import lombok.Data;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;

/**
 * 接口统一返回格式
 *
 * ResponseWarpper
 * 创建人:linzeyou 
 * 时间：2021年6月24日 上午10:21:54 
 *
 * @version 1.0.0
 */
@Data
public class ResponseWrapper {
	 
    /**是否成功*/

    private boolean success;
    /**返回码*/
    private Integer code;
    /**返回信息*/

    private String msg;
    /**返回数据*/

    private Object data;


    private String decive;


    public ResponseWrapper() {
		super();
	}

 




    /**
     * 查询成功但无数据
     * @return
     */
    public static ResponseWrapper markSuccessButNoData(){
        ResponseWrapper responseWrapper  = new ResponseWrapper();
        responseWrapper.setSuccess(true);
        responseWrapper.setCode(0);
        responseWrapper.setMsg(null);
        responseWrapper.setData(null);
        responseWrapper.setDecive(getDeviceInfo());

        return responseWrapper;
    }

    /**
     * 查询成功且有数据
     * @param data
     * @return
     */
    public static ResponseWrapper markSuccess(Object data){
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setSuccess(true);
        responseWrapper.setCode(0);
        responseWrapper.setMsg(null);
        responseWrapper.setData(data);
        responseWrapper.setDecive(getDeviceInfo());
        return  responseWrapper;
    }




    /**
     * 操作成功
     * @param data
     * @return
     */
    public static ResponseWrapper opSuccess(Object data){
    	ResponseWrapper responseWrapper = new ResponseWrapper();
    	responseWrapper.setSuccess(true);
    	responseWrapper.setCode(0);
    	responseWrapper.setMsg(null);
    	responseWrapper.setData(data);
        responseWrapper.setDecive(getDeviceInfo());
    	return  responseWrapper;
    }
    /**
     * 操作失败
     * @param
     * @return
     */
    public static ResponseWrapper opFail(){
    	ResponseWrapper responseWrapper = new ResponseWrapper();
    	responseWrapper.setSuccess(false);
    	responseWrapper.setCode(0);
    	responseWrapper.setMsg(null);
    	responseWrapper.setData("");
        responseWrapper.setDecive(getDeviceInfo());
    	return  responseWrapper;
    }

    /**
     * 操作失败 有返回 msg 无 data
     * @param
     * @return
     */
    public static ResponseWrapper ERROR(String msg){
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setSuccess(false);
        responseWrapper.setCode(1);
        responseWrapper.setMsg(msg);
        responseWrapper.setData("");
        responseWrapper.setDecive(getDeviceInfo());
        return  responseWrapper;
    }
    /**
     * 操作成功 有返回 msg 无 data
     * @param
     * @return
     */
    public static ResponseWrapper OK(String msg){
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setSuccess(true);
        responseWrapper.setCode(0);
        responseWrapper.setMsg(msg);
        responseWrapper.setData("");
        responseWrapper.setDecive(getDeviceInfo());
        return  responseWrapper;
    }

    public static ResponseWrapper OK(String msg,Object object){
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setSuccess(true);
        responseWrapper.setCode(0);
        responseWrapper.setMsg(msg);
        responseWrapper.setData(object);
        responseWrapper.setDecive(getDeviceInfo());
        return  responseWrapper;
    }


    /**
     * 业务异常
     *
     */
    public static ResponseWrapper markFailed(Integer code ,String message){
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setSuccess(false);
        responseWrapper.setCode(code);
        responseWrapper.setMsg(message);
        responseWrapper.setDecive(getDeviceInfo());
        return responseWrapper;
    }

    public static ResponseWrapper success(String message,Object data){
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setSuccess(true);
        responseWrapper.setCode(0);
        responseWrapper.setMsg(message);
        responseWrapper.setData(data);
        responseWrapper.setDecive(getDeviceInfo());
        return responseWrapper;
    }

    public static ResponseWrapper success(Object data){
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setSuccess(true);
        responseWrapper.setCode(0);
        responseWrapper.setData(data);
        responseWrapper.setDecive(getDeviceInfo());
        return responseWrapper;
    }

    public static ResponseWrapper success(Boolean bool){
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setSuccess(bool);
        responseWrapper.setCode(bool==true?0:-1);
        responseWrapper.setMsg(bool==true?"操作成功":"操作失败");
        responseWrapper.setDecive(getDeviceInfo());
        return responseWrapper;
    }

    public static ResponseWrapper success(Boolean bool,String message){
        ResponseWrapper responseWrapper = new ResponseWrapper();
        responseWrapper.setSuccess(bool);
        responseWrapper.setCode(0);
        responseWrapper.setMsg(message);
        responseWrapper.setData(message);
        responseWrapper.setDecive(getDeviceInfo());
        return responseWrapper;
    }

    public static String getDeviceInfo(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(null !=requestAttributes) {
            HttpServletRequest request = requestAttributes.getRequest();
            String ua = request.getHeader("User-Agent");
            if(ua.contains("Mac OS")){
                return "mac";
            }else if(ua.contains("iPhone")){
                return "iPhone";
            }else if(ua.contains("Android")){
                return "Android";
            }else if(ua.contains("Windows")){
                return "Window";
            }

        }
        //获取设备信息。
        return null;
    }


}

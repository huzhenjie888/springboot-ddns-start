package cn.net.rjnetwork.dns;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.dnspod.v20210323.DnspodClient;
import com.tencentcloudapi.dnspod.v20210323.models.*;
import lombok.extern.slf4j.Slf4j;
import java.util.Arrays;

/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2022/11/30 16:34
 * @desc  腾讯云DNSPOD
 */
@Slf4j
public class TencentDnspodManager {

    private static Credential getCredential(String secretId,String secretKey){
        Credential credential = new Credential(secretId,secretKey);
        return credential;
    }

    public static DnspodClient getDnspodClient(String secretId,String secretKey,String region){
        Console.log("地域信息为{}",region);
        Credential credential = getCredential(secretId,secretKey);
        DnspodClient dnspodClient = new DnspodClient(credential,region==null?"ap-shanghai":region);
        return dnspodClient;
    }
    /**
     * @desc 获取当前账号的所有域名列表
     * @param dnspodClient
     * @return list
     */
    public static DomainListItem[] getDomainList(DnspodClient dnspodClient) throws TencentCloudSDKException {
        DescribeDomainListRequest request = new DescribeDomainListRequest();
        DescribeDomainListResponse response = dnspodClient.DescribeDomainList(request);
        return  response.getDomainList();
    }

    /**
     * 获取主域名信息
     *
     */
    public static DomainInfo getDomain(DnspodClient dnspodClient,String domain) throws TencentCloudSDKException {
        DescribeDomainRequest request = new DescribeDomainRequest();
        request.setDomain(domain);
        DescribeDomainResponse response =  dnspodClient.DescribeDomain(request);
        return response.getDomainInfo();
    }

   /**
    * 根据域名获取解析记录列表
    * @param dnspodClient
    * @param domain 主域名
    */
    public static RecordListItem[] getDescribeRecordList(DnspodClient dnspodClient,String domain) throws TencentCloudSDKException {
        DescribeRecordListRequest request = new DescribeRecordListRequest();
        request.setDomain(domain);
        DescribeRecordListResponse response = dnspodClient.DescribeRecordList(request);
        return response.getRecordList();
    }
    /**
     * 根据主域名和解析记录名称查找解析记录信息
     * @param domain 主域名
     * @param name   解析记录名称
     */
    public static RecordListItem getSubRecordInfo( DnspodClient dnspodClient,String domain,String name) throws TencentCloudSDKException {
        RecordListItem[] items = getDescribeRecordList(dnspodClient,domain);
        return Arrays.stream(items).filter((item)->{return item.getName().equals(name);}).findFirst().get();
    }


    /**
     * 更新已经记录的ip信息
     * @param dnspodClient
     * @param ip ip值
     * @param item 解析记录
     * @param domain 主域名
     */
    public static void updateRecordIp(DnspodClient dnspodClient,RecordListItem item,String ip,String domain) throws TencentCloudSDKException {
        log.info("传入的ip信息为{}",ip);
        if(StrUtil.isBlankOrUndefined(ip)||StrUtil.isBlankOrUndefined(domain)){
            throw new RuntimeException("ip不能为空");
        }
        if(ip.contains(item.getValue())){
            log.info("获取的ip与已经解析的ip相同，无需更新,{},{}",ip,item.getValue());
            return ;
        }
        ModifyRecordRequest request = new ModifyRecordRequest();
        request.setRecordId(item.getRecordId());
        request.setValue(ip);
        request.setDomain(domain);
        request.setRecordLine(item.getLine());
        request.setRecordType(item.getType());
        request.setSubDomain(item.getName());
        ModifyRecordResponse response = dnspodClient.ModifyRecord(request);
        log.info("更新的结果为{}", JSONUtil.toJsonStr(response));
    }



    /**
     * 新增一条解析记录
     * @param domain 主域名
     * @param recordType 解析类型    A CNAME MX TXT AAAA NS CAA HTTPS
     * @param recordLine 解析线路   默认值 可以设置为  默认
     * @param value 解析记录值
     * @param subDomain 主机记录   www  @  mail *  ...
     */
    public static void addRecordIp(
            DnspodClient dnspodClient,
            String domain,String recordType,String recordLine,String value,
            String subDomain) throws TencentCloudSDKException {
        if(StrUtil.isBlankOrUndefined(domain)){
            throw new RuntimeException("主域名不能为空");
        }
        if(StrUtil.isBlankOrUndefined(recordType)){
            throw new RuntimeException("记录类型不能为空");
        }
        if(StrUtil.isBlankOrUndefined(recordLine)){
            throw new RuntimeException("记录线路不能为空");
        }
        if(StrUtil.isBlankOrUndefined(value)){
            throw new RuntimeException("记录值不能为空");
        }
        if(StrUtil.isBlankOrUndefined(subDomain)){
            throw new RuntimeException("主机记录不能为空");
        }
        CreateRecordRequest createRecordRequest = new CreateRecordRequest();
        createRecordRequest.setDomain(domain);
        createRecordRequest.setRecordType(recordType);
        createRecordRequest.setRecordLine(recordLine);
        createRecordRequest.setValue(value);
        createRecordRequest.setSubDomain(subDomain);
        dnspodClient.CreateRecord(createRecordRequest);
    }

    public static void main(String[] args) throws TencentCloudSDKException {
        DnspodClient dnspodClient =  getDnspodClient("secretId","secretKey",null);
        DomainInfo domainInfo = getDomain(dnspodClient,"domain");
        RecordListItem[] recordListItems = getDescribeRecordList(dnspodClient,"domain");
        for(RecordListItem rr :recordListItems){
            System.out.println(rr.getName()+"=="+rr.getValue());
        }
    }
}

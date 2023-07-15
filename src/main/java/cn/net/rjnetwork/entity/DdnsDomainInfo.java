package cn.net.rjnetwork.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2023/7/15 17:13
 * @desc
 */
@Data
@TableName("ddns_domain_info")
public class DdnsDomainInfo extends Model {

    @TableId(type= IdType.AUTO)
    private Integer id;

    private Date createTime;

    @TableField("`status`")
    private String status;

    private String domain;

    private Integer appid;

    @TableField(exist = false)
    private String appName;
    @TableField(exist = false)
    private String appkey;
    @TableField(exist = false)
    private String appsecret;
    @TableField(exist = false)
    private String statusName;
    @TableField(exist = false)
    private String appType;





}

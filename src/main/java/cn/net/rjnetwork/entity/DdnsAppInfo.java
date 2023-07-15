package cn.net.rjnetwork.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

@Data
@TableName("ddns_app_info")
public class DdnsAppInfo extends Model {

    @TableId(type=IdType.AUTO)
    private Integer id;

    @TableField("`type`")
    private String type;

    private String secretId;

    private String secretKey;

    private String region;

    private Date createTime;

    @TableField("`status`")
    private String status;
    @TableField("`name`")
    private String name;



}

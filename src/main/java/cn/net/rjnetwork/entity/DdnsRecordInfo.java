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
@TableName("ddns_record_info")
public class DdnsRecordInfo extends Model {

    @TableId(type= IdType.AUTO)
    private Integer id;

    private Date createTime;

    @TableField("`status`")
    private String status;

    private String recordName;

    private Integer domainId;



    @TableField(exist = false)
    private String domain;
    @TableField(exist = false)
    private String statusName;
}

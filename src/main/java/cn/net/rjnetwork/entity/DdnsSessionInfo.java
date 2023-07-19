package cn.net.rjnetwork.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.Date;

/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2023/7/19 09:21
 * @desc
 */
@Data
@TableName("ddns_session_info")
public class DdnsSessionInfo extends Model {

    @TableId(type= IdType.AUTO)
    private Integer id;

    private Integer userId;

    private String session;

    private Integer loginFlag;

    private Date loginTime;

}

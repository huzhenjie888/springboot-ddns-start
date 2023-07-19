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
 * @date 2023/7/19 09:15
 * @desc
 */
@Data
@TableName("ddns_user_info")
public class DdnsUserInfo extends Model {

    @TableId(type= IdType.AUTO)
    private Integer id;

    private String username;

    private String password;

    private Date createTime;

    @TableField("`status`")
    private String status;


}

package cn.net.rjnetwork.entity;

import lombok.Data;

/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2023/7/12 11:23
 * @desc
 */
@Data
public class BeanTaskInfo {

    private String className;

    private String cron;

    private Boolean enabled;

}

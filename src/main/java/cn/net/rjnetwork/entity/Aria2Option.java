package cn.net.rjnetwork.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2023/7/21 17:05
 * @desc
 */
@Data
@Accessors(chain = true)
public class Aria2Option {

    String dir;
    String out;
    String referer;
}

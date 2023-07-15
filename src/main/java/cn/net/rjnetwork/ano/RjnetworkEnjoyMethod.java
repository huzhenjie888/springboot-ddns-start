package cn.net.rjnetwork.ano;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @auther huzhenjie
 * @email huzhenjie@rjnetwork.net.cn
 * @date 2022/11/15 16:49
 * @desc
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface RjnetworkEnjoyMethod {

    String value();

    boolean override() default false;
}

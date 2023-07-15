package cn.net.rjnetwork.ano;

import org.springframework.stereotype.Component;
import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface RjnetworkDircetive {
    String value();

    boolean override() default false;
}

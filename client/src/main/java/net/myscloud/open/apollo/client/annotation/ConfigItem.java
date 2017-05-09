package net.myscloud.open.apollo.client.annotation;

import java.lang.annotation.*;

/**
 * Created by genesis on 17-5-9.
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfigItem {

    String key();


}

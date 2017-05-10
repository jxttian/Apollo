package net.myscloud.open.apollo.client.annotation;

import net.myscloud.open.apollo.core.consts.ConfigItemType;

import java.lang.annotation.*;

/**
 * Created by genesis on 17-5-9.
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfigItem {

    /**
     * 项目名称
     *
     * @return
     */
    String project() default "";

    /**
     * 配置Key
     *
     * @return
     */
    String key() default "";

    /**
     * 配置Key 别名
     *
     * @return
     */
    String value() default "";

    /**
     * 类型
     *
     * @return
     */
    ConfigItemType type() default ConfigItemType.String;

    /**
     * 默认配置
     *
     * @return
     */
    String defaultValue() default "";
}

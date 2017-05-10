package net.myscloud.open.apollo.client.item;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Splitter;
import net.myscloud.open.apollo.client.annotation.ConfigItem;
import net.myscloud.open.apollo.common.kits.StringKits;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by jxtti on 2017/05/10.
 */
@Aspect
public class ConfigItemAspect {

    @Autowired
    private ApolloConfiguration apolloConfiguration;

    @Around("@annotation(configItem)")
    public Object around(ProceedingJoinPoint point, ConfigItem configItem) throws Throwable {

        //默认项目名称为 ApolloConfiguration 中配置的projects取第一个
        String project = StringKits.isEmpty(configItem.project())
                ? Splitter.on(",").trimResults().omitEmptyStrings().splitToList(apolloConfiguration.getProjects()).get(0)
                : configItem.project();

        //默认项目名称为方法名称
        String key = StringKits.isEmpty(configItem.key())
                ? StringKits.isEmpty(configItem.value()) ? point.getSignature().getName() : configItem.value()
                : configItem.key();

        String value = apolloConfiguration.get(project, key);
        if (StringKits.isEmpty(value)) {
            if (StringKits.isEmpty(configItem.defaultValue())) {
                return point.proceed();
            } else {
                value = configItem.defaultValue();
            }
        }
        switch (configItem.type()) {
            case Number:
                return Long.parseLong(value);
            case Boolean:
                return Boolean.valueOf(value);
            case List:
                return JSON.parseArray(value);
            case Map:
                return JSON.parseObject(value);
            case String:
            default:
                return value;
        }
    }
}

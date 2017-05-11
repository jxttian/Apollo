package net.myscloud.open.apollo.console.service;

import net.myscloud.open.apollo.common.Response;
import net.myscloud.open.apollo.console.framework.mybatis.IGenericService;
import net.myscloud.open.apollo.domain.model.ConfigItem;
import net.myscloud.open.yuna.model.User;

public interface ConfigItemService extends IGenericService<ConfigItem, Integer> {
    Response save(User user, ConfigItem item);

    Response getFromRegister(String project, String env, String key);
}
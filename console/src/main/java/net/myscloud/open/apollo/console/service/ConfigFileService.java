package net.myscloud.open.apollo.console.service;

import net.myscloud.open.apollo.common.framework.Response;
import net.myscloud.open.apollo.common.framework.mybatis.IGenericService;
import net.myscloud.open.apollo.console.search.ConfigFileSearch;
import net.myscloud.open.apollo.domain.model.ConfigFile;
import net.myscloud.open.yuna.model.User;

import java.util.Optional;

public interface ConfigFileService extends IGenericService<ConfigFile, Integer> {
    Response save(User user, ConfigFile item);

    Optional<ConfigFile> getFirst(ConfigFileSearch search);
}
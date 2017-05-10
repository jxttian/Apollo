package net.myscloud.open.apollo.console.mapper;

import net.myscloud.open.apollo.console.framework.mybatis.IGenericMapper;
import net.myscloud.open.apollo.console.search.ConfigFileSearch;
import net.myscloud.open.apollo.domain.model.ConfigFile;

public interface ConfigFileMapper extends IGenericMapper<ConfigFile, Integer> {
    ConfigFile getFirstBySearchCondition(ConfigFileSearch search);
}
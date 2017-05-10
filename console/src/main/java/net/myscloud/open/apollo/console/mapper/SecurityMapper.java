package net.myscloud.open.apollo.console.mapper;

import net.myscloud.open.apollo.console.framework.mybatis.IGenericMapper;
import net.myscloud.open.apollo.domain.model.Security;
import org.apache.ibatis.annotations.Param;

public interface SecurityMapper extends IGenericMapper<Security, Integer> {
    Security getByProjectAndEnv(@Param("project") String project,@Param("env") String env);
}
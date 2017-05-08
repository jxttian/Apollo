package net.myscloud.open.apollo.console.service;

import net.myscloud.open.apollo.common.framework.mybatis.IGenericService;
import net.myscloud.open.apollo.domain.model.Security;

import java.util.Optional;

public interface SecurityService extends IGenericService<Security, Integer> {
    Optional<Security> get(String project, String env);
}
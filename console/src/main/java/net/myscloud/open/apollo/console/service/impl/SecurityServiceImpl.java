package net.myscloud.open.apollo.console.service.impl;

import net.myscloud.open.apollo.common.framework.mybatis.AbstractGenericService;
import net.myscloud.open.apollo.console.mapper.SecurityMapper;
import net.myscloud.open.apollo.console.service.SecurityService;
import net.myscloud.open.apollo.domain.model.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityServiceImpl extends AbstractGenericService<Security, Integer, SecurityMapper> implements SecurityService {

    @Autowired
    private SecurityMapper mapper;

    @Override
    protected SecurityMapper getMapper() {
        return mapper;
    }

    @Override
    public Optional<Security> get(String project, String env) {
        return Optional.ofNullable(mapper.getByProjectAndEnv(project, env));
    }
}
package net.myscloud.open.apollo.console.service.impl;

import net.myscloud.open.apollo.console.framework.mybatis.AbstractGenericService;
import net.myscloud.open.apollo.console.mapper.EnvironmentMapper;
import net.myscloud.open.apollo.console.service.EnvironmentService;
import net.myscloud.open.apollo.domain.model.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnvironmentServiceImpl extends AbstractGenericService<Environment, Integer,EnvironmentMapper> implements EnvironmentService {

    @Autowired
    private EnvironmentMapper mapper;

    @Override
    protected EnvironmentMapper getMapper() {
        return mapper;
    }
}
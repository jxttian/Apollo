package net.myscloud.open.apollo.console.service.impl;

import net.myscloud.open.apollo.common.framework.mybatis.AbstractGenericService;
import net.myscloud.open.apollo.console.mapper.ProjectMapper;
import net.myscloud.open.apollo.console.service.ProjectService;
import net.myscloud.open.apollo.domain.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl extends AbstractGenericService<Project, Integer,ProjectMapper> implements ProjectService {

    @Autowired
    private ProjectMapper mapper;

    @Override
    protected ProjectMapper getMapper() {
        return mapper;
    }
}
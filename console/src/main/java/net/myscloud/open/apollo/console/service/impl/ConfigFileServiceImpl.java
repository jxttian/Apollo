package net.myscloud.open.apollo.console.service.impl;

import net.myscloud.open.apollo.common.framework.Response;
import net.myscloud.open.apollo.common.framework.base.BaseModel;
import net.myscloud.open.apollo.common.framework.mybatis.AbstractGenericService;
import net.myscloud.open.apollo.console.mapper.ConfigFileMapper;
import net.myscloud.open.apollo.console.search.ConfigFileSearch;
import net.myscloud.open.apollo.console.service.ConfigFileService;
import net.myscloud.open.apollo.domain.model.ConfigFile;
import net.myscloud.open.yuna.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ConfigFileServiceImpl extends AbstractGenericService<ConfigFile, Integer, ConfigFileMapper> implements ConfigFileService {

    @Autowired
    private ConfigFileMapper mapper;

    @Override
    protected ConfigFileMapper getMapper() {
        return mapper;
    }

    @Override
    public Response save(User user, ConfigFile item) {
        if (item.getId() == null) {
            //新增
            item.setLatest(1);
            item.setVersion(1);
            item.setCreationTime(new Date());
            item.setCreator(user.getId());
            this.insert(item);
        } else {
            ConfigFile old = new ConfigFile();
            old.setId(item.getId());
            old.setLatest(0);
            old.setEnable(BaseModel.Enable.No.getCode());
            old.setModificationTime(new Date());
            old.setModifier(user.getId());
            this.updateSelective(old).ifPresent(/*无用*/useless -> {
                item.setVersion(item.getVersion() + 1);
                item.setId(null);
                item.setLatest(1);
                item.setCreationTime(new Date());
                item.setCreator(user.getId());
                this.insert(item);
            });
        }
        return Response.success();
    }

    @Override
    public Optional<ConfigFile> getFirst(ConfigFileSearch search) {
        return Optional.ofNullable(mapper.getFirstBySearchCondition(search));
    }
}
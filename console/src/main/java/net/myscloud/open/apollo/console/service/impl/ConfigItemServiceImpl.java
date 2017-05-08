package net.myscloud.open.apollo.console.service.impl;

import net.myscloud.open.apollo.common.framework.Response;
import net.myscloud.open.apollo.common.framework.base.BaseModel;
import net.myscloud.open.apollo.common.framework.mybatis.AbstractGenericService;
import net.myscloud.open.apollo.console.mapper.ConfigItemMapper;
import net.myscloud.open.apollo.console.service.ConfigItemService;
import net.myscloud.open.apollo.domain.model.ConfigItem;
import net.myscloud.open.yuna.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class ConfigItemServiceImpl extends AbstractGenericService<ConfigItem, Integer,ConfigItemMapper> implements ConfigItemService {

    @Autowired
    private ConfigItemMapper mapper;

    @Override
    protected ConfigItemMapper getMapper() {
        return mapper;
    }

    @Override
    @Transactional
    public Response save(User user, ConfigItem item) {
        if (item.getId() == null) {
            //新增
            item.setVersion(1);
            item.setLatest(1);
            item.setCreationTime(new Date());
            item.setCreator(user.getId());
            this.insert(item);
        } else {
            ConfigItem old = new ConfigItem();
            old.setId(item.getId());
            old.setLatest(0);
            old.setEnable(BaseModel.Enable.No.getCode());
            old.setModificationTime(new Date());
            old.setModifier(user.getId());
            this.updateSelective(old).ifPresent(/*无用*/useless -> {
                item.setId(null);
                item.setVersion(item.getVersion() + 1);
                item.setLatest(1);
                item.setCreationTime(new Date());
                item.setCreator(user.getId());
                this.insert(item);
            });
        }
        return Response.success();
    }
}
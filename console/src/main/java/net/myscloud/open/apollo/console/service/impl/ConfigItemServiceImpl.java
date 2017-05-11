package net.myscloud.open.apollo.console.service.impl;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import net.myscloud.open.apollo.common.Response;
import net.myscloud.open.apollo.common.base.BaseModel;
import net.myscloud.open.apollo.console.framework.mybatis.AbstractGenericService;
import net.myscloud.open.apollo.console.mapper.ConfigItemMapper;
import net.myscloud.open.apollo.console.service.ConfigItemService;
import net.myscloud.open.apollo.core.CuratorRegister;
import net.myscloud.open.apollo.domain.model.ConfigItem;
import net.myscloud.open.yuna.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
public class ConfigItemServiceImpl extends AbstractGenericService<ConfigItem, Integer, ConfigItemMapper> implements ConfigItemService {

    @Autowired
    private ConfigItemMapper mapper;

    @Autowired
    private CuratorRegister curatorRegister;

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
            if (this.insert(item) > 0) {
                try {
                    curatorRegister.register(String.join("/", "", item.getProject(), item.getEnv(), item.getKey()), item.getValue());
                } catch (Exception e) {
                    log.warn(e.getMessage(), e);
                    return Response.error("配置中心注册节点失败");
                }
            }
        } else {
            ConfigItem old = new ConfigItem();
            old.setId(item.getId());
            old.setLatest(0);
            old.setEnable(BaseModel.Enable.No.getCode());
            old.setModificationTime(new Date());
            old.setModifier(user.getId());
            if (!this.updateSelective(old).isPresent()) {
                return Response.error("更新数据失败");
            }
            item.setId(null);
            item.setVersion(item.getVersion() + 1);
            item.setLatest(1);
            item.setCreationTime(new Date());
            item.setCreator(user.getId());
            if (this.insert(item) > 0) {
                try {
                    curatorRegister.update(String.join("/", "", item.getProject(), item.getEnv(), item.getKey()), item.getValue());
                } catch (Exception e) {
                    log.warn(e.getMessage(), e);
                    return Response.error("配置中心更新节点失败");
                }
            }
        }
        return Response.success();
    }

    @Override
    public Response getFromRegister(String project, String env, String key) {
        try {
            return Response.success(curatorRegister.get(Joiner.on("/").join("", project, env, key)));
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return Response.error(e.getMessage());
        }
    }
}
package net.myscloud.open.apollo.core;

import lombok.Setter;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.nio.charset.Charset;

/**
 * Created by genesis on 17-5-9.
 */
public class CuratorRegister implements Register {

    private CuratorFramework client;

    @Setter
    private String namespace;

    @Setter
    private String zookeepers;

    @Override
    public void initialize() {
        client = CuratorFrameworkFactory.builder()
                .namespace(namespace)
                .connectString(zookeepers)
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(3000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.start();
    }

    @Override
    public void register(String key, String data) throws Exception {
        if (client.checkExists().forPath(key) == null) {
            //需创建的节点不存在
            client.create().creatingParentsIfNeeded().forPath(key, data.getBytes(Charset.defaultCharset()));
        } else {
            //节点已存在则更新节点
            this.update(key, data);
        }
    }

    @Override
    public void update(String key, String data) throws Exception {
        if (client.checkExists().forPath(key) != null) {
            //节点存在则更新节点数据
            client.setData().forPath(key, data.getBytes(Charset.defaultCharset()));
        } else {
            //节点不存在，则注册一个节点
            this.register(key, data);
        }
    }

    @Override
    public void remove(String key) throws Exception {
        if (client.checkExists().forPath(key) != null) {
            client.delete().forPath(key);
        }
    }

    @Override
    public String get(String key) throws Exception {
        if (client.checkExists().forPath(key) != null) {
            return new String(client.getData().forPath(key), Charset.defaultCharset());
        }
        return null;
    }

    @Override
    public void destroy() {
        client.close();
    }
}

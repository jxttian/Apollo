package net.myscloud.open.apollo.client;

import com.google.common.base.Joiner;
import lombok.Setter;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.RetryNTimes;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/**
 * Created by genesis on 17-5-9.
 */
public class ApolloConfiguration {

    @Setter
    private String project;

    @Setter
    private String env;

    @Setter
    private String zookeepers;

    private CuratorFramework client;

    private String rootPath;

    private ConcurrentHashMap<String, Object> cachedConfiguration = new ConcurrentHashMap<>();

    public Object get(String key) {
        return cachedConfiguration.get(key);
    }

    public Long getNumber(String key) {
        return (Long) cachedConfiguration.get(key);
    }

    public Boolean getBoolean(String key) {
        return Boolean.getBoolean(cachedConfiguration.get(key).toString());
    }

    public List getList(String key) {
        return (List) cachedConfiguration.get(key);
    }

    public Map getMap(String key) {
        return (Map) cachedConfiguration.get(key);
    }

    public void init() throws Exception {
        client = CuratorFrameworkFactory.newClient(
                zookeepers,
                new RetryNTimes(10, 5000)
        );
        client.start();
        rootPath = Joiner.on("/").join("", "apollo", project, env);
    }

    public void add(String key, String data) throws Exception {
        client.create().creatingParentsIfNeeded().forPath(rootPath + "/" + key, data.getBytes(Charset.defaultCharset()));
        final PathChildrenCache childrenCache = new PathChildrenCache(client, rootPath, true);
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        childrenCache.getListenable().addListener(
                (client, event) -> {
                    switch (event.getType()) {
                        case CHILD_ADDED:
                            System.out.println("CHILD_ADDED: " + event.getData().getPath());
                            break;
                        case CHILD_REMOVED:
                            System.out.println("CHILD_REMOVED: " + event.getData().getPath());
                            break;
                        case CHILD_UPDATED:
                            System.out.println("CHILD_UPDATED: " + event.getData().getPath());
                            break;
                        default:
                            break;
                    }
                },
                Executors.newFixedThreadPool(2)
        );
    }

    public void set(String key, String data) throws Exception {
        client.setData().forPath(rootPath + "/" + key, data.getBytes(Charset.defaultCharset()));
    }

    public void del(String key) throws Exception {
        client.delete().forPath(rootPath + "/" + key);
    }

    public void destory() {
        client.close();
    }
}

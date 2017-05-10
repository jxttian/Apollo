package net.myscloud.open.apollo.client.item;

import com.google.common.base.Splitter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.myscloud.open.apollo.common.kits.CollectionKits;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 存储初始化时加载的配置项，并且监听配置变化
 *
 * @since 1.0
 */
@Slf4j
public class ApolloConfiguration {

    @Setter
    @Getter
    private String projects;

    @Setter
    private String env;

    @Setter
    private String zookeepers;

    @Setter
    private String namespace = "apollo";

    @Setter
    private int threadPoolSize = 2;

    private CuratorFramework client;

    private ExecutorService pool;

    @Getter
    private ConcurrentHashMap<String, ConcurrentHashMap<String, String>>
            cachedConfigurations = new ConcurrentHashMap<>();

    /**
     * 获取某一项目的某配置
     *
     * @param project
     * @param key
     * @return
     */
    public String get(String project, String key) {
        ConcurrentHashMap<String, String> cachedConfiguration = cachedConfigurations.get(project);
        if (CollectionKits.isNotEmpty(cachedConfiguration))
            return cachedConfiguration.get(key);
        log.warn("未找到项目[{}]在[{}]环境下的[{}]配置,请检查代码或者在配置中心增加配置", project, env, key);
        return null;
    }

    public void init() throws Exception {
        pool = Executors.newFixedThreadPool(threadPoolSize);
        Splitter.on(",").trimResults().omitEmptyStrings().splitToList(projects).forEach(project -> {
            try {
                String rootPath = String.join("/", "", project, env);
                client = CuratorFrameworkFactory.builder()
                        .connectString(zookeepers)
                        .namespace(namespace)
                        .sessionTimeoutMs(2000)
                        .connectionTimeoutMs(3000)
                        .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                        .build();
                client.start();
                final PathChildrenCache childrenCache = new PathChildrenCache(client, rootPath, true);
                childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
                childrenCache.getListenable().addListener(
                        (client1, event) -> {
                            switch (event.getType()) {
                                case CHILD_ADDED:
                                case CHILD_UPDATED:
                                    String key = event.getData().getPath().replace(rootPath + "/", "");
                                    String value = new String(event.getData().getData(), Charset.defaultCharset());
                                    ConcurrentHashMap<String, String> cachedConfiguration = cachedConfigurations.computeIfAbsent(project, useless -> new ConcurrentHashMap<>());
                                    cachedConfiguration.put(key, value);
                                    log.info("成功加载项目[{}]在[{}]环境下的[{}]配置 => {}", project, env, key, value);
                                    break;
                                default:
                                    break;
                            }
                        },
                        pool
                );
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
            }
        });
    }

    public void destroy() {
        pool.shutdown();
        client.close();
    }
}

package net.myscloud.open.apollo.client.test;

import com.alibaba.fastjson.JSON;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by genesis on 17-2-9.
 */
public class CuratorTest {

//    public static void main(String[] args) throws Exception {
//        ApolloConfiguration apolloConfiguration = new ApolloConfiguration();
//        apolloConfiguration.setProject("bill_cms");
//        apolloConfiguration.setEnv("develop");
//        apolloConfiguration.setZookeepers("101.37.19.33:2181");
//        apolloConfiguration.init();
//        apolloConfiguration.add("test", "test");
//        System.out.println(apolloConfiguration.get("test"));
//        apolloConfiguration.set("test", "test222");
//        System.out.println(apolloConfiguration.get("test"));
//        apolloConfiguration.del("test");
//        apolloConfiguration.destory();
//    }


    public static void main(String[] args) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("101.37.19.33:2181")
                .sessionTimeoutMs(2000)
                .connectionTimeoutMs(3000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.start();
        client.delete().forPath("/zk-huey/cnode");
        client.create()
                .creatingParentsIfNeeded()
                .forPath("/zk-huey/cnode", "hello".getBytes());
        /**
         * 在注册监听器的时候，如果传入此参数，当事件触发时，逻辑由线程池处理
         */
        ExecutorService pool = Executors.newFixedThreadPool(2);
        /**
         * 监听数据节点的变化情况
         */
        final NodeCache nodeCache = new NodeCache(client, "/zk-huey/cnode", false);
        nodeCache.start(true);
        nodeCache.getListenable().addListener(
                new NodeCacheListener() {
                    @Override
                    public void nodeChanged() throws Exception {
                        System.out.println("Node data is changed, new data: " +
                                new String(nodeCache.getCurrentData().getData()));
                    }
                },
                pool
        );
        /**
         * 监听子节点的变化情况
         */
        final PathChildrenCache childrenCache = new PathChildrenCache(client, "/zk-huey", true);
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        childrenCache.getListenable().addListener(
                new PathChildrenCacheListener() {
                    @Override
                    public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
                            throws Exception {
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
                    }
                },
                pool
        );
        client.setData().forPath("/zk-huey/cnode", "world".getBytes());
        Thread.sleep(2 * 1000);
        client.setData().forPath("/zk-huey/cnode", "worl23d".getBytes());
        Thread.sleep(2 * 1000);
        client.setData().forPath("/zk-huey/cnode", "wor23l23d".getBytes());
        Thread.sleep(2 * 1000);
        System.out.println(JSON.toJSONString(client.checkExists().forPath("/zk-huey/test")));
        client.create().creatingParentsIfNeeded().forPath("/zk-huey/test", "test".getBytes());
        Thread.sleep(2 * 1000);
        client.setData().forPath("/zk-huey/test", "tes2323t".getBytes());
        Thread.sleep(2 * 1000);
        System.out.println(JSON.toJSONString(client.checkExists().forPath("/zk-huey/test")));
        Thread.sleep(2 * 1000);
        client.delete().forPath("/zk-huey/test");
        Thread.sleep(2 * 1000);

        pool.shutdown();
        client.close();
    }
}

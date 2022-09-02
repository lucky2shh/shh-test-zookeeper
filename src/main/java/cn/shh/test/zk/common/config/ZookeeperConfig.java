package cn.shh.test.zk.common.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.CountDownLatch;

@Slf4j
@EnableConfigurationProperties(value = ZKProperties.class)
@Configuration
public class ZookeeperConfig {
    @Autowired
    private ZKProperties properties;

    @Bean
    public CuratorFramework curatorFramework (){
        //重试策略，初试时间1秒，重试10次
        RetryPolicy policy = new ExponentialBackoffRetry(properties.getBaseSleepTimeMs(), properties.getMaxRetries());
        //通过工厂创建Curator
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(properties.getConnectString())
                .authorization("digest", properties.getDigest().getBytes())
                .connectionTimeoutMs(properties.getConnectionTimeoutMs())
                .sessionTimeoutMs(properties.getSessionTimeoutMs())
                .namespace(properties.getNamespace())
                .retryPolicy(policy).build();
        //开启连接
        curatorFramework.start();
        log.info("zookeeper 初始化完成...");
        return curatorFramework;
    }

    @Bean(name = "zkClient")
    public ZooKeeper zooKeeper() {
        ZooKeeper zooKeeper = null;
        try {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            zooKeeper = new ZooKeeper(properties.getConnectString(), properties.getSessionTimeoutMs(), new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (Event.KeeperState.SyncConnected == event.getState()) {
                        countDownLatch.countDown();
                    }
                }
            });
            countDownLatch.await();
            log.info("【初始化ZooKeeper连接状态】= {}", zooKeeper.getState());
        } catch (Exception e) {
            log.error("初始化ZooKeeper连接异常】= {}", e);
        }
        return zooKeeper;
    }
}

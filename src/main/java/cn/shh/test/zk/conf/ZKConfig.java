package cn.shh.test.zk.conf;

import cn.shh.test.zk.watch.ZookeeperWatches;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(value = ZKProperties.class)
@Configuration
public class ZKConfig {
    @Bean
    public CuratorFramework curatorFramework(ZKProperties porperties) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                //连接地址  集群用,隔开
                .connectString(porperties.getIp())
                .connectionTimeoutMs(porperties.getConnectionTimeoutMs())
                //会话超时时间
                .sessionTimeoutMs(porperties.getSessionTimeOut())
                //设置重试机制
                .retryPolicy(new ExponentialBackoffRetry(porperties.getSleepMsBetweenRetry(),porperties.getMaxRetries()))
                //设置命名空间 在操作节点的时候，会以这个为父节点
                .namespace(porperties.getNamespace())
                .build();
        client.start();

        //注册监听器
        ZookeeperWatches watches = new ZookeeperWatches(client);
        watches.znodeWatcher();
        watches.znodeChildrenWatcher();
        return client;
    }
}

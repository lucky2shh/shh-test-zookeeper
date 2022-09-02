package cn.shh.test.zk.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "zookeeper")
public class ZKProperties {
    private Boolean enabled;
    private String connectString;
    private String namespace;
    private String digest;
    private Integer sessionTimeoutMs;
    private Integer connectionTimeoutMs;
    private Integer maxRetries;
    private Integer baseSleepTimeMs;
}

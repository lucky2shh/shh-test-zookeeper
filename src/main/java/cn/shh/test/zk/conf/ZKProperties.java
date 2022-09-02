package cn.shh.test.zk.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "zookeeper.curator")
public class ZKProperties {
    private String ip;
    private Integer connectionTimeoutMs;
    private Integer sessionTimeOut;
    private Integer sleepMsBetweenRetry;
    private Integer maxRetries;
    private String namespace;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setConnectionTimeoutMs(Integer connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public Integer getSessionTimeOut() {
        return sessionTimeOut;
    }

    public void setSessionTimeOut(Integer sessionTimeOut) {
        this.sessionTimeOut = sessionTimeOut;
    }

    public Integer getSleepMsBetweenRetry() {
        return sleepMsBetweenRetry;
    }

    public void setSleepMsBetweenRetry(Integer sleepMsBetweenRetry) {
        this.sleepMsBetweenRetry = sleepMsBetweenRetry;
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}

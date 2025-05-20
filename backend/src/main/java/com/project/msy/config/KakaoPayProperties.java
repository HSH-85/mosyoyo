package com.project.msy.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kakao.api")
public class KakaoPayProperties {
    private String restKey;
    private String cid;
    private String host;
    private String frontHost;

    public String getRestKey() { return restKey; }
    public void setRestKey(String restKey) { this.restKey = restKey; }

    public String getCid() { return cid; }
    public void setCid(String cid) { this.cid = cid; }

    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }

    public String getFrontHost() { return frontHost; }
    public void setFrontHost(String frontHost) { this.frontHost = frontHost; }
}
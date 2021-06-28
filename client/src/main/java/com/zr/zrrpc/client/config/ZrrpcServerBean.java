package com.zr.zrrpc.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <h3>ZR-RPC</h3>
 * <h4>com.zr.zrrpc.server.config</h4>
 * <p></p>
 *
 * @author : richlogic
 * @since : 2021/06/16
 */
@Configuration
@ConfigurationProperties(prefix="spring.zrrpc.server")
public class ZrrpcServerBean {

    private String name;
    private String[] domain;
    private String[] classes;

    public String getName() {
        return name;
    }

    public ZrrpcServerBean setName(String name) {
        this.name = name;
        return this;
    }

    public String[] getDomain() {
        return domain;
    }

    public ZrrpcServerBean setDomain(String[] domain) {
        this.domain = domain;
        return this;
    }

    public String[] getClasses() {
        return classes;
    }

    public ZrrpcServerBean setClasses(String[] classes) {
        this.classes = classes;
        return this;
    }
}

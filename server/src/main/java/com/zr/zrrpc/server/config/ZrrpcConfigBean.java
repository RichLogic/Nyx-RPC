package com.zr.zrrpc.server.config;

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
@ConfigurationProperties(prefix="spring.zrrpc")
public class ZrrpcConfigBean {

    private ZrrpcServerBean[] server;

    public ZrrpcServerBean[] getServer() {
        return server;
    }

    public ZrrpcConfigBean setServer(ZrrpcServerBean[] server) {
        this.server = server;
        return this;
    }
}

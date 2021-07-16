package com.zr.zpc.core.model;

/**
 * <h3>zrrpc.client</h3>
 * <h4>com.zr.zpc.core.model</h4>
 * <p></p>
 *
 * @author : zhouning
 * @since : 2021/06/28
 */
public class RpcConnectParams {
    private String serviceName;
    private String host;
    private int port;

    public String getServiceName() {
        return serviceName;
    }

    public RpcConnectParams setServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public String getHost() {
        return host;
    }

    public RpcConnectParams setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public RpcConnectParams setPort(int port) {
        this.port = port;
        return this;
    }

    @Override
    public String toString() {
        return "RpcConnectParams{" +
                "serviceName='" + serviceName + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
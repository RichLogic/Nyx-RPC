package com.zr.zpc.core.model;

/**
 * <h3>ZR-RPC</h3>
 * <h4>com.zr.zpc.core.model</h4>
 * <p></p>
 *
 * @author : richlogic
 * @since : 2021/07/15
 */
public class ZkNode {

    private String serverName;
    private String ip;
    private int port;

    public ZkNode() {
    }

    public ZkNode(String serverName, String ip, int port) {
        this.serverName = serverName;
        this.ip = ip;
        this.port = port;
    }

    public String getServerName() {
        return serverName;
    }

    public ZkNode setServerName(String serverName) {
        this.serverName = serverName;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public ZkNode setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public int getPort() {
        return port;
    }

    public ZkNode setPort(int port) {
        this.port = port;
        return this;
    }

    @Override
    public String toString() {
        return "ZkNode{" +
                "serverName='" + serverName + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}

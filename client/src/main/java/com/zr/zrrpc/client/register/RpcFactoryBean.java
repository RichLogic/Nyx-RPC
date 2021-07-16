package com.zr.zrrpc.client.register;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * <h3>ZR-RPC</h3>
 * <h4>com.zr.zrrpc.client.register</h4>
 * <p></p>
 *
 * @author : richlogic
 * @since : 2021/06/23
 */
public class RpcFactoryBean implements FactoryBean<Object> {

    private String proxyClassName;
    private String serverName;
    private String interfaceName;

    @Override
    public Object getObject() throws ClassNotFoundException {
        return Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[]{Class.forName(proxyClassName)},
                new InvokeHandler(serverName, interfaceName)
        );
    }

    @Override
    public Class<?> getObjectType() {
        try {
            return Class.forName(proxyClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getProxyClassName() {
        return proxyClassName;
    }

    public RpcFactoryBean setProxyClassName(String proxyClassName) {
        this.proxyClassName = proxyClassName;
        return this;
    }

    public String getServerName() {
        return serverName;
    }

    public RpcFactoryBean setServerName(String serverName) {
        this.serverName = serverName;
        return this;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public RpcFactoryBean setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
        return this;
    }
}

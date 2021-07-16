package com.zr.zpc.core.model;

import java.io.Serializable;

/**
 * <h3>ZR-RPC</h3>
 * <h4>com.zr.zrrpc.client.register</h4>
 * <p></p>
 *
 * @author : richlogic
 * @since : 2021/06/27
 */
public class RpcInvoker implements Serializable {

    private Class<?> rpcInterface;
    private String serverName;
    private String interfaceName;
    private String method;
    private Class<?>[] paramClasses;
    private Object[] params;
    private Class<?> returnType;

    public Class<?> getRpcInterface() {
        return rpcInterface;
    }

    public RpcInvoker setRpcInterface(Class<?> rpcInterface) {
        this.rpcInterface = rpcInterface;
        return this;
    }

    public String getServerName() {
        return serverName;
    }

    public RpcInvoker setServerName(String serverName) {
        this.serverName = serverName;
        return this;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public RpcInvoker setInterfaceName(String[] interfaceNames) throws Exception {
        if (interfaceNames.length > 1) {
            throw new Exception("接口大于一个");
        }
        this.interfaceName = interfaceNames[0];
        return this;
    }

    public RpcInvoker setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public RpcInvoker setMethod(String method) {
        this.method = method;
        return this;
    }

    public Class<?>[] getParamClasses() {
        return paramClasses;
    }

    public RpcInvoker setParamClasses(Class<?>[] paramClasses) {
        this.paramClasses = paramClasses;
        return this;
    }

    public Object[] getParams() {
        return params;
    }

    public RpcInvoker setParams(Object[] params) {
        this.params = params;
        return this;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public RpcInvoker setReturnType(Class<?> returnType) {
        this.returnType = returnType;
        return this;
    }
}

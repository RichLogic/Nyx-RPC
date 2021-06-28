package com.zr.zpc.core.model;

/**
 * <h3>ZR-RPC</h3>
 * <h4>com.zr.zrrpc.client.register</h4>
 * <p></p>
 *
 * @author : richlogic
 * @since : 2021/06/27
 */
public class RpcInvoker {

    private Class<?> rpcInterface;
    private String serviceName;
    private String interfaceName;

    public Class<?> getRpcInterface() {
        return rpcInterface;
    }

    public RpcInvoker setRpcInterface(Class<?> rpcInterface) {
        this.rpcInterface = rpcInterface;
        return this;
    }

    public String getServiceName() {
        return serviceName;
    }

    public RpcInvoker setServiceName(String serviceName) {
        this.serviceName = serviceName;
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
}

package com.zr.zrrpc.client.register;

import com.zr.zpc.core.model.RpcInvoker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.IntFunction;

/**
 * <h3>ZR-RPC</h3>
 * <h4>com.zr.zrrpc.client.register</h4>
 * <p></p>
 *
 * @author : richlogic
 * @since : 2021/06/23
 */
public class InvokeHandler implements InvocationHandler {

    private final String serverName;
    private final String interfaceName;

    public InvokeHandler(String serverName, String interfaceName) {
        this.serverName = serverName;
        this.interfaceName = interfaceName;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {

        RpcInvoker invoker = new RpcInvoker()
                .setServerName(serverName)
                .setInterfaceName(interfaceName)
                .setMethod(method.getName())
                .setParamClasses(Arrays.stream(args).map(Object::getClass).toArray((IntFunction<Class<?>[]>) Class[]::new))
                .setParams(args)
                .setReturnType(method.getReturnType());
        return invoker;
    }

}

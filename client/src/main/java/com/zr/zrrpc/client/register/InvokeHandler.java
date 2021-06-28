package com.zr.zrrpc.client.register;

import com.zr.zpc.core.model.RpcInvoker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <h3>ZR-RPC</h3>
 * <h4>com.zr.zrrpc.client.register</h4>
 * <p></p>
 *
 * @author : richlogic
 * @since : 2021/06/23
 */
public class InvokeHandler implements InvocationHandler {

    private RpcInvoker invoker;

    public InvokeHandler(RpcInvoker invoker) {
        this.invoker = invoker;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        // object的公用方法直接调用当前invoke对象的。
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
            // 针对接口的不同方法书写我们具体的实现
        }
        return invoker.getServiceName() + ":" + invoker.getInterfaceName() + ":" + method.getName();
    }

}

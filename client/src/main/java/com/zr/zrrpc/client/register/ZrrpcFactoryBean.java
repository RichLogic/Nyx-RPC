package com.zr.zrrpc.client.register;

import com.zr.zpc.core.model.RpcInvoker;
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
public class ZrrpcFactoryBean implements FactoryBean<Object> {

    private RpcInvoker invoker;

    @Override
    public Object getObject() {
        return Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[]{invoker.getRpcInterface()},
                new InvokeHandler(invoker)
        );
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    public RpcInvoker getInvoker() {
        return invoker;
    }

    public ZrrpcFactoryBean setInvoker(RpcInvoker invoker) {
        this.invoker = invoker;
        return this;
    }
}

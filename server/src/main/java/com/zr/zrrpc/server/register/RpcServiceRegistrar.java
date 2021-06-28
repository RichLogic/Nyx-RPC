package com.zr.zrrpc.server.register;

import com.zr.zpc.core.exception.RpcException;
import com.zr.zrrpc.server.annotation.RpcService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <h3>ZR-RPC</h3>
 * <h4>com.zr.zrrpc.server.register</h4>
 * <p></p>
 *
 * @author : richlogic
 * @since : 2021/06/28
 */
@Configuration
public class RpcServiceRegistrar implements ImportBeanDefinitionRegistrar, ApplicationContextAware {

    private static final Map<String, Object> BEAN_MAP = new ConcurrentHashMap<>(64);
    private static final Set<String> NOT_RPC_INTERFACE_SET = new HashSet<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        AnnotationConfigApplicationContext context = (AnnotationConfigApplicationContext) applicationContext;
        Map<String, Object> serviceMap = context.getBeansWithAnnotation(RpcService.class);

        for (String key : serviceMap.keySet()) {
            Object service = serviceMap.get(key);
            Arrays.stream(service.getClass().getInterfaces())
                    .forEach(x -> {
                        if (NOT_RPC_INTERFACE_SET.contains(x.getName())) {
                            return;
                        }
                        if (BEAN_MAP.containsKey(x.getName())) {
                            NOT_RPC_INTERFACE_SET.add(x.getName());
                            return;
                        }
                        BEAN_MAP.put(x.getName(), service);
            });
        }

    }

    public Object getService(String key) throws RpcException {
        if (!BEAN_MAP.containsKey(key)) {
            throw new RpcException("没有服务");
        }
        return BEAN_MAP.get(key);
    }
}

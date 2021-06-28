package com.zr.zrrpc.server;

import com.zr.zrrpc.server.bean.ZrrpcServer;
import com.zr.zrrpc.server.config.ZrrpcConfigBean;
import com.zr.zrrpc.server.config.ZrrpcServerBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class RpcProxyRegister implements BeanDefinitionRegistryPostProcessor {

    private final Map<String, List<ZrrpcServer>> ZRRPC_SERVER_MAP = new HashMap<>();
    private final List<String> CLASS_LIST = new ArrayList<>();

    @Resource
    private ZrrpcConfigBean zrrpcConfigBean;

    @Resource
    private Environment environment;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

//        this.handleConfig();
        System.out.println();

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    private void handleConfig() {
        ZrrpcServerBean[] servers = zrrpcConfigBean.getServer();
        for (ZrrpcServerBean bean : servers) {
            CLASS_LIST.addAll(Arrays.asList(bean.getClasses()));

            List<ZrrpcServer> serverList = Arrays.stream(bean.getDomain())
                    .map(x -> {
                        String[] domain = x.split(":");
                        return new ZrrpcServer()
                                .setIp(domain[0])
                                .setPort(Integer.valueOf(domain[1]));
                    }).collect(Collectors.toList());
            ZRRPC_SERVER_MAP.put(bean.getName(), serverList);
        }
    }


}
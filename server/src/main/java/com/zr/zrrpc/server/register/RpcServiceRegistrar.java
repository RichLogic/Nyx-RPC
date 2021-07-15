package com.zr.zrrpc.server.register;

import com.zr.zpc.core.exception.RpcException;
import com.zr.zrrpc.server.annotation.RpcService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;
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
public class RpcServiceRegistrar implements ImportBeanDefinitionRegistrar, ApplicationContextAware, EnvironmentAware {

    private static final Map<String, Object> BEAN_MAP = new ConcurrentHashMap<>(64);

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        String serverName = environment.getProperty("spring.rpc.server.name");
        String listenPort = environment.getProperty("spring.rpc.server.port");



        System.out.println();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.registerRpcServer();

        AnnotationConfigApplicationContext context = (AnnotationConfigApplicationContext) applicationContext;
        this.collectRpcService(context);

    }

    private void registerRpcServer() {

        String serverName = environment.getProperty("spring.rpc.server.name");
        String listenPort = environment.getProperty("spring.rpc.server.port");
        // 启动 Netty 服务端，并拿到 ip
        // 现在假设 ip 是 localhost
        String localIp = "127.0.0.1";
    }

    private void collectRpcService(AnnotationConfigApplicationContext context) {
        Map<String, Object> serviceMap = context.getBeansWithAnnotation(RpcService.class);
        Set<String> interfaceSet = new HashSet<>();
        for (Object service : serviceMap.values()) {

            Class<?>[] classes = service.getClass().getInterfaces();
            if (classes.length != 1) {
                throw new RuntimeException(service.getClass().getName() + "能且仅能实现一个接口");
            }
            Class<?> inter = classes[0];
            if (interfaceSet.contains(inter.getName())) {
                throw new RuntimeException(inter.getName() + "接口被多次实现，不符合要求");
            }
            interfaceSet.add(inter.getName());
            BEAN_MAP.put(inter.getName(), service);
        }
    }

    public static Object getService(String key) throws RpcException {
        if (!BEAN_MAP.containsKey(key)) {
            throw new RpcException("没有服务");
        }
        return BEAN_MAP.get(key);
    }

    public static void main(String args[]) {

        HashSet<String> addrSet = new HashSet<>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress addr = inetAddresses.nextElement();
                    if (addr.isLoopbackAddress()) {
                        continue;
                    }

                    String ip = addr.getHostAddress();
                    //skip the IPv6 addr
                    if (ip.contains(":")) {
                        continue;
                    }
                    addrSet.add(ip);
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        addrSet.forEach(System.out::println);
    }
}

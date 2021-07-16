package com.zr.zrrpc.server.register;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zr.zpc.core.exception.RpcException;
import com.zr.zpc.core.model.Constance;
import com.zr.zpc.core.model.ZkNode;
import com.zr.zrrpc.server.annotation.RpcService;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
public class RpcServiceRegistrar implements ApplicationContextAware, EnvironmentAware {

    public static final Map<String, Object> BEAN_MAP = new ConcurrentHashMap<>(64);
    public static ZooKeeper zooKeeper;
    public static Gson gson;

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;

        String zkConnectString = environment.getProperty("spring.rpc.zookeeper.connect-string");
        if (Strings.isNullOrEmpty(zkConnectString)) {
            throw new RuntimeException("无法连接 zookeeper");
        }
        try {
            zooKeeper = new ZooKeeper(zkConnectString, 2000, null);
        } catch (IOException e) {
            throw new RuntimeException("无法连接 zookeeper");
        }

        gson = new GsonBuilder().disableHtmlEscaping().create();
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

        if (Strings.isNullOrEmpty(listenPort)) {
            throw new RuntimeException("rpc port为空，无法连接");
        }
        ZkNode node = new ZkNode(serverName, localIp, Integer.parseInt(listenPort));

        try {
            if (zooKeeper.exists(Constance.RPC + serverName, false) == null) {
                zooKeeper.create(Constance.RPC + serverName, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            zooKeeper.create(Constance.RPC + serverName + Constance.SERVER, gson.toJson(node).getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }

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

}

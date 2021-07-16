package com.zr.zrrpc.client.register;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zr.zpc.core.model.Constance;
import com.zr.zpc.core.model.RpcInvoker;
import com.zr.zpc.core.model.ZkNode;
import com.zr.zrrpc.client.annotation.RpcClient;
import org.apache.zookeeper.*;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.Introspector;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * <h3>ZR-RPC</h3>
 * <h4>com.zr.zrrpc.client.register</h4>
 * <p></p>
 *
 * @author : richlogic
 * @since : 2021/06/21
 */
public class RpcClientRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware, ResourceLoaderAware {

    public static final Map<String, List<ZkNode>> NODE_MAP = new ConcurrentHashMap<>(64);
    public static ZooKeeper zooKeeper;
    public static Gson gson;

    private ResourceLoader resourceLoader;

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
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {

        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                return beanDefinition.getMetadata().isIndependent() && (!beanDefinition.getMetadata().isAnnotation());
            }
        };
        scanner.setResourceLoader(this.resourceLoader);
        scanner.addIncludeFilter(new AnnotationTypeFilter(RpcClient.class));

        String packageName = ClassUtils.getPackageName(metadata.getClassName());
        Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(packageName);
        for (BeanDefinition candidateComponent : candidateComponents) {
            if (!(candidateComponent instanceof AnnotatedBeanDefinition)) {
                continue;
            }

            // 注册这个 service
            String serverName = this.registerBeanDefinition((AnnotatedBeanDefinition) candidateComponent, registry);

            // 拉取服务 zk 节点
            this.pullFromZk(serverName);
        }

    }

    private String registerBeanDefinition(AnnotatedBeanDefinition beanDefinition, BeanDefinitionRegistry registry) {

        AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
        Assert.isTrue(annotationMetadata.isInterface(), "@ZrrpcClient can only be specified on an interface");

        Map<String, Object> attributes = annotationMetadata.getAnnotationAttributes(RpcClient.class.getCanonicalName());
        assert attributes != null;

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(RpcFactoryBean.class);
        builder.setScope("singleton");

        builder.addPropertyValue("proxyClassName", annotationMetadata.getClassName());
        builder.addPropertyValue("serverName", attributes.get("value"));
        builder.addPropertyValue("interfaceName", annotationMetadata.getInterfaceNames());

        registry.registerBeanDefinition(this.handleClassName(annotationMetadata.getClassName()), builder.getBeanDefinition());

        return (String) attributes.get("value");
    }

    private String handleClassName(String fullClassName) {
        String shortClassName = ClassUtils.getShortName(fullClassName);
        return Introspector.decapitalize(shortClassName);
    }

    private void pullFromZk(String serverName) {
        RpcClientRegistrar.pullNodeList(zooKeeper, serverName);
        try {
            zooKeeper.addWatch(Constance.RPC + serverName, new RpcWatch(zooKeeper, serverName), AddWatchMode.PERSISTENT);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void pullNodeList(ZooKeeper zooKeeper, String serverName) {
        List<String> nodeList = null;
        try {
            nodeList = zooKeeper.getChildren(Constance.RPC + serverName, false);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        if (nodeList == null) {
            return;
        }

        List<ZkNode> zkNodeList = nodeList.stream().map(x -> {
            try {
                byte[] value = zooKeeper.getData(Constance.RPC + serverName + "/" + x, false, null);
                return gson.fromJson(new String(value), ZkNode.class);
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        NODE_MAP.put(serverName, zkNodeList);

        System.out.println(gson.toJson(NODE_MAP));
    }

    static class RpcWatch implements Watcher {

        private final ZooKeeper zooKeeper;
        private final String serverName;

        public RpcWatch(ZooKeeper zooKeeper, String serverName) {
            this.zooKeeper = zooKeeper;
            this.serverName = serverName;
        }

        @Override
        public void process(WatchedEvent event) {
            RpcClientRegistrar.pullNodeList(zooKeeper, serverName);
        }
    }

}

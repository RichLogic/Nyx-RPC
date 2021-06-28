package com.zr.zrrpc.client.register;

import com.zr.zpc.core.model.RpcInvoker;
import com.zr.zrrpc.client.annotation.RpcClient;
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
import java.util.Map;
import java.util.Set;

/**
 * <h3>ZR-RPC</h3>
 * <h4>com.zr.zrrpc.client.register</h4>
 * <p></p>
 *
 * @author : richlogic
 * @since : 2021/06/21
 */
public class ZrrpcClientRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware, ResourceLoaderAware {

    private ResourceLoader resourceLoader;

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
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
            if (candidateComponent instanceof AnnotatedBeanDefinition) {

                AnnotationMetadata annotationMetadata = ((AnnotatedBeanDefinition) candidateComponent).getMetadata();
                Assert.isTrue(annotationMetadata.isInterface(), "@ZrrpcClient can only be specified on an interface");

                Map<String, Object> attributes = annotationMetadata.getAnnotationAttributes(RpcClient.class.getCanonicalName());
                assert attributes != null;

                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ZrrpcFactoryBean.class);
                builder.setScope("singleton");

                RpcInvoker invoker = new RpcInvoker();
                try {
                    invoker.setRpcInterface(Class.forName(annotationMetadata.getClassName()))
                            .setServiceName((String) attributes.get("value"))
                            .setInterfaceName(annotationMetadata.getInterfaceNames())
                    ;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                builder.addPropertyValue("invoker", invoker);

                registry.registerBeanDefinition(this.handleClassName(annotationMetadata.getClassName()), builder.getBeanDefinition());
            }
        }

    }

    private String handleClassName(String fullClassName) {
        String shortClassName = ClassUtils.getShortName(fullClassName);
        return Introspector.decapitalize(shortClassName);
    }

}

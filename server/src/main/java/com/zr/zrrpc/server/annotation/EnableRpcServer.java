package com.zr.zrrpc.server.annotation;

import com.zr.zrrpc.server.register.RpcServiceRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <h3>ZR-RPC</h3>
 * <h4>com.zr.zrrpc.server.annotation</h4>
 * <p></p>
 *
 * @author : richlogic
 * @since : 2021/06/17
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RpcServiceRegistrar.class)
public @interface EnableRpcServer {

}
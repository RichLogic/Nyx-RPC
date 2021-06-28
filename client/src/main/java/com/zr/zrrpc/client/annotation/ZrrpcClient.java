package com.zr.zrrpc.client.annotation;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * <h3>ZR-RPC</h3>
 * <h4>com.zr.zrrpc.client.annotation</h4>
 * <p></p>
 *
 * @author : richlogic
 * @since : 2021/06/26
 */
@Service
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ZrrpcClient {

    String value();

}

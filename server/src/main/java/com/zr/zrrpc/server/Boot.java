package com.zr.zrrpc.server;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * <h3>ZR-RPC</h3>
 * <h4>com.zr.zrrpc.server</h4>
 * <p></p>
 *
 * @author : richlogic
 * @since : 2021/06/15
 */
@SpringBootApplication
public class Boot {

    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(Boot.class).run(args);
    }

}

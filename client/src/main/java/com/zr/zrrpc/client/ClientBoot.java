package com.zr.zrrpc.client;

import com.zr.zrrpc.client.annotation.EnableRpcClient;
import com.zr.zrrpc.client.test.UserRpcClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * <h3>ZR-RPC</h3>
 * <h4>com.zr.zrrpc.server</h4>
 * <p></p>
 *
 * @author : richlogic
 * @since : 2021/06/15
 */
@SpringBootApplication
@EnableRpcClient
public class ClientBoot {

    @Resource
    private UserRpcClient userRpcClient;

    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(ClientBoot.class).run(args);

        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void test() {
        String name = userRpcClient.getName();
        System.out.println(name);
    }

}

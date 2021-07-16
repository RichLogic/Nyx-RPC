package com.zr.zrrpc.server;

import com.zr.zpc.core.model.RpcInvoker;
import com.zr.zpc.core.test.IUserService;
import com.zr.zrrpc.server.annotation.EnableRpcServer;
import com.zr.zrrpc.server.register.InvokeService;
import com.zr.zrrpc.server.service.ZRRpcServer;
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
@EnableRpcServer
public class ServerBoot {

    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(ServerBoot.class).run(args);
        try {
            Thread.sleep(10000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Resource
    private InvokeService service;

    @Resource
    private ZRRpcServer zrRpcServer;

    @PostConstruct
    public void init() {
        zrRpcServer.start();

//        RpcInvoker invoker = new RpcInvoker();
//        invoker.setInterfaceName(IUserService.class.getName());
//        invoker.setMethod("getName");
//
//        Object name = service.invoke(invoker);
//        System.out.println(name);
    }

}

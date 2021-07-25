package com.zr.zrrpc.server.handler;

import com.zr.zpc.core.model.RpcConnectParams;
import com.zr.zpc.core.model.RpcInvoker;
import com.zr.zrrpc.server.register.InvokeService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

import javax.annotation.Resource;

/**
 * <h3>zrrpc.client</h3>
 * <h4>com.zr.zrrpc.client.handler</h4>
 * <p></p>
 *
 * @author : zhouning
 * @since : 2021/06/16
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcInvoker> {


    @Resource
    private InvokeService invokeService;
    private final RpcConnectParams rpcConnectParams;

    public NettyServerHandler(final RpcConnectParams rpcConnectParams) {
        this.rpcConnectParams = rpcConnectParams;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcInvoker request) throws Exception {
        System.out.println("class:" + request.getClass().getName());
        invokeService.invoke(request);
    }
}
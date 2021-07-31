package com.zr.zrrpc.server.handler;

import com.zr.zpc.core.model.RpcConnectParams;
import com.zr.zpc.core.model.RpcInvoker;
import com.zr.zpc.core.model.RpcResult;
import com.zr.zrrpc.server.register.InvokeService;
import com.zr.zrrpc.server.register.RpcServiceRegistrar;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.internal.ThrowableUtil;

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
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcInvoker invoker) throws Exception {
        System.out.println("class:" + invoker.getClass().getName());
        try {
            RpcResult result = invokeService.invoke(invoker);
            channelHandlerContext.writeAndFlush(result);
        } catch (Exception e) {
            RpcResult<String> rpcResult = new RpcResult<>();
            rpcResult.setResult("exception");
            rpcResult.setExceptionStackInfo(e.getMessage());
            channelHandlerContext.writeAndFlush(rpcResult);
        }
    }
}
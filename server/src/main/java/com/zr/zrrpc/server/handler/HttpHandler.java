package com.zr.zrrpc.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * <h3>zrrpc.client</h3>
 * <h4>com.zr.zrrpc.client.handler</h4>
 * <p></p>
 *
 * @author : zhouning
 * @since : 2021/06/16
 */
public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest msg) throws Exception {
        System.out.println("class:" + msg.getClass().getName());
    }
}
package com.zr.zrrpc.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

import java.nio.charset.Charset;

/**
 * <h3>zrrpc.client</h3>
 * <h4>com.zr.zrrpc.client.handler</h4>
 * <p></p>
 *
 * @author : zhouning
 * @since : 2021/06/16
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            String value = ((ByteBuf) msg).toString(Charset.defaultCharset());
            System.out.println("服务器端返回的数据:" + value);
        }

        AttributeKey<String> key = AttributeKey.valueOf("ServerData");
        ctx.channel().attr(key).set("客户端处理完毕");

        ctx.channel().close();
    }
}
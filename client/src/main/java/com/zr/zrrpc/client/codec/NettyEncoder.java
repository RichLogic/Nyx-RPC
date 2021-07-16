package com.zr.zrrpc.client.codec;

import com.zr.zrrpc.client.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * <h3>zrrpc.client</h3>
 * <h4>com.zr.zrrpc.client.codec</h4>
 * <p></p>
 *
 * @author : zhouning
 * @since : 2021/07/15
 */
public class NettyEncoder extends MessageToByteEncoder<Object> {
    private Class<?> genericClass;
    private Serializer serializer;

    public NettyEncoder(Class<?> genericClass, final Serializer serializer) {
        this.genericClass = genericClass;
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {

    }
}
package com.zr.zpc.core.codec;

import com.zr.zpc.core.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * <h3>zrrpc.client</h3>
 * <h4>com.zr.zpc.core.codec</h4>
 * <p></p>
 *
 * @author : zhouning
 * @since : 2021/07/15
 */
public class NettyEncoder extends MessageToByteEncoder<Object> {
    private Class<?> genericClass;

    public NettyEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {

    }
}
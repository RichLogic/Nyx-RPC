package com.zr.zpc.core.codec;

import com.zr.zpc.core.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * <h3>zrrpc.client</h3>
 * <h4>com.zr.zpc.core.codec</h4>
 * <p></p>
 *
 * @author : zhouning
 * @since : 2021/07/15
 */
public class NettyDecoder extends ByteToMessageDecoder {

    private Class<?> genericClass;

    public NettyDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

    }
}
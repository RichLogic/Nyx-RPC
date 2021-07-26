package com.zr.zrrpc.client.codec;

import com.zr.zpc.core.model.RpcInvoker;
import com.zr.zpc.core.util.HessianUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * <h3>zrrpc.client</h3>
 * <h4>com.zr.zpc.core.codec</h4>
 * <p></p>
 *
 * @author : zhouning
 * @since : 2021/07/15
 */
public class ProtocolDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) {
        RpcInvoker invoker = HessianUtil.deserialize(byteBuf.array());
        out.add(invoker);
    }

}
package com.zr.zrrpc.client.codec;

import com.zr.zpc.core.model.RpcInvoker;
import com.zr.zpc.core.model.RpcResult;
import com.zr.zpc.core.util.HessianUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * <h3>ZR-RPC</h3>
 * <h4>com.zr.zrrpc.client.codec</h4>
 * <p></p>
 *
 * @author : richlogic
 * @since : 2021/07/26
 */
public class ProtocolEncoder extends MessageToMessageEncoder<RpcResult> {

    @Override
    protected void encode(ChannelHandlerContext context, RpcResult rpcResult, List<Object> out) {
        ByteBuf buf = context.alloc().buffer();
        buf.writeBytes(HessianUtil.serialize(rpcResult));
        out.add(buf);
    }

}

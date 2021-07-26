package com.zr.zrrpc.client.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * <h3>ZR-RPC</h3>
 * <h4>com.zr.zrrpc.client.codec</h4>
 * <p></p>
 *
 * @author : richlogic
 * @since : 2021/07/26
 */
public class FrameDecoder extends LengthFieldBasedFrameDecoder {

    public FrameDecoder() {
        super(Integer.MAX_VALUE, 0, 4, 0, 4);
    }

}

package com.zr.zrrpc.client.codec;

import io.netty.handler.codec.LengthFieldPrepender;

/**
 * <h3>zrrpc.client</h3>
 * <h4>com.zr.zpc.core.codec</h4>
 * <p></p>
 *
 * @author : zhouning
 * @since : 2021/07/15
 */
public class FrameEncoder extends LengthFieldPrepender {

    public FrameEncoder() {
        super(4);
    }

}
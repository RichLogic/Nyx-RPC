package com.zr.zrrpc.client.client;

import com.zr.zpc.core.model.RpcConnectParams;
import com.zr.zpc.core.model.RpcInvoker;
import com.zr.zrrpc.client.codec.FrameDecoder;
import com.zr.zrrpc.client.codec.FrameEncoder;
import com.zr.zrrpc.client.codec.ProtocolDecoder;
import com.zr.zrrpc.client.codec.ProtocolEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * <h3>zrrpc.client</h3>
 * <h4>com.zr.zrrpc.client.service</h4>
 * <p></p>
 *
 * @author : zhouning
 * @since : 2021/06/16
 */
public class ZRRpcClient {

    private static ConcurrentMap<String, Channel> serviceNameChannel;
    private static Bootstrap client = new Bootstrap();
    private static EventLoopGroup group = new NioEventLoopGroup();
    private Channel channel;

    public void init() {
        client.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new FrameDecoder())
                                .addLast(new FrameEncoder())
                                .addLast(new ProtocolDecoder())
                                .addLast(new ProtocolEncoder());
                    }
                })
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
//        this.channel = client.connect(connectParams.getHost(), connectParams.getPort()).sync().channel();
//        serviceNameChannel.put(connectParams.getServiceName(), this.channel);
    }

    public static void connect(RpcConnectParams connectParams) {
        client.connect(connectParams.getHost(), connectParams.getPort()).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    future.channel().eventLoop().schedule(() -> connect(connectParams), 5, TimeUnit.SECONDS);
                } else {
                    serviceNameChannel.put(connectParams.getServiceName(), future.channel());
                }
            }
        });
    }

    public void send(RpcInvoker invoker) throws Exception {
        Channel channel = serviceNameChannel.get(invoker.getServerName());
        if (Objects.nonNull(channel)){
            channel.writeAndFlush(invoker).sync();
        }
        throw new RuntimeException(invoker.getServerName() + "not registrar");
    }
}
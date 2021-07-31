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
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
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

    private static ConcurrentMap<String, List<Channel>> serviceNameChannel;
    private static final Bootstrap client = new Bootstrap();
    private static final EventLoopGroup group = new NioEventLoopGroup();
    private final Random random = new Random();

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
                    List<Channel> channelList = serviceNameChannel.get(connectParams.getServiceName());
                    if (ObjectUtils.isEmpty(channelList)){
                        serviceNameChannel.put(connectParams.getServiceName(), new ArrayList<Channel>(){{add(future.channel());}});
                    }else {
                        channelList.add(future.channel());
                        serviceNameChannel.put(connectParams.getServiceName(), channelList);
                    }
                }
            }
        });
    }

    public void send(RpcInvoker invoker) throws Exception {
        Channel channel = getServerChannel(invoker.getServerName());
        channel.writeAndFlush(invoker).sync();
    }

    public Channel getServerChannel(String serverName) {
        List<Channel> channelList = serviceNameChannel.get(serverName);
        if (ObjectUtils.isEmpty(channelList)) {
            throw new RuntimeException(serverName + " 无可用执行实例");
        }

        return channelList.get(this.random.nextInt(channelList.size()));
    }
}
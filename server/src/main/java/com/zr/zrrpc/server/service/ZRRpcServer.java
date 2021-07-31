package com.zr.zrrpc.server.service;


import com.zr.zpc.core.codec.NettyDecoder;
import com.zr.zpc.core.codec.NettyEncoder;
import com.zr.zpc.core.model.RpcConnectParams;
import com.zr.zpc.core.model.RpcInvoker;
import com.zr.zpc.core.model.RpcResult;
import com.zr.zrrpc.server.codec.FrameDecoder;
import com.zr.zrrpc.server.codec.FrameEncoder;
import com.zr.zrrpc.server.codec.ProtocolDecoder;
import com.zr.zrrpc.server.codec.ProtocolEncoder;
import com.zr.zrrpc.server.handler.NettyServerHandler;
import com.zr.zrrpc.server.register.InvokeService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <h3>zrrpc.client</h3>
 * <h4>com.zr.zrrpc.client.service</h4>
 * <p></p>
 *
 * @author : zhouning
 * @since : 2021/06/16
 */
@Service
public class ZRRpcServer {
    protected static final Logger logger = LoggerFactory.getLogger(ZRRpcServer.class);

    private int port = 10500;
    private int workThreadNum = 10;

    public void start(final RpcConnectParams rpcConnectParams) {
        ServerBootstrap bootstrap = new ServerBootstrap();

        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("zrRpc_boss_" + port, true));
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(workThreadNum, new DefaultThreadFactory("zrRpc_work_" + port, true));
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast("frameDecoder", new FrameDecoder())
                                .addLast("frameEncoder", new FrameEncoder())
                                .addLast("protocolDecoder", new ProtocolDecoder())
                                .addLast("protocolEncoder", new ProtocolEncoder())
                                .addLast("handler", new NettyServerHandler(rpcConnectParams));
                    }
                }).option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);

        try {
            ChannelFuture future = bootstrap.bind(port).sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

        }

    }
}















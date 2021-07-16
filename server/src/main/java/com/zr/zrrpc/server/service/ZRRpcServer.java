package com.zr.zrrpc.server.service;


import com.zr.zrrpc.server.handler.HttpHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.springframework.stereotype.Service;

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

    private int port = 10500;
    private int workThreadNum = 10;

    public void start() {
        ServerBootstrap bootstrap = new ServerBootstrap();

        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("zrRpc_boss_" + port, true));
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(workThreadNum, new DefaultThreadFactory("zrRpc_work_" + port, true));
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast("decoder", new HttpRequestDecoder())
                                .addLast("encoder", new HttpRequestEncoder())
                                .addLast("handler", new HttpHandler());
                    }
                }).option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);

        try {
            bootstrap.bind(port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
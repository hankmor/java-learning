package com.belonk.netty.hello;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by sun on 2017/4/16.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class NettyServer {
	//~ Static fields/initializers =====================================================================================


	//~ Instance fields ================================================================================================
	private int port;

	//~ Constructors ===================================================================================================
	public NettyServer(int port) {
		this.port = port;
	}

	//~ Methods ========================================================================================================
	public void run() throws InterruptedException {
		EventLoopGroup boosGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(boosGroup, workGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new SimpleHandler());
					}
				})
				.option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true);

		ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
		channelFuture.channel().closeFuture().sync();
		workGroup.shutdownGracefully();
		boosGroup.shutdownGracefully();
	}

	public static void main(String[] args) throws InterruptedException {
		new NettyServer(8080).run();
	}
}
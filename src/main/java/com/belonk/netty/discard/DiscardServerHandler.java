package com.belonk.netty.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 事件处理器。
 * <p>
 * ChannelInboundHandlerAdapter：处理器适配器
 * Created by sun on 2017/10/21.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {
	//~ Static fields/initializers =====================================================================================


	//~ Instance fields ================================================================================================


	//~ Constructors ===================================================================================================


	//~ Methods ========================================================================================================

	/**
	 * 收到消息时调用，例子中收到的消息是ByteBuf类型。
	 *
	 * @param ctx
	 * @param msg
	 * @throws Exception
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// 为了实现DISCARD协议，处理程序必须忽略接收到的消息。ByteBuf是一个引用计数对象，必须通过release()方法显式释放。
		((ByteBuf) msg).release();
	}

	/**
	 * I/O异常或事件处理异常抛出时调用。
	 * <p>
	 * 在大多数情况下，捕获的异常应该被记录下来，它的相关通道应该在这里关闭，尽管这个方法的实现可能会不同，
	 * 这取决于您想要处理的异常情况。例如，您可能希望在关闭连接之前发送带有错误代码的响应消息。
	 *
	 * @param ctx
	 * @param cause
	 * @throws Exception
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}

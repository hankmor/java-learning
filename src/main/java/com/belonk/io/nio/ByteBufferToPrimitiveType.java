package com.belonk.io.nio;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.ShortBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Created by sun on 2021/12/12.
 *
 * @author sunfuchang03@126.com
 * @since 1.0
 */
public class ByteBufferToPrimitiveType {
	//~ Static fields/constants/initializer


	//~ Instance fields


	//~ Constructors


	//~ Methods

	/*
	 * 缓冲区Buffer
	 *
	 * Buffer称为缓冲区。除了内容之外，缓冲区中包含几个参数：
	 * 1、位置(position)：缓冲区的位置是要读取或写入的下一个元素的索引。 缓冲区的位置永远不会为负，也永远不会大于其限制。
	 * 2、限制(limit)：缓冲区的限制是不应读取或写入的第一个元素的索引。 缓冲区的限制永远不会为负，也永远不会大于其容量。
	 * 3、容量(capacity)：缓冲区中包含的元素个数，它永远不为负数且不会改变
	 * 4、标记(mark)：
	 *
	 * 缓冲区每个子类都定义了两类get和put操作：
	 * 1、相对操作从当前位置开始读取或写入一个或多个元素，然后将位置增加传输的元素数量。 如果请求的传输超过
	 * 限制，则相对获取操作会抛出BufferUnderflowException而相对放置操作会抛出BufferOverflowException ； 在任何一种情况下，都不会传输任
	 * 何数据。
	 * 2、绝对操作采用显式元素索引并且不影响位置。 如果索引参数超出限制，绝对获取和放置操作会抛出IndexOutOfBoundsException。
	 *
	 * 标记和重置
	 *
	 * 缓冲区的标记是调用reset方法时其位置将重置到的索引。标记并不总是被定义，但当它被定义时，它永远不会是负的，也永远不会大于位置。如果定义了
	 * 标记，则在将位置或限制调整为小于标记的值时将丢弃该标记。如果未定义标记，则调用reset方法会导致抛出InvalidMarkException。
	 *
	 * 以下不变量适用于标记、位置、限制和容量值：
	 * 0 <=标记<=位置<=限制<=容量
	 * 新创建的缓冲区始终具有零位置和未定义的标记。初始限制可能为零，也可能是某个其他值，具体取决于缓冲区的类型及其构造方式。新分配的缓冲区的每个
	 * 元素都初始化为零。
	 *
	 * 清除、翻转和倒带
	 *
	 * 除了用于访问位置、限制和容量值以及用于标记和重置的方法之外，该类还定义了对缓冲区的以下操作：
	 * 1、clear：使缓冲区为新的通道读取或相对放置操作序列做好准备：它将位置设置为零，将限制设置为容量大小，并丢弃标记。
	 * 2、flip：使缓冲区为新的通道写入或相对获取操作序列做好准备：将限制设置为当前位置，然后将位置设置为零。如果定义了标记，则将其丢弃。
	 * 3、rewind：使缓冲区准备好重新读取它已经包含的数据：位置设置为零，标记被丢弃。
	 *
	 * 只读缓冲区
	 *
	 * 每个缓冲区都是可读的，但并非每个缓冲区都是可写的。 每个缓冲区类的变异方法被指定为可选操作，当在只读缓冲区上调用时将抛出
	 * ReadOnlyBufferException 。 只读缓冲区不允许更改其内容，但其标记、位置和限制值是可变的。 缓冲区是否为只读可以通过调用它的isReadOnly
	 * 方法来确定。
	 *
	 * 线程安全
	 *
	 * 多个并发线程使用缓冲区是不安全的。 如果一个缓冲区被多个线程使用，那么对缓冲区的访问应该有适当的同步控制。
	 *
	 * 链式调用
	 *
	 * 此类中没有返回值的方法被指定为返回调用它们的缓冲区。这允许链式方法调用；
	 * 例如，语句序列
	 * b.flip();
	 * b.position(23);
	 * b.limit(42);
	 * 可以写为：b.flip().position(23).limit(42);
	 *
	 * ByteBuffer
	 *
	 * ByteBuffer继承自Buffer。
	 * ByteBuffer提供了asCharBuffer()、asShortBuffer()、asIntBuffer()、asFloatBuffer()、asLongBuffer()、asDoubleBuffer()等方法，
	 * 他们都返回一个新的缓冲区视图，更改其内容会反映到ByteBuffer中，但是新的缓冲区视图的位置(position)、限制(limit)、标记(mark)都是独立的，
	 * 容量(capacity)与原ByteBuffer相同。
	 */

	public static void main(String[] args) {
		int size = 1024;
		ByteBuffer byteBuffer = ByteBuffer.allocate(size);
		// 检查是否默认的字节都是0
		while (byteBuffer.hasRemaining()) {
			if (byteBuffer.get() != 0) {
				System.out.println("none zero");
			}
		}
		System.out.println("limit: " + byteBuffer.limit());

		// 操作CharBuffer
		byteBuffer.rewind();
		CharBuffer charBuffer = byteBuffer.asCharBuffer().put("Hello Java");
		// 打印charBuffer
		charBuffer.flip();
		System.out.println("CharBuffer: " + charBuffer);
		// 打印byteBuffer
		System.out.print("ByteBuffer: ");
		while (byteBuffer.hasRemaining()) {
			char c = byteBuffer.getChar();
			if (c != 0) {
				System.out.print(c);
			} else {
				break;
			}
		}
		System.out.println();

		// 操作ShortBuffer
		byteBuffer.rewind();
		ShortBuffer shortBuffer = byteBuffer.asShortBuffer().put((short) 471142); // 转型后长度截取造成结果不同
		shortBuffer.flip();
		System.out.println("ShortBuffer: " + shortBuffer.get());
		System.out.println("ByteBuffer: " + byteBuffer.getShort());

		// 操作IntBuffer
		byteBuffer.rewind();

	}
}
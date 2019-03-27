import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLOutput;

/**
 * 一、缓冲区（Buffer）：在Java NIO 中负责数据 的存取，缓冲区就是数组。用于储存不同类型的数据
 *
 * 根据数据类型的不同（boolean 除外），提供了相应类型的缓冲区
 * ByteBuffer,charBuffer,ShortBuffer,IntBuffer,LongBuffer,FloatBuffer, DoubleBuffer
 * 上述缓冲区的管理方式基本一致，通过allocate（）来获取缓冲区
 *
 * 二、缓冲区的两个核心的方法
 * put()：存入数据到缓冲区中
 * get()：获取缓冲区的数据
 *
 *三、 缓冲区的四个核心属性:
 * capacity:容量，表示缓冲区的最大储存数据的容量
 * limit:界限，表示缓冲区中可以操作数据的大小（limit 后数据不能进行读取）
 * position：位置，表示缓冲区正在进行操作数据的位置
 * position<=limit<=capacity
 */

public class TestBuffer {
    @Test
    public void test1(){
        String str = "abce";
        //分配一个指定大小的缓冲区
        ByteBuffer buf=ByteBuffer.allocate(1024);
        System.out.println("--------------allocate()--------------");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        //利用put()存入数据到缓冲区中
        buf.put(str.getBytes());
        System.out.println("--------------put()--------------");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        //转换到读模式
        buf.flip();
        System.out.println("--------------filp()--------------");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());
    }
@Test//通道之间的直接物理层面的传输
   public void test2() throws IOException {
    FileChannel open = FileChannel.open(Paths.get("d:/1.mkv"), StandardOpenOption.READ);
    FileChannel in = FileChannel.open(Paths.get("d:/2.mkv"), StandardOpenOption.READ, StandardOpenOption.WRITE);

    in.transferTo(0,in.size(),open);
    in.close();
    open.close();
}
@Test//分散读取
public void test3() throws Exception {
    RandomAccessFile file = new RandomAccessFile("E:\\1.txt","rw");
    //获取通道
    FileChannel channel = file.getChannel();


    //获取缓冲区
    ByteBuffer allocate = ByteBuffer.allocate(100);
    ByteBuffer allocate1 = ByteBuffer.allocate(1024);

    //分散读取
   ByteBuffer[] buf= {allocate,allocate1};
    channel.read(buf);
    for (ByteBuffer byteBuffer : buf) {
        //切换成读模式
        byteBuffer.flip();
    }
    System.out.println(new String(buf[0].array(),0,buf[0].limit()));
    //合并写入
    RandomAccessFile file1 = new RandomAccessFile("E:\\2.txt", "rw");
    FileChannel channel1 = file1.getChannel();
    channel1.write(buf);
    channel1.close();
    channel.close();
}
//字符的编码和解码
    @Test
    public void test4() throws CharacterCodingException {
        Charset gbk = Charset.forName("GBK");
        //获取编码器
        CharsetEncoder ce = gbk.newEncoder();
        //获取解码器
        CharsetDecoder cd = gbk.newDecoder();

        CharBuffer cbf = CharBuffer.allocate(1024);
        cbf.put("少年当自强");
        cbf.flip();
        //编码
        ByteBuffer bbf = ce.encode(cbf);
        //解码
        CharBuffer cbf2 = cd.decode(bbf);
        System.out.println(cbf2);


    }
}





























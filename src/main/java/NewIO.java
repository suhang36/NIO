import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.Scanner;

public class NewIO {
    //客户端
    @Test
    public void client() throws IOException {
        //获取通道
        SocketChannel schannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",9898));
        //切换到非阻塞模式
        schannel.configureBlocking(false);
        //分配缓存区
        ByteBuffer buf = ByteBuffer.allocate(1024);
        //发送数据到服务端
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNext()){
            String str=scanner.next();
            buf.put((LocalTime.now().toString()+"\n"+str).getBytes());
            buf.flip();
            schannel.write(buf);
            buf.clear();
        }

        //关闭通道
        schannel.close();
    }
    @Test
    public void service() throws IOException {
        //获取通道
        ServerSocketChannel ssChannel = ServerSocketChannel.open();

        //切换到非阻塞模式
        ssChannel.configureBlocking(false);
        //绑定端口
        ssChannel.bind(new InetSocketAddress(9898));
        //获取监听器
        Selector selector = Selector.open();
        //将监听器注册到通道中
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环的监听监听器完成的事件
        while (selector.select()> 0){
            //得到监听选择器的所有注册的“选择键”
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                //获取准备就绪的事件
                SelectionKey sk = iterator.next();
                if (sk.isAcceptable()){
                    //如果接收就绪，就创建与服务器的连接
                    SocketChannel sChannel = ssChannel.accept();
                    //切换到非阻塞模式
                    sChannel.configureBlocking(false);
                    //将该通道注册到选择器上
                    sChannel.register(selector,SelectionKey.OP_READ);
                }else if (sk.isReadable()){
                    //获得当前选择器就绪的通道
                    SocketChannel sChannel = (SocketChannel) sk.channel();
                    //读取数据
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    int let;
                    while ((let=sChannel.read(buf))>0){
                        buf.flip();
                        System.out.println(new String(buf.array(),0,let));
                        buf.clear();
                    }
                }

            }
            iterator.remove();
        }

    }
}

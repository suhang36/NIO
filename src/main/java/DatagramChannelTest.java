import jdk.nashorn.internal.objects.annotations.Where;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Scanner;

public class DatagramChannelTest {

    @Test
    public void test1() throws IOException {
        DatagramChannel channel = DatagramChannel.open();
        channel.configureBlocking(false);
        ByteBuffer buf = ByteBuffer.allocate(1024);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String str=scanner.next();
            buf.put(str.getBytes());
            buf.flip();
            channel.send(buf, new InetSocketAddress("127.0.0.1",9898));
            buf.clear();
        }
    }

    @Test
    public void test2() throws IOException {
        DatagramChannel sChannel = DatagramChannel.open();
        sChannel.configureBlocking(false);
        sChannel.bind(new InetSocketAddress(8989));
        Selector selector = Selector.open();
        sChannel.register(selector, SelectionKey.OP_READ);
        while (selector.select()>0){
            for (SelectionKey it : selector.selectedKeys()) {
                if (it.isReadable()) {
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    //读
                    sChannel.receive(buf);
                    buf.flip();
                    System.out.println(new String(buf.array(), 0, buf.limit()));
                }
            }
        }
    }
}

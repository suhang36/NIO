import org.junit.Test;

import javax.naming.NamingEnumeration;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

//管道：单向传输，用于单向的线程传输数据
public class PipeTest {
    @Test
    public void test1() throws IOException {
        Pipe pipe = Pipe.open();
        //向管道中读取数据
        Pipe.SinkChannel sink = pipe.sink();
        ByteBuffer buf = ByteBuffer.allocate(1024);
        buf.put("向管道中写入数据".getBytes());
        buf.flip();
        sink.write(buf);
        buf.clear();
        //在管道中读取数据
        Pipe.SourceChannel source = pipe.source();
        int len = source.read(buf);
        System.out.println(new String(buf.array(),0,len));
        source.close();
        sink.close();


    }

}

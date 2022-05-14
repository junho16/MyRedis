import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Scanner;

/**
 * @author Junho
 * @date 2022/5/14 19:10
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Scanner sc = new Scanner(System.in);
        while (true){
            String s = sc.nextLine();
            System.out.println("client " + ctx +" "+ s);
            ctx.writeAndFlush(Unpooled.copiedBuffer(s , CharsetUtil.UTF_8));
        }
//        System.out.println("client " + ctx);
//        ctx.writeAndFlush(Unpooled.copiedBuffer("hello, server: (>^ω^<)喵", CharsetUtil.UTF_8));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
//        System.out.println("服务器回复的消息:" + buf.toString(CharsetUtil.UTF_8));
//        System.out.println("服务器的地址： "+ ctx.channel().remoteAddress());
        System.out.println("Server ip: " + ctx.channel().remoteAddress() +
                "Info: " + buf.toString(CharsetUtil.UTF_8));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception { cause.printStackTrace();
        ctx.close();
    }


}

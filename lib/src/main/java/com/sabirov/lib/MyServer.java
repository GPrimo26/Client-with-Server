package com.sabirov.lib;

import com.google.protobuf.util.TimeUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import io.grpc.netty.NettyServerBuilder;
import io.netty.channel.ChannelOption;

public class MyServer {
    private final Logger logger = Logger.getLogger(MyServer.class.getName());

    private void start() throws IOException, InterruptedException {
        int port = 5067;
        SocketAddress socketAddress = new InetSocketAddress("0.0.0.0", port);
        NettyServerBuilder server = NettyServerBuilder.forAddress(socketAddress)
                .addService(new MyService());
        server.build().start().awaitTermination();
        logger.info("Server started, listening on " + port);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final MyServer server = new MyServer();
        server.start();
    }
}

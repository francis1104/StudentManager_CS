package com.itheima;

import com.itheima.service.StudentService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ServerApplication {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(10000);

        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                5,
                10,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()

        );

        while (true) {
            Socket socket = ss.accept();
            pool.submit(new StudentService(socket));
        }
    }
}

package ru.erus.nettr.net;

import ru.erus.nettr.Data.DataBundle;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Reciever {

    private int                  port = 0;
    private OnRecievingCompleted onRecievingCompleted;


    public Reciever(int port, OnRecievingCompleted onRecievingCompleted) {
        this.port = port;
        this.onRecievingCompleted = onRecievingCompleted;
    }

    public Reciever(int port) {
        this.port = port;
    }

    public void waitForRecieving() {
        new Worker().start();
    }

    public void setOnRecievingCompleted(OnRecievingCompleted onRecievingCompleted) {
        this.onRecievingCompleted = onRecievingCompleted;
    }

    private class Worker extends Thread {

        @Override
        public void run() {

            DataBundle bundle = new DataBundle();
            int        result = 0;

            try {

                ServerSocket server = new ServerSocket(port);
                Socket       socket = server.accept();
                bundle.loadFromStream(socket.getInputStream());

                if (!socket.isClosed()) {
                    socket.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
                result = -1;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                result = -2;
            }


            if (onRecievingCompleted != null) {
                onRecievingCompleted.onComplete(result, bundle);
            }

        }
    }
}
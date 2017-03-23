package top.leekm.minihttp.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ThreadPoolExecutor;

import top.leekm.minihttp.core.http.HttpHandler;
import top.leekm.minihttp.utils.Helper;

/**
 * Created by lkm on 2017/3/20.
 */
public class MiniHttpServer {

    private ServerSocket serverSocket;
    private Thread serverThread;
    private Config serverConfig;
    private ThreadPoolExecutor executor;
    private volatile Status status = Status.Stop;

    private DynamicManager dynamicManager;

    public MiniHttpServer() {
        dynamicManager = new DynamicManager(this);
    }

    public void start() throws IOException {
        start(new Config());
    }

    public HttpHandler queryHttpHandler(String path) {
        return dynamicManager.queryHttpHandler(path);
    }

    public <T> T queryLocalService(Class<T> clazz) {
        return dynamicManager.queryLocalService(clazz);
    }

    public void registHttpHandler(String path, Class<? extends HttpHandler> clazz) {
        dynamicManager.registHttpHandler(path, clazz);
    }

    public void registLocalService(Class<?> clazz, Object service) {
        dynamicManager.registLocalService(clazz, service);
    }

    public HttpHandler getDefaultHttpHandler() {
        return dynamicManager.getDefaultHttpHandler();
    }

    public synchronized void start(Config config) throws IOException {
        if (status != Status.Stop) {
            throw new IllegalStateException("server is not stop");
        }

        serverConfig = config;

        serverSocket = new ServerSocket(serverConfig.port, serverConfig.backlog);
        serverSocket.setSoTimeout(serverConfig.acceptTimeout);

        executor = MiniHttpExecutor.generateExecutor(config.maxConcurrent);

        serverThread = new Thread(new Run() {
            @Override
            protected void todo() throws Throwable {
                loop();
            }
        }, "mini-server-thread");
        serverThread.start();
        status = Status.Start;

        Log.log("server", "server started");
    }

    public synchronized void stop() {
        if (status != Status.Start) {
            throw new IllegalStateException("server is not start");
        }
        status = Status.Pending;
    }

    private void loop() {
        Log.log("server", "start loop");
        while (status == Status.Start) {
            try {
                Socket socket = serverSocket.accept();
                Log.log("server", "new conn from " + Helper.byteToIp(socket
                        .getInetAddress().getAddress()) + ":" + socket.getPort());
                socket.setSoTimeout(serverConfig.bizTimeout);
                executor.execute(new ConnectionHandler(socket, this));
            } catch (SocketTimeoutException timeout) {
                // ignore
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            serverSocket.close();
        } catch (IOException e) {
            Log.exception("core", e);
        }
        executor.shutdown();
        dynamicManager.clear();

        status = Status.Stop;
        Log.log("server", "final stop");
    }

    public static class Config {
        public int port = 8088;
        public int acceptTimeout = 2000;
        public int bizTimeout = 2000;
        public int backlog = 64;
        public int maxConcurrent = 64;
        public String ROOT = "";
    }

    private enum Status {
        Stop, Start, Pending
    }
}

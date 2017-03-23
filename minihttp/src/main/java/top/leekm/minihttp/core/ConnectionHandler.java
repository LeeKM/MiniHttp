package top.leekm.minihttp.core;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

import top.leekm.minihttp.core.http.HttpHandler;
import top.leekm.minihttp.core.http.HttpProcessor;
import top.leekm.minihttp.core.http.HttpRequest;
import top.leekm.minihttp.core.http.HttpResponse;
import top.leekm.minihttp.utils.Helper;

/**
 * Created by lkm on 2017/3/20.
 */
public class ConnectionHandler extends RejactableRun {

    private MiniHttpServer server;
    private Socket socket;
    private boolean keepAlive = false;
    private HttpRequest request;
    private HttpProcessor processor;

    public ConnectionHandler(Socket socket, MiniHttpServer server) {
        this.socket = socket;
        this.server = server;
        this.request = new HttpRequest();
        this.processor = new HttpProcessor(request, socket);
    }

    @Override
    protected void todo() throws Throwable {

        Status status = Status.Head;

        exit:
        while (status != Status.Handled) {
            switch (status) {
                case Head:
                    status = processor.processHeaderLine() ? Status.Property : Status.BadRequest;
                    break;
                case Property:
                    if (processor.processProperties()) {
                        keepAlive = "keep-alive".equalsIgnoreCase(request.getProperty("Connection"));
                        status = Status.Param;
                    } else {
                        status = Status.BadRequest;
                    }
                    break;
                case Param:
                    status = processor.processParams() ? Status.GoodRequest : Status.BadRequest;
                    break;
                case GoodRequest:
                    handleHttpRequest(request);
                    if (keepAlive) {
                        status = Status.Head;
                        continue;
                    }
                    break exit;
                case BadRequest:
                    break exit;
            }
        }

        if (status == Status.BadRequest) {
            responseBadRequest();
        }

        shutdownRemote();
    }

    private void responseBadRequest() throws IOException {
        writeHttpResponse(HttpResponse.getBadRequestResponse());
    }

    private void handleHttpRequest(HttpRequest request) throws IOException {
        HttpResponse response = new HttpResponse();
        HttpHandler handler = server.queryHttpHandler(request.path);
        if (null == handler) {
            handler = server.getDefaultHttpHandler();
        }
        if ("GET".equals(request.method)) {
            handler.doGet(request, response);
        } else if ("POST".equals(request.method)) {
            handler.doPost(request, response);
        }
        writeHttpResponse(response);
    }

    private void writeHttpResponse(HttpResponse response) throws IOException {
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        writer.print(response.getHeader() + "\r\n");
        Map<String, String> properties = response.getProperties(keepAlive);
        for (String name : properties.keySet()) {
            String value = properties.get(name);
            writer.print(name + ": " + value + "\r\n");
        }
        writer.print("\r\n");
        writer.flush();
        socket.getOutputStream().write(response.getResponseBody());
        socket.getOutputStream().flush();
    }

    @Override
    protected void onFailed(Throwable ex) {
        if (ex instanceof IOException) {
            shutdownRemote();
        } else {
            super.onFailed(ex);
        }
    }

    private void shutdownRemote() {
        try {
            Log.log("server", "shutDown remote " + Helper.byteToIp(socket
                    .getInetAddress().getAddress()) + ":" + socket.getPort());
            socket.close();
        } catch (Throwable e) {
        }
    }

    private enum Status {
        Head, Property, Param, GoodRequest, BadRequest, Handled
    }
}

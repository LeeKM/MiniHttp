package top.leekm.minihttpserver;

import java.io.PrintWriter;
import java.util.Map;

import top.leekm.minihttp.core.http.HttpHandler;
import top.leekm.minihttp.core.http.HttpRequest;
import top.leekm.minihttp.core.http.HttpResponse;

/**
 * Created by lkm on 2017/3/23.
 */
public class EchoHttpHandler extends HttpHandler {

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        PrintWriter writer = response.getWriter();
        writer.write(request.method + " " + request.path + " " + request.httpVersion + "\r\n");
        writer.write(mapToString(request.getParams()));
        writer.write(mapToString(request.getProperties()));
    }

    private String mapToString(Map<String, String> values) {
        StringBuffer buffer = new StringBuffer();
        for (String key : values.keySet()) {
            buffer.append(key + ":" + values.get(key) + ";\n");
        }
        if (buffer.length() <= 0) {
            buffer.append("[Empty]");
        }
        buffer.append("\r\n");
        return buffer.toString();
    }
}

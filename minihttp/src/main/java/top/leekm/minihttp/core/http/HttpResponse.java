package top.leekm.minihttp.core.http;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lkm on 2017/3/22.
 */

public class HttpResponse {

    public String httpVersion = "HTTP/1.1";

    public HttpStatusCode statusCode = HttpStatusCode._200;

    private ByteArrayOutputStream responseBody = new ByteArrayOutputStream();

    private Map<String, String> properties = new HashMap<>();

    private PrintWriter writer = new PrintWriter(responseBody);

    public PrintWriter getWriter() {
        return writer;
    }

    public OutputStream getOutputStream() {
        writer.flush();
        return responseBody;
    }

    public String getHeader() {
        return httpVersion + ' ' + statusCode.getStatusCode()
                + ' ' + statusCode.getStatusMessage();
    }

    public Map<String, String> getProperties(boolean keepAlive) {
        writer.flush();
        Map<String, String> property = new HashMap<>(properties);
        property.put("Connection", keepAlive ? "keep-alive" : "close");
        property.put("Content-Length", String.valueOf(responseBody.size()));
        property.put("Cache-Control", "no-cache");
        return property;
    }

    public byte[] getResponseBody() {
        writer.flush();
        return responseBody.toByteArray();
    }

    public static HttpResponse getBadRequestResponse() {
        HttpResponse response = new HttpResponse();
        response.statusCode = HttpStatusCode._400;
        return response;
    }

}

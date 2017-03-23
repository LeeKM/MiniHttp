package top.leekm.minihttp.core.http;

import java.io.IOException;

/**
 * Created by lkm on 2017/3/22.
 */

public class HttpHandler extends HttpContext {

    public void doGet(HttpRequest request,
                      HttpResponse response) throws IOException {
        response.statusCode = HttpStatusCode._501;
    }

    public void doPost(HttpRequest request,
                       HttpResponse response) throws IOException {
        response.statusCode = HttpStatusCode._501;
    }

}

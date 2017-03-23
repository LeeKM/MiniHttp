package top.leekm.minihttpserver;

import java.io.IOException;
import java.io.PrintWriter;

import top.leekm.minihttp.core.http.HttpHandler;
import top.leekm.minihttp.core.http.HttpRequest;
import top.leekm.minihttp.core.http.HttpResponse;

/**
 * Created by lkm on 2017/3/23.
 */

public class ActionHandler extends HttpHandler {

    @Override
    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.println("<html>");
        writer.println("\t<body>");
        writer.println("\t\t<h1>");
        writer.println("\t\t\tHello Mini-Server");
        writer.println("\r\r</h1>");
        writer.println("\t</body>");
        writer.println("</html>");
    }

}

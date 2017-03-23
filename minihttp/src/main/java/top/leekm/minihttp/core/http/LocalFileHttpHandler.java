package top.leekm.minihttp.core.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import top.leekm.minihttp.core.service.FileService;
import top.leekm.minihttp.utils.StreamUtils;

/**
 * Created by lkm on 2017/3/23.
 */
public class LocalFileHttpHandler extends HttpHandler {

    @Override
    public void doGet(HttpRequest request, HttpResponse response) throws IOException {
        FileService fileService = queryLocalService(FileService.class);
        if (null != fileService && fileService.isFileExist(request.path)) {
            InputStream inputStream = fileService.openReadOnlyFile(request.path);
            OutputStream outputStream = response.getOutputStream();
            StreamUtils.transfer(inputStream, outputStream);
            inputStream.close();
        } else {
            doPost(request, response);
        }
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) throws IOException {
        response.statusCode = HttpStatusCode._404;
    }
}

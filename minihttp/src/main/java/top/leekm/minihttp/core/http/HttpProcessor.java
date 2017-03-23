package top.leekm.minihttp.core.http;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import top.leekm.minihttp.utils.TextUtils;

/**
 * Created by lkm on 2017/3/22.
 */
public class HttpProcessor {

    private final static int MAX_HEADER_SIZE = 4 * 4096;
    private HttpRequest request;
    private Socket socket;

    public HttpProcessor(HttpRequest request, Socket socket) {
        this.request = request;
        this.socket = socket;
    }

    public boolean processHeaderLine() throws IOException {
        Status status = Status.Method;
        InputStream inputStream = socket.getInputStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        String name = null;
        while (status != Status.CTRL && buffer.size() <= MAX_HEADER_SIZE) {
            int value = inputStream.read();
            if (value == -1)
                throw new EOFException();

            switch (status) {
                case Method:
                    if (' ' == value) {
                        request.method = buffer.toString();
                        status = Status.Path;
                        buffer.reset();
                        continue;
                    }
                    break;
                case Path:
                    if (' ' == value || '?' == value) {
                        request.path = buffer.toString();
                        status = ' ' == value ? Status.Version : Status.Name;
                        buffer.reset();
                        continue;
                    }
                    break;
                case Name:
                    if ('&' == value || ' ' == value) {
                        return false;
                    } else if ('=' == value) {
                        name = TextUtils.urlDecode(buffer.toString());
                        if (TextUtils.isEmpty(name)) {
                            return false;
                        }
                        status = Status.Value;
                        buffer.reset();
                        continue;
                    }
                    break;
                case Value:
                    if ('&' == value || ' ' == value) {
                        String property = TextUtils.urlDecode(buffer.toString());
                        request.addParam(name, property);
                        status = '&' == value ? Status.Name : Status.Version;
                        buffer.reset();
                        continue;
                    }
                    break;
                case Version:
                    if ('\r' == value) {
                        status = Status.CT;
                        continue;
                    }
                    break;
                case CT:
                    if ('\n' != value) {
                        return false;
                    } else {
                        request.httpVersion = buffer.toString();
                        status = Status.CTRL;
                        continue;
                    }
            }
            buffer.write(value);
        }
        return true;
    }

    public boolean processProperties() throws IOException {
        Status status = Status.Name;
        InputStream inputStream = socket.getInputStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        String name = null;
        while (status != Status.CTRLCTRL && buffer.size() <= MAX_HEADER_SIZE) {
            int value = inputStream.read();
            switch (status) {
                case Name:
                    if (':' == value) {
                        name = buffer.toString();
                        status = Status.CAL;
                        buffer.reset();
                        continue;
                    }
                    break;
                case CAL:
                    if (' ' != value) {
                        return false;
                    } else {
                        status = Status.SPC;
                        continue;
                    }
                case SPC:
                    if ('\r' == value) {
                        String property = buffer.toString();
                        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(property)) {
                            return false;
                        }
                        request.addProperty(name, property);
                        status = Status.CT;
                        buffer.reset();
                        continue;
                    }
                    break;
                case CT:
                    if ('\n' != value) {
                        return false;
                    } else {
                        status = Status.CTRL;
                        continue;
                    }
                case CTRL:
                    if ('\r' == value) {
                        status = Status.CTRLCT;
                        continue;
                    } else {
                        status = Status.Name;
                    }
                    break;
                case CTRLCT:
                    if ('\n' != value) {
                        return false;
                    } else {
                        status = Status.CTRLCTRL;
                        continue;
                    }
            }
            buffer.write(value);
        }
        return true;
    }

    public boolean processParams() {

        return true;
    }

    private enum Status {
        Method, Path, Version, Name, Value, CAL, SPC, CT, CTRL, CTRLCT, CTRLCTRL
    }

}

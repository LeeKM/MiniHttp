package top.leekm.minihttp.core.http;

/**
 * Created by lkm on 2017/3/23.
 */

public enum HttpStatusCode {

    _200(200, "OK"),

    _400(400, "Bad Request"),

    _404(404, "Not Found"),

    _501(501, "Not Implemented");

    private int statusCode;
    private String statusMessage;

    HttpStatusCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.statusMessage = message;
    }

    public String getStatusMessage() {
        return this.statusMessage;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    @Override
    public String toString() {
        return this.statusCode + " " + this.statusMessage;
    }
}

package top.leekm.minihttp.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by lkm on 2017/3/22.
 */

public class TextUtils {

    public static boolean isEmpty(String string) {
        return null == string || string.length() <= 0;
    }

    public static String urlDecode(String urlString) {
        try {
            return URLDecoder.decode(urlString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}

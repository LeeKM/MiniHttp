package top.leekm.minihttp.android.client;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Iterator;
import java.util.LinkedList;

import top.leekm.minihttp.core.Run;

/**
 * Created by lkm on 2017/3/20.
 */
public class Console extends TextView {

    private final static int BLACK_BG = 0xFF000000;

    private final static int GREEN_TEXT = 0xFF00FF00;

    private int bufferSize = 128;
    private LinkedList<String> buffer = new LinkedList<>();

    public Console(Context context) {
        this(context, null);
    }

    public Console(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTextColor(GREEN_TEXT);
        setBackgroundColor(BLACK_BG);
        setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    public void println(final String text) {
        handler.post(new Run() {
            @Override
            protected void todo() throws Throwable {
                while (buffer.size() >= bufferSize) {
                    buffer.removeFirst();
                }
                buffer.addLast(text);
                transport();
            }
        });
    }

    public void clear() {
        handler.post(new Run() {
            @Override
            protected void todo() throws Throwable {
                buffer.clear();
                transport();
            }
        });
    }

    private void transport() {
        StringBuffer stringBuffer = new StringBuffer();
        Iterator<String> iterator = buffer.iterator();
        while (iterator.hasNext()) {
            stringBuffer.append("~$ ");
            stringBuffer.append(iterator.next());
            stringBuffer.append('\n');
        }
        setText(stringBuffer);
    }

    private Handler handler = new Handler(Looper.getMainLooper());
}

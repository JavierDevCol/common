package common.http.util;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;

public class ServletOutputStreamEx extends ServletOutputStream {
    StringBuilder stringBuilder;

    public ServletOutputStreamEx() {
        this.stringBuilder = new StringBuilder();
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {

    }

    @Override
    public void write(int b) throws IOException {
    }

    @Override
    public void write(byte b[], int off, int len) throws IOException {
        stringBuilder.append(new String(b, "UTF-8"));
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }
}

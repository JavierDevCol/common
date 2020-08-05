package common.config;

import common.http.util.ServletOutputStreamEx;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class HttpResponseBody extends HttpServletResponseWrapper {
    ServletOutputStream outputStream;

    public HttpResponseBody(HttpServletResponse response) throws IOException {
        super(response);
        outputStream = new ServletOutputStreamEx();
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"));
    }

}

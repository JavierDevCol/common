package common.config;

import common.http.util.HttpRequestContextHolder;
import common.types.ClienteDomainBean;
import common.util.UtilJson;
import common.util.UtilString;
import lombok.extern.log4j.Log4j2;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

@Log4j2
public class HttpRquestBody extends HttpServletRequestWrapper {

    private final String body;

    public HttpRquestBody(HttpServletRequest request) throws IOException {
        super(request);
        String responseBody = UtilString.getString(request.getInputStream());
        if (HttpRequestContextHolder.getCliente() != null) {
            String bodyProv = new String(responseBody);
            try {
                Map<String, String> map = UtilJson.toObject(bodyProv, Map.class);
                map.put(ClienteDomainBean.Attributes.CLIENTE_ID, HttpRequestContextHolder.getCliente());
                responseBody = UtilJson.toString(map);
            }
            catch (Exception e) {

            }
        }
        body = responseBody;
    }


    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }

            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
        return servletInputStream;
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }
}

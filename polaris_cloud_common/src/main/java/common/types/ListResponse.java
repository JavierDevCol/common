package common.types;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ListResponse<T> extends BaseResponse {
    private long count;
    private List<T> data;

    public ListResponse() {
    }

    public ListResponse(String message) {
        super(false, HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public ListResponse(HttpStatus status, String message) {
        super(false, status.value(), message);
    }

    public ListResponse(int statusCode, String message) {
        super(false, statusCode, message);
    }

    public ListResponse(List<T> data) {
        this("Success", data);
    }

    public ListResponse(List<T> data, long count) {
        this("Success", data);
        this.count = count;
    }

    public ListResponse(String message, List<T> data) {
        super(true, HttpStatus.OK, message);
        this.data = data;
        this.count = data == null ? 0L : (long) data.size();
    }

    public List<T> getData() {
        return this.data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public long getCount() {
        return this.count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}

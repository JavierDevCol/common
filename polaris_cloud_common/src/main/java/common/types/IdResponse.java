package common.types;


import org.springframework.http.HttpStatus;

public class IdResponse extends ObjectResponse<Long> {
    public IdResponse() {
    }

    public IdResponse(Long id) {
        super(HttpStatus.CREATED, HttpStatus.CREATED.getReasonPhrase(), id);
    }
}

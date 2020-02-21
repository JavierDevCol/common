package common.types;

import java.util.List;

public class IdListResponse extends ListResponse<Long> {
    public IdListResponse() {
    }

    public IdListResponse(List<Long> ids) {
        super(ids);
    }

    public IdListResponse(long count, List<Long> ids) {
        super(ids, count);
    }
}

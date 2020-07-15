package common.types;

import java.io.Serializable;

public abstract class Entity<ID extends Serializable> extends IdObject<ID> implements Serializable {

    public Entity() {
    }

    public interface Attributes {
        String ID = "id";
    }
}

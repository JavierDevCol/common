package com.heystyles.common.types;

import java.io.Serializable;

public abstract class Entity<ID extends Serializable> extends IdObject<ID> implements Serializable {
    private static final long serialVersionUID = -7716070975924354714L;

    public Entity() {
    }

    public interface Attributes {
        String ID = "id";
    }
}

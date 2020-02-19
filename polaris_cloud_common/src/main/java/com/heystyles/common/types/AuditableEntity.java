package com.heystyles.common.types;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class AuditableEntity<ID extends Serializable> extends Entity<ID> {
    public AuditableEntity() {
    }

    public abstract LocalDateTime getCreatedDate();

    public abstract void setCreatedDate(LocalDateTime var1);

    public abstract LocalDateTime getUpdatedDate();

    public abstract void setUpdatedDate(LocalDateTime var1);

    public interface Attributes extends com.heystyles.common.types.Entity.Attributes {
        String CREATED_DATE = "createdDate";
        String UPDATED_DATE = "updatedDate";
    }
}

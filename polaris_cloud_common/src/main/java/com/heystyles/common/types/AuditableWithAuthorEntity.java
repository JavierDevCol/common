package com.heystyles.common.types;

import java.io.Serializable;

public abstract class AuditableWithAuthorEntity<ID extends Serializable> extends AuditableEntity<ID> {
    public AuditableWithAuthorEntity() {
    }

    public abstract String getCreatedBy();

    public abstract void setCreatedBy(String var1);

    public abstract String getUpdatedBy();

    public abstract void setUpdatedBy(String var1);

    public interface Attributes extends AuditableEntity.Attributes {
        String CREATED_BY = "createdBy";
        String UPDATED_BY = "updatedBy";
    }
}

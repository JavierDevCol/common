package com.heystyles.common.types;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class IdObject<ID> {
    IdObject() {
    }

    public abstract ID getId();

    public abstract void setId(ID var1);

    public int hashCode() {
        int prime = 1;
        int result = 31 * prime + (this.getId() == null ? 0 : this.getId().hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        else if (obj != null && this.getClass() == obj.getClass()) {
            IdObject<ID> other = (IdObject) obj;
            if (this.getId() == null) {
                return other.getId() == null;
            }
            else {
                return this.getId().equals(other.getId());
            }
        }
        else {
            return false;
        }
    }

    @JsonIgnore
    public boolean isNewObject() {
        return this.getId() == null;
    }

    public interface Attributes {
        String ID = "id";
    }
}

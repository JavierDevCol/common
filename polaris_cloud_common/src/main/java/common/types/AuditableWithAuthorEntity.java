package common.types;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.cassandra.core.mapping.Column;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class AuditableWithAuthorEntity<ID extends Serializable> extends AuditableEntity<ID> {

    @CreatedDate
    @Column("created_date")
    private LocalDateTime createdDate;

    @CreatedBy
    @Column("created_by")
    private String createdBy;

    @LastModifiedDate
    @Column("updated_date")
    private LocalDateTime updatedDate;

    @LastModifiedBy
    @Column("updated_by")
    private String updatedBy;

    public AuditableWithAuthorEntity() {
    }

    @Override
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    @Override
    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public interface Attributes extends AuditableEntity.Attributes {
        String CREATED_BY = "createdBy";
        String UPDATED_BY = "updatedBy";
    }
}

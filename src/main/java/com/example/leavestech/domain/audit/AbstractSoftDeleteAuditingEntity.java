package com.example.leavestech.domain.audit;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;

@MappedSuperclass
public abstract class AbstractSoftDeleteAuditingEntity extends AbstractAuditingEntity {

    @Column(name = "deleted_flag")
    private Boolean deletedFlag = false;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    public Boolean getDeletedFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(final Boolean deletedFlag) {
        this.deletedFlag = deletedFlag;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(final String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(final Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}

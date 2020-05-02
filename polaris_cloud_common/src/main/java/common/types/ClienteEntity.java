package common.types;

import org.springframework.data.cassandra.core.mapping.Column;

import java.io.Serializable;
import java.util.UUID;

public abstract class ClienteEntity<ID extends Serializable> extends AuditableWithAuthorEntity<ID> {

    public interface Attributes extends AuditableWithAuthorEntity.Attributes {
        String CLIENTE_ID = "clienteId";
    }

    @Column("cliente_id")
    private UUID clienteId;

    public ClienteEntity() {
    }

    public UUID getClienteId() {
        return clienteId;
    }

    public void setClienteId(UUID clienteId) {
        this.clienteId = clienteId;
    }
}

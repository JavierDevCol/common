package common.types;

import org.springframework.data.cassandra.core.mapping.Column;

import java.io.Serializable;

public abstract class ClienteEntity<ID extends Serializable> extends AuditableWithAuthorEntity<ID> {

    public interface Attributes extends AuditableWithAuthorEntity.Attributes {
        String CLIENTE_ID = "clienteId";
    }

    @Column("cliente_id")
    private ID clienteId;

    public ClienteEntity() {
    }

    public ID getClienteId() {
        return clienteId;
    }

    public void setClienteId(ID clienteId) {
        this.clienteId = clienteId;
    }
}

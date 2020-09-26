package common.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public abstract class ClienteDomainBean<ID> extends DomainBean<ID> {

    public interface Attributes extends DomainBean.Attributes {
        String CLIENTE_ID = "clienteId";
    }

    private ID clienteId;

    public ID getClienteId() {
        return clienteId;
    }

    public void setClienteId(ID clienteId) {
        this.clienteId = clienteId;
    }
}

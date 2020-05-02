package common.types;

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

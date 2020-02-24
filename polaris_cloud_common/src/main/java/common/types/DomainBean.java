package common.types;

public abstract class DomainBean<ID> extends IdObject<ID> {
    public DomainBean() {
    }

    public interface Attributes extends IdObject.Attributes {
    }
}

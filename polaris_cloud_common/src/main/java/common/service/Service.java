package common.service;

import common.types.DomainBean;

import java.util.List;

public interface Service<D extends DomainBean<ID>, ID> {

    ID insert(D var1);

    List<D> insert(List<D> var1);

    void update(D var1);

    void update(List<D> var1);

    List<D> updateBatchInList(List<D> var1);

    void update(ID id, D var1);

    void delete(ID var1);

    void delete(D var1);

    D findById(ID var1);

    <R> R findById(ID var1, Class<R> var2);

    List<D> findAll();

    <R> List<R> findAll(Class<R> var1);
}

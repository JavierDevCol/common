package common.controller;

import common.filter.Filter;
import common.service.Service;
import common.types.DomainBean;
import common.types.ListResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public abstract class ControllerWithFilterImpl<D extends DomainBean<ID>, ID extends Serializable, T extends Filter> {

    @PostMapping
    @ResponseBody
    public ID insert(@NotNull @RequestBody @Validated D request) {
        return getService().insert(request);
    }

    @PostMapping(path = "/batch")
    @ResponseBody
    public ListResponse<D> insertBatch(@NotNull @RequestBody @Validated List<D> request) {
        return new ListResponse<D>(getService().insert(request));
    }

    @PutMapping
    public void update(@NotNull @RequestBody @Validated D request) {
        getService().update(request);
    }

    @PutMapping(path = "/batch")
    @ResponseBody
    public ListResponse<D> updateBatch(@NotNull @RequestBody @Validated List<D> request) {
        return new ListResponse<D>(getService().updateBatchInList(request));
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@NotNull @PathVariable ID id) {
        getService().delete(id);
    }

    @DeleteMapping(path = "/batch")
    public void delete(@NotNull @RequestParam List<ID> id) {
        getService().deleteAllById(id);
    }

    @GetMapping(path = "/{id}")
    @ResponseBody
    public D findById(@NotNull @PathVariable ID id) {
        return getService().findById(id);
    }

    @GetMapping
    public ListResponse<D> findAll(T filter) {
        return new ListResponse<D>(getService().findAll());
    }

    public abstract Service<D, ID> getService();
}

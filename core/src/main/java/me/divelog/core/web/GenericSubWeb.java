package me.divelog.core.web;

import me.divelog.core.grid.GridReq;
import me.divelog.core.grid.GridRes;
import me.divelog.core.repository.BaseRepository;
import me.divelog.core.service.GenericService;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.Serializable;

public abstract class GenericSubWeb<E, ID extends Serializable, DTO, GREQ extends GridReq<E>, GRES extends GridRes<DTO>, S extends GenericService<E, ID, ? extends BaseRepository<E, ID>>> extends GenericWeb<E, ID, DTO, GREQ, GRES, S> {
    /**
     * owner(main) 모듈 이름
     *
     * @return
     */
    protected abstract String ownerName();


    /**
     * ADD Entity ==============================================================
     **/

    @Override
    public String addForm(Model model) {
        throw new UnsupportedOperationException("use '/subadd' instead");
    }

    @Override
    public String addSubmit(E entity, BindingResult errors, SessionStatus status, Model model, RedirectAttributes redirectAttributes) {
        throw new UnsupportedOperationException("use '/subadd' instead");
    }

    @Override
    protected void addEntity(E entity) {
        throw new UnsupportedOperationException();
    }

    @RequestMapping(value = "/subadd", method = RequestMethod.GET)
    public String addForm(@PathVariable("ownerId") ID ownerId, Model model) {
        E entity = createEntity();

        defaults(entity);
        model.addAttribute(modelName(), entity);
        formReferenceData(model, entity, ownerId);
        addReferenceData(model, entity, ownerId);

        return addFormView();
    }

    @Override
    protected String addFormView() {
        return prefix() + ownerName() + "/sub" + moduleName() + "add";
    }

    @PostMapping("/subadd")
    public String addSubmit(@PathVariable("ownerId") ID ownerId, @ModelAttribute @Valid E entity, BindingResult errors, SessionStatus status, Model model, RedirectAttributes redirectAttributes) {
        validate(entity, errors);
        validateAdd(entity, errors);

        if (errors.hasErrors()) {
            formReferenceData(model, entity, ownerId);
            addReferenceData(model, entity, ownerId);
            return addFormView();
        }

        status.setComplete();
        addSubEntity(ownerId, entity);

        return addSubmitSuccessView(entity, model, redirectAttributes);
    }

    abstract protected void addSubEntity(ID ownerId, E entity);

    /**
     * EDIT Entity ==============================================================
     **/

    @Override
    public String editForm(ID id, Model model) {
        throw new UnsupportedOperationException("use '/subedit' instead");
    }

    @Override
    public String editSubmit(ID id, E entity, BindingResult errors, SessionStatus status, Model model, RedirectAttributes ra) {
        throw new UnsupportedOperationException("use '/subedit' instead");
    }


    @Override
    protected void editEntity(E entity) {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/{id}/subedit")
    public String editForm(@PathVariable("ownerId") ID ownerId, @PathVariable ID id, Model model) {
        E entity = service.findOne(id);

        model.addAttribute(modelName(), entity);
        formReferenceData(model, entity, ownerId);
        editReferenceData(model, entity, ownerId);

        return editFormView();
    }

    @Override
    protected String editFormView() {
        return prefix() + ownerName() + "/sub" + moduleName() + "edit";
    }

    @PutMapping("/{id}/subedit")
    public String editSubmit(@PathVariable("ownerId") ID ownerId, ID id, @ModelAttribute @Valid E entity, BindingResult errors, SessionStatus status, Model model, RedirectAttributes ra) {
        validate(entity, errors);
        validateEdit(entity, errors);

        if (errors.hasErrors()) {
            formReferenceData(model, entity, ownerId);
            editReferenceData(model, entity, ownerId);
            return editFormView();
        }

        status.setComplete();
        editSubEntity(ownerId, entity);

        return editSubmitSuccessView(entity, model, ra);
    }

    abstract protected void editSubEntity(ID ownerId, E entity);

    /**
     * ======================== VALIDATION ======================
     **/

    protected void formReferenceData(Model model, E entity, ID ownerId) {
        super.formReferenceData(model, entity);
    }

    protected void addReferenceData(Model model, E entity, ID ownerId) {
        super.addReferenceData(model, entity);
    }

    protected void editReferenceData(Model model, E entity, ID ownerId) {
        super.editReferenceData(model, entity);
    }

}

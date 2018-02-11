package me.divelog.core.web;

import me.divelog.core.grid.GridReq;
import me.divelog.core.grid.GridRes;
import me.divelog.core.repository.BaseRepository;
import me.divelog.core.service.GenericService;
import me.divelog.core.web.message.MessageSourceSupport;
import me.divelog.core.support.CommonUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.List;

public abstract class GenericWeb<E, ID extends Serializable, DTO, GREQ extends GridReq<E>, GRES extends GridRes<DTO>, S extends GenericService<E, ID, ? extends BaseRepository<E, ID>>> {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected static final String VIEW_CLOSE_AND_REFRESH = "close-and-refresh";

    protected Class<E> entityClass;
    protected Class<GRES> gridResClass;
    protected Class<DTO> dataClass;

    private String defaultModuleName;
    private String defaultModelName;

    @Autowired
    protected S service;
    @Autowired
    protected ModelMapper mapper;
    @Autowired
    private MessageSource messageSource;

    /**
     * URL 생성에 사용
     *
     * @return
     */
    protected String moduleName() {
        return this.defaultModuleName;
    }

    /**
     * Session 저장,
     *
     * @return
     */
    protected String modelName() {
        return this.defaultModelName;
    }

    protected String prefix() {
        return "";
    }

    protected String suffix() {
        return "";
    }

    @PostConstruct
    public final void initInternal() {
        entityClass = CommonUtils.getGenericTypeParam(getClass(), 0);
        dataClass = CommonUtils.getGenericTypeParam(getClass(), 2);
        gridResClass = CommonUtils.getGenericTypeParam(getClass(), 4);

        String entityName = entityClass.getSimpleName();
        this.defaultModuleName = entityName.toLowerCase();
        this.defaultModelName = entityName.substring(0, 1).toLowerCase() + entityName.substring(1);
    }

    /**
     * reference data for main page
     *
     * @param model
     */
    protected void mainReferenceData(Model model) {
    }

    @GetMapping
    public String main(Model model) {
        mainReferenceData(model);
        return prefix() + moduleName() + suffix() + "/" + moduleName() + "main";
    }

    @GetMapping("/grid")
    public @ResponseBody
    GRES grid(GREQ req) {
        List<E> results = service.search(req);
        return convertToData(req, results);
    }

    protected GRES convertToData(GREQ req, List<E> list) {
        GRES res;
        try {
            res = gridResClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        res.setTotal(req.getTotalPages());
        res.setPage(req.getPage());
        res.setRecords(req.getRecords());

        DTO[] data = (DTO[]) Array.newInstance(dataClass, list.size());
        int i = 0;

        for (E c : list) {
            DTO dt = mapper.map(c, dataClass);
            convertMore(c, dt);
            if (dt instanceof MessageSourceSupport)
                MessageSourceSupport.class.cast(dt).setMessageSource(this.messageSource);
            data[i] = dt;
            i++;
        }

        res.setRows(data);

        return res;
    }

    private void convertMore(E entity, DTO dto) {

    }

    /**
     * Common =============================================================
     */

    /**
     * Entity 가 add 혹은 edit 되는 경우에 공통적으로 사용하는 검증 메서드
     *
     * @param entity
     * @param errors
     */
    protected void validate(E entity, BindingResult errors) {
    }


    /**
     * ADD Entity =============================================================
     */

    /**
     * Entity Add View
     *
     * @param model
     * @return
     */
    @GetMapping("/add")
    public String addForm(Model model) {
        E entity = createEntity();
        defaults(entity);
        model.addAttribute(modelName(), entity);
        formReferenceData(model, entity);
        addReferenceData(model, entity);
        return addFormView();
    }

    /**
     * 추가View
     *
     * @return
     */
    protected String addFormView() {
        return prefix() + moduleName() + suffix() + "/" + modelName() + "add";
    }

    /**
     * Entity Add 화면에서만 모델이나 엔티티 작업
     *
     * @param model
     * @param entity
     */
    protected void addReferenceData(Model model, E entity) {
    }

    /**
     * Entity Add/Edit 화면에서 모델이나 엔티티에 대한 공동작업
     *
     * @param model
     * @param entity
     */
    protected void formReferenceData(Model model, E entity) {
    }

    /**
     * 기본적으로 해야하는 작업
     *
     * @param entity
     */
    protected void defaults(E entity) {
    }

    /**
     * Generic 선언된 E타입 엔티티 생성반환
     *
     * @return
     */
    protected E createEntity() {
        try {
            return entityClass.newInstance();
        } catch (Exception e) {
            logger.error("Occurred Exception: {}", e);
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/add")
    public String addSubmit(@ModelAttribute @Valid E entity, BindingResult errors, SessionStatus status, Model model, RedirectAttributes redirectAttributes) {
        validate(entity, errors);
        validateAdd(entity, errors);

        if (errors.hasErrors()) {
            formReferenceData(model, entity);
            addReferenceData(model, entity);
            return addFormView();
        }

        status.setComplete();
        addEntity(entity);
        return addSubmitSuccessView(entity, model, redirectAttributes);
    }

    /**
     * Entity 추가시 처리사항
     *
     * @param entity
     */
    protected void addEntity(E entity) {
        service.save(entity);
    }

    /**
     * Entity 추가 성공 후 처리화면
     *
     * @param entity
     * @param model
     * @param redirectAttributes
     * @return
     */
    protected String addSubmitSuccessView(@Valid E entity, Model model, RedirectAttributes redirectAttributes) {
        return VIEW_CLOSE_AND_REFRESH;
    }

    /**
     * Entity 가 add 될 때 사용하는 검증메서드
     *
     * @param entity
     * @param errors
     */
    protected void validateAdd(E entity, BindingResult errors) {
    }

    /**
     * EDIT Entity =============================================================
     */

    /**
     * 수정화면 View
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/{id}/edit")
    public String editForm(@PathVariable ID id, Model model) {
        E entity = service.findOne(id);

        model.addAttribute(modelName(), entity);
        formReferenceData(model, entity);
        editReferenceData(model, entity);

        return editFormView();
    }

    protected String editFormView() {
        return prefix() + moduleName() + suffix() + "/" + moduleName() + "edit";
    }

    /**
     * Entity 수정에 필요한 정보 추가
     *
     * @param model
     * @param entity
     */
    protected void editReferenceData(Model model, E entity) {
    }

    /**
     * Submit Entity edit.
     *
     * @param id
     * @param entity
     * @param errors
     * @param status
     * @param model
     * @param ra
     * @return
     */
    @PutMapping(value = "/{id}/edit")
    public String editSubmit(@PathVariable ID id, @ModelAttribute @Valid E entity, BindingResult errors, SessionStatus status, Model model, RedirectAttributes ra) {
        validate(entity, errors);
        validateEdit(entity, errors);

        if (errors.hasErrors()) {
            formReferenceData(model, entity);
            editReferenceData(model, entity);
            return editFormView();
        }

        status.setComplete();
        editEntity(entity);

        return editSubmitSuccessView(entity, model, ra);
    }

    protected void editEntity(E entity) {
        service.save(entity);
    }

    /**
     * Entity 를 edit 할 때 사용하는 검증 메서드
     *
     * @param entity
     * @param errors
     */
    protected void validateEdit(E entity, BindingResult errors) {
    }

    protected String editSubmitSuccessView(E entity, Model model, RedirectAttributes redirectAttributes) {
        return VIEW_CLOSE_AND_REFRESH;
    }

    /**
     * DELETE Entity =============================================================
     */

    /**
     * Entity 삭제
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/{id}/delete")
    public String delete(@PathVariable ID id, Model model) {
        service.deleteById(id);

        return deleteSuccessView(id, model);
    }

    /**
     * 삭제 후 화면처리 정의
     *
     * @param id
     * @param model
     * @return
     */
    protected String deleteSuccessView(ID id, Model model) {
        return VIEW_CLOSE_AND_REFRESH;
    }
}

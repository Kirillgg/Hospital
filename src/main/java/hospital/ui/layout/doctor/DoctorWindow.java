package hospital.ui.layout.doctor;

import hospital.database.DoctorDao;
import hospital.model.Base;
import hospital.model.Doctor;
import hospital.ui.layout.CreateOrUpdateWindow;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * Window for create and update Doctor.
 *
 * @author Kirill
 */
public final class DoctorWindow extends CreateOrUpdateWindow {
     /**
     * wordValidator - regular expression for validate field that is type word (starting with upper case)
     * binder - object bind field from Doctor object with fields in window
     * doctorDao - DAO for doctor
     */
    private final RegexpValidator wordValidator = new RegexpValidator("[A-Z][a-z]{2,49}", true, "Enter the word length from 3 to 50 characters (with a capital letter)");
    private final BeanFieldGroup<Doctor> binder = new BeanFieldGroup<>(Doctor.class);
    private final DoctorDao doctorDao = new DoctorDao();

    public DoctorWindow(String caption) {

        super(caption);
        setResizable(false);
        setWidth(500.0f, Unit.PIXELS);
        setModal(true);
        setClosable(false);

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        setContent(layout);
        //initialization input fields and adding in window
        layout.addComponent(initFields());
        //initialization OK, Cancel button
        HorizontalLayout buttonLayout = new CreateOrUpdateButtons(binder, doctorDao);
        layout.addComponent(buttonLayout);
        layout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);
        center();

    }
    /**
     * Method binding item with fields
     * @param base item for binding
     */
    @Override
    public void bindBeanWithFields(Base base) {
        binder.setItemDataSource((Doctor) base);
    }
    /**
     * Method clear fields and set empty instance doctor
     */
    @Override
    protected void bindClearBean() {
        binder.setItemDataSource(new Doctor());
        binder.clear();
    }
    /**
     * Initialization fields for input doctor.
     * 
     * @return FormLayout with fields
     */
    private FormLayout initFields() {
        FormLayout formLayout = new FormLayout();

        formLayout.addComponent(binder.buildAndBind("First name", "firstName"));
        formLayout.addComponent(binder.buildAndBind("Middle name", "middleName"));
        formLayout.addComponent(binder.buildAndBind("Last name", "lastName"));
        formLayout.addComponent(binder.buildAndBind("Specialization", "specialization"));
        binder.getFields().forEach(field -> {
            field.setRequired(true);
            field.setWidth("100%");
            field.addValidator(wordValidator);
        });
        bindClearBean();
        return formLayout;
    }

}

package hospital.ui.layout.patient;

import hospital.database.PatientDao;
import hospital.model.Base;
import hospital.model.Patient;
import hospital.ui.layout.CreateOrUpdateWindow;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 * Window for create and update Patient.
 *
 * @author Kirill
 */
public final class PatientWindow extends CreateOrUpdateWindow {

    /**
     * wordValidator - regular expression for validate field that is word type(starting with upper case) 
     * binder - object bind field from Patient object with fields in window 
     * doctorDao - DAO for patient
     */
    private final RegexpValidator wordValidator = new RegexpValidator("[A-Z][a-z]{2,49}", true, "Enter the word length from 3 to 50 characters (with a capital letter)");
    private final BeanFieldGroup<Patient> binder = new BeanFieldGroup<>(Patient.class);
    private final PatientDao pacientDao = new PatientDao();

    public PatientWindow(String caption) {

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
        HorizontalLayout buttonLayout = new CreateOrUpdateButtons(binder, pacientDao);
        layout.addComponent(buttonLayout);
        layout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);
        center();

    }

    /**
     * Method binding item with fields
     *
     * @param base item for binding
     */
    @Override
    public void bindBeanWithFields(Base base) {
        binder.setItemDataSource((Patient) base);
    }

    /**
     * Method clear fields and set empty instance patient
     */
    @Override
    protected void bindClearBean() {
        binder.setItemDataSource(new Patient());
        binder.clear();
    }

    /**
     * Initialization fields for input patient.
     *
     * @return FormLayout with fields
     */
    private FormLayout initFields() {
        FormLayout formLayout = new FormLayout();

        formLayout.addComponent(binder.buildAndBind("First name", "firstName"));
        formLayout.addComponent(binder.buildAndBind("Middle name", "middleName"));
        formLayout.addComponent(binder.buildAndBind("Last name", "lastName"));
        binder.getFields().forEach(field -> {
            field.addValidator(wordValidator);
        });

        formLayout.addComponent(binder.buildAndBind("Phone", "phone"));
        binder.getFields().forEach(field -> {
            field.setRequired(true);
            field.setWidth("100%");
            ((TextField) field).setNullRepresentation("");
        });
        bindClearBean();
        return formLayout;
    }

}

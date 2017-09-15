package hospital.ui.layout.recipe;

import hospital.database.DoctorDao;
import hospital.database.PatientDao;
import hospital.database.RecipeDao;
import hospital.model.Base;
import hospital.model.Doctor;
import hospital.model.Patient;
import hospital.model.Recipe;
import hospital.ui.layout.CreateOrUpdateWindow;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.validator.LongRangeValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * Window for create and update Recipe.
 *
 * @author Kirill
 */
public final class RecipeWindow extends CreateOrUpdateWindow {

    /**
     * binder - object bind field from Recipe object with fields in window
     * selectDoctor - select for selecting Doctor 
     * selectPatient - select for selecting Patient 
     * selectPriority - select for selecting Priority
     * doctorDao - DAO for doctor 
     * patientDao - DAO for patient 
     * recipeDao - DAO for recipe
     */
    private final BeanFieldGroup<Recipe> binder = new BeanFieldGroup<>(Recipe.class);
    private NativeSelect selectDoctor;
    private NativeSelect selectPatient;
    private NativeSelect selectPriority;
    private final DoctorDao doctorDao;
    private final PatientDao patientDao;
    private final RecipeDao recipeDao;

    public RecipeWindow(String caption) {
        super(caption);
        setResizable(false);
        setWidth(500.0f, Unit.PIXELS);
        setModal(true);
        setClosable(false);
        //init final object
        doctorDao = new DoctorDao();
        patientDao = new PatientDao();
        recipeDao = new RecipeDao();

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        setContent(layout);
        //initialization fields and adding to widndow
        layout.addComponent(initFields());

        HorizontalLayout buttonLayout = new CreateOrUpdateButtons(binder, recipeDao);
        layout.addComponent(buttonLayout);
        layout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);
        center();
    }

    /**
     * Initialization fields for input recipe.
     *
     * @return FormLayout with fields
     */
    private FormLayout initFields() {
        FormLayout formLayout = new FormLayout();

        selectDoctor = new NativeSelect("Doctors", doctorDao.getAll());
        selectPatient = new NativeSelect("Pacient", patientDao.getAll());
        selectPriority = new NativeSelect("Priority", RecipeDao.getAllPriority());
        DateField dateCreate = new DateField("Date create");
        TextField duration = new TextField("Duration");
        TextArea description = new TextArea("Description");

        selectDoctor.setNullSelectionAllowed(false);
        selectPatient.setNullSelectionAllowed(false);
        selectPriority.setNullSelectionAllowed(false);
        duration.setNullSettingAllowed(false);
        description.setNullSettingAllowed(false);

        duration.setNullRepresentation("");
        description.setNullRepresentation("");
        //adding validator for Long > 0 - duration
        duration.addValidator(new LongRangeValidator("Number of days greater than 0", (long) 1, Long.MAX_VALUE));
        //adding length validator for description
        description.addValidator(new StringLengthValidator("The number of characters is greater than 5", 5, 200, false));

        duration.setInputPrompt("Enter the number of days");
        description.setInputPrompt("Enter description");

        dateCreate.setRangeStart(Date.from(Instant.now()));
        //binding fields
        binder.bind(selectDoctor, "doctor");
        binder.bind(selectPatient, "patient");
        binder.bind(selectPriority, "priority");
        binder.bind(dateCreate, "dateCreate");
        binder.bind(duration, "duration");
        binder.bind(description, "description");
        binder.getFields().forEach(field -> {
            field.setRequired(true);
            field.setSizeFull();
            formLayout.addComponent(field);
        });
        bindClearBean();

        return formLayout;
    }

    /**
     * Method refreshing data in selectDoctor and selectPatient.
     *
     */
    public void refreshData() {
        if (!this.isConnectorEnabled()) {
            return;
        }
        Doctor prevDoctor = (Doctor) selectDoctor.getConvertedValue();
        List<Doctor> newDoctors = doctorDao.getAll();
        selectDoctor.removeAllItems();
        selectDoctor.addItems(newDoctors);
        int indexOfDoctor = newDoctors.indexOf(prevDoctor);
        if (indexOfDoctor > -1) {
            selectDoctor.select(newDoctors.get(indexOfDoctor));
        } else {
            if (prevDoctor != null) {
                //other user delete selected doctor
                Notification.show("Error", "The doctor was removed", Type.ERROR_MESSAGE);
            }
        }

        Patient prevPatient = (Patient) selectPatient.getConvertedValue();
        List<Patient> newPatients = patientDao.getAll();
        selectPatient.removeAllItems();
        selectPatient.addItems(newPatients);
        int indexOfPacient = newPatients.indexOf(prevPatient);
        if (indexOfPacient > -1) {
            selectPatient.select(newPatients.get(indexOfPacient));
        } else {
            if (prevPatient != null) {
                //other user delete selected patient
                Notification.show("Error", "The patient was removed", Type.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Method clear fields and set empty instance patient
     */
    @Override
    protected void bindClearBean() {
        binder.setItemDataSource(new Recipe());
        binder.clear();
    }

    /**
     * Method binding item with fields
     *
     * @param base item for binding
     */
    @Override
    public void bindBeanWithFields(Base base
    ) {
        binder.setItemDataSource((Recipe) base);
    }

}

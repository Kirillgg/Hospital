package hospital.ui.layout.patient;

import hospital.database.PatientDao;
import hospital.model.Base;
import hospital.model.Patient;
import hospital.ui.layout.CrudButtonLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import hospital.ui.layout.RefreshableLayout;
import com.vaadin.data.util.AbstractBeanContainer;
import com.vaadin.data.util.BeanContainer;
import java.util.List;

/**
 * Main layout for patients consists of grid (table) + crudButtons.
 *
 * @author Kirill
 */
public final class PatientLayout extends HorizontalLayout implements RefreshableLayout {

    /**
     * patientGrid - table patients 
     * container - list patients with properties 
     * idResolver - object mapping bean in container with id that is used for communicate with container 
     * patients - last list patients take from DB 
     * pacientDao - DAO for doctor
     */
    private final Grid patientGrid;
    private final BeanContainer container;
    private final AbstractBeanContainer.BeanIdResolver idResolver;
    private List<Patient> patients;
    private final PatientDao pacientDao;

    public PatientLayout() {
        super();
        setSizeFull();
        setSpacing(true);
        pacientDao = new PatientDao();
        //init final object 
        patientGrid = new Grid();
        container = new BeanContainer(Patient.class);
        //id resolver taking id like id patient
        idResolver = (AbstractBeanContainer.BeanIdResolver<Long, Patient>) (Patient bean) -> bean.getId();

        initPacientGrid();
        //create layout with create, update, delete buttons
        CrudButtonLayout crudButtonLayout = new CrudButtonLayout(this, new PatientWindow("Patient"), new PatientDao());
        crudButtonLayout.setWidth("100%");
        crudButtonLayout.setSpacing(true);

        addComponent(patientGrid);
        addComponent(crudButtonLayout);
        //set relation size between  patientGrid and crudButtonLayout in it layout
        setExpandRatio(patientGrid, 6f);
        setExpandRatio(crudButtonLayout, 1f);
    }
    /**
     * Initialization grid column and data container
     * 
     */
    private void initPacientGrid() {
        patientGrid.setSizeFull();
        patientGrid.setColumns("firstName", "lastName", "middleName", "phone");
        patientGrid.getColumns().forEach(column -> {
            column.setResizable(false);
        });

        initDataContainer();

        patientGrid.setContainerDataSource(container);
    }
    /**
     * Initialization container
     * 
     */
    private void initDataContainer() {
        container.setBeanIdResolver(idResolver);
        patients = pacientDao.getAll();
        container.addAll(patients);
    }

    /**
     * Method for refresh data in layout. Executing refresh container for
     * patientGrid. Taking all patients from BD and compare with previous list
     * of patients. New items from DB adding in container. Old items in
     * container that not exist in new list from database deleting from
     * container.
     */
    @Override
    public void refreshData() {
        if (!this.isConnectorEnabled()) {
            return;
        }
        List<Patient> newPatients = pacientDao.getAll();
        patients.forEach(oldPatient -> {
            if (!newPatients.contains(oldPatient)) {
                container.removeItem(idResolver.getIdForBean(oldPatient));
            }
        }
        );
        container.addAll(newPatients);
        patients = newPatients;
    }
    /**
     * Getting selected item in patientGrid.
     *
     * @return Patient or null
     */
    @Override
    public Base getSelectedItem() {
        Object id = patientGrid.getSelectedRow();
        if (id != null) {
            return (Patient) (container.getItem(id).getBean());
        }
        return null;
    }

}

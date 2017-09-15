package hospital.ui.layout.doctor;

import hospital.database.DoctorDao;
import hospital.model.Base;
import hospital.model.Doctor;
import hospital.model.Recipe;
import hospital.ui.layout.CrudButtonLayout;
import com.vaadin.data.Item;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import java.util.Set;
import hospital.ui.layout.RefreshableLayout;
import com.vaadin.data.util.AbstractBeanContainer;
import com.vaadin.data.util.BeanContainer;
import java.util.List;

/**
 * Main layout for doctors consists of grid (table) + crudButtons.
 *
 * @author Kirill
 */
public final class DoctorLayout extends HorizontalLayout implements RefreshableLayout {

    /**
     * doctorGrid - table doctors 
     * container - list doctors with properties 
     * gpc - wrapper for container consists of property for showing count recipes given by doctor 
     * idResolver - object mapping bean in container with id that is used for communicate with container 
     * countRecipesProperty - property for executing count recipes given by doctor 
     * doctors - last list doctors take from DB 
     * doctorDao - DAO for doctor
     */
    private final Grid doctorGrid;
    private final BeanContainer container;
    private final GeneratedPropertyContainer gpc;
    private final AbstractBeanContainer.BeanIdResolver idResolver;
    private PropertyValueGenerator<Integer> countRecipesProperty;
    private List<Doctor> doctors;
    private final DoctorDao doctorDao;

    public DoctorLayout() {
        super();
        setSizeFull();
        setSpacing(true);
        doctorDao = new DoctorDao();
        //init final object 
        doctorGrid = new Grid();
        container = new BeanContainer(Doctor.class);
        gpc = new GeneratedPropertyContainer(container);
        //id resolver taking id like id doctor
        idResolver = (AbstractBeanContainer.BeanIdResolver<Long, Doctor>) (Doctor bean) -> bean.getId();

        initDoctorGrid();
        //create layout with create, update, delete buttons
        CrudButtonLayout crudButtonLayout = new CrudButtonLayout(this, new DoctorWindow("Doctor"), new DoctorDao());
        crudButtonLayout.setWidth("100%");
        crudButtonLayout.setSpacing(true);

        addComponent(doctorGrid);
        addComponent(crudButtonLayout);
        //set relation size between  doctorGrid and crudButtonLayout in it layout
        setExpandRatio(doctorGrid, 6f);
        setExpandRatio(crudButtonLayout, 1f);
    }

    /**
     * Initialization grid column and data container
     * 
     */
    private void initDoctorGrid() {
        doctorGrid.setSizeFull();
        doctorGrid.setColumns("firstName", "lastName", "middleName", "specialization", "countRecipes");

        initDataContainer();

        doctorGrid.getColumns().forEach(column -> {
            column.setResizable(false);
        });
        doctorGrid.setContainerDataSource(gpc);

    }

    /**
     * Initialization container and set wrapper
     * 
     */
    private void initDataContainer() {
        countRecipesProperty = new PropertyValueGenerator<Integer>() {
            @Override
            public Integer getValue(Item item, Object itemId, Object propertyId) {

                return ((Set<Recipe>) (item.getItemProperty("recipes").getValue())).size();
            }

            @Override
            public Class<Integer> getType() {
                return Integer.class;
            }
        };

        container.setBeanIdResolver(idResolver);
        container.addNestedContainerProperty("recipes");
        doctors = doctorDao.getAll();
        container.addAll(doctors);

        gpc.addGeneratedProperty("countRecipes", countRecipesProperty);

    }

    /**
     * Method for refresh data in layout. Executing refresh container for
     * doctorGrid. Taking all doctors from BD and compare with previous list of
     * doctors. New items from DB adding in container. Old items in container
     * that not exist in new list from database deleting from container.
     */
    @Override
    public void refreshData() {
        if (!this.isConnectorEnabled()) {
            return;
        }
        List<Doctor> newDoctors = doctorDao.getAll();
        
        doctors.forEach(oldDoctor -> {
            if (!newDoctors.contains(oldDoctor)) {
                container.removeItem(idResolver.getIdForBean(oldDoctor));
            }
        }
        );
        container.addAll(newDoctors);
        doctors = newDoctors;
    }

    /**
     * Getting item selected in doctorGrid.
     *
     * @return Doctor or null
     */
    @Override
    public Base getSelectedItem() {
        Object id = doctorGrid.getSelectedRow();
        if (id != null) {
            return (Doctor) (container.getItem(id).getBean());
        }
        return null;
    }
}

package hospital.ui.layout.recipe;

import hospital.database.RecipeDao;
import hospital.model.Base;
import hospital.model.Doctor;
import hospital.model.Patient;
import hospital.model.Priority;
import hospital.model.Recipe;
import hospital.ui.layout.CrudButtonLayout;
import com.vaadin.data.util.AbstractBeanContainer.BeanIdResolver;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import hospital.ui.layout.RefreshableLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import java.util.List;

/**
 * Main layout for doctors consists of grid (table) + crudButtons +
 * filterLayout.
 *
 * @author Kirill
 */
public final class RecipeLayout extends HorizontalLayout implements RefreshableLayout {

    /**
     * recipeGrid - table recipes 
     * container - list recipes with properties
     * idResolver - object mapping bean in container with id that is used for communicate with container 
     * recipes - last list doctors take from DB
     * recipeDao - DAO for doctor 
     * recipeWindow - window for create and update that needing to refreshing
     */
    private final Grid recipeGrid;
    private final BeanContainer container;
    private List<Recipe> recipes;
    private final BeanIdResolver idResolver;
    private final RecipeDao recipeDao;
    private final RecipeWindow recipeWindow;

    public RecipeLayout() {
        super();
        setSizeFull();
        setSpacing(true);
        //initialization fintal objects
        recipeDao = new RecipeDao();
        recipeWindow = new RecipeWindow("Recipe");
        recipeGrid = new Grid();
        container = new BeanContainer(Recipe.class);
        //id resolver taking id like id recipe
        idResolver = (BeanIdResolver<Long, Recipe>) (Recipe bean) -> bean.getId();
        //initialization recipeGrid (table recipes)
        initRecipeGrid();
        //create layout with create, update, delete buttons
        CrudButtonLayout crudButtonLayout = new CrudButtonLayout(this, recipeWindow, new RecipeDao());
        crudButtonLayout.setWidth("100%");
        crudButtonLayout.setSpacing(true);
        //adding filter layout to crud layout
        crudButtonLayout.addComponent(createFilterPanel());

        addComponent(recipeGrid);
        addComponent(crudButtonLayout);
        //set relation size between  recipeGrid and crudButtonLayout in it layout
        setExpandRatio(recipeGrid, 6.0f);
        setExpandRatio(crudButtonLayout, 1.0f);
    }

    /**
     * Initialization grid column and data container
     */
    private void initRecipeGrid() {
        recipeGrid.setSizeFull();
        recipeGrid.setEditorEnabled(false);
        recipeGrid.setColumns("description", "dateCreate", "duration");
        recipeGrid.addColumn("priority.name").setHeaderCaption("Priority");
        recipeGrid.addColumn("patient", Patient.class);
        recipeGrid.addColumn("doctor", Doctor.class);
        recipeGrid.getColumns().forEach(column -> {
            column.setResizable(false);
        });

        initDataContainer();

        recipeGrid.setContainerDataSource(container);
    }

    /**
     * Initialization filterLayout for Description, Patient, Priority fields.
     *
     * @return FormLayout for filtering
     */
    private FormLayout createFilterPanel() {
        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("100%");
        formLayout.setCaption("Filter");

        TextField descriptionFilterField = new TextField("Description:");
        TextField pacientFilterField = new TextField("Patient:");
        NativeSelect priorityFilterField = new NativeSelect("Priority:", RecipeDao.getAllPriority());

        Priority anyPriority = new Priority();
        anyPriority.setName("Any");
        priorityFilterField.setNullSelectionAllowed(true);
        priorityFilterField.setNullSelectionItemId(anyPriority);

        Button sumbitButton = new Button("Filtering");

        sumbitButton.addClickListener(event -> {
            container.removeAllContainerFilters();
            container.addContainerFilter("description", descriptionFilterField.getValue().trim(), true, false);
            container.addContainerFilter("patient", pacientFilterField.getValue().trim(), true, false);
            Priority selectedPriority = (Priority) (priorityFilterField.getConvertedValue());
            if (selectedPriority != null) {
                container.addContainerFilter("priority", selectedPriority.getName(), true, false);
            }
        });
        
        formLayout.addComponent(descriptionFilterField);
        formLayout.addComponent(pacientFilterField);
        formLayout.addComponent(priorityFilterField);
        formLayout.addComponent(sumbitButton);
        
        formLayout.forEach(filterField -> {
            filterField.setWidth("100%");
        });
        return formLayout;
    }

    /**
     * Initialization container.
     *
     */
    public void initDataContainer() {
        container.setBeanIdResolver(idResolver);
        recipes = recipeDao.getAll();
        container.addAll(recipes);
        container.addNestedContainerProperty("priority.name");

    }

    /**
     * Method for refresh data in layout. Executing refresh container for
     * recipeGrid. Taking all recipes from BD and compare with previous list of
     * recipes. New items from DB adding in container. Old items in container
     * that not exist in new list from database deleting from container. Also
     * refreshing RecipeWindow for updating SelectedFields that includes list
     * doctors and patients.
     */
    @Override
    public void refreshData() {
        List<Recipe> newRecipes = recipeDao.getAll();
        recipes.forEach(oldRecipe -> {
            if (!newRecipes.contains(oldRecipe)) {
                container.removeItem(idResolver.getIdForBean(oldRecipe));
            }
        }
        );
        container.addAll(newRecipes);
        recipes = newRecipes;
        recipeWindow.refreshData();
    }

    /**
     * Getting selected item in recipeGrid.
     *
     * @return Recipe or null
     */
    @Override
    public Base getSelectedItem() {
        Object id = recipeGrid.getSelectedRow();
        if (id != null) {
            return (Recipe) (container.getItem(id).getBean());
        }
        return null;
    }

}

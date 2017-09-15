package hospital.ui.layout;

import hospital.database.Dao;
import hospital.database.ForeignKeyError;
import hospital.model.Base;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class using in 3 main layouts Doctor, Patient, Recipe layout. Class is
 * VerticalLayout that contains 3 Button : create, update, delete. When you
 * click on the button create/update will show window for create/update entity.
 * When you click on the button delete will triggered delete method by DAO
 * class.
 *
 * @author Kirill
 */
public class CrudButtonLayout extends VerticalLayout {

    /**
     * buttons - 3 buttons create/update/delete.
     */
    private final Map<String, Button> buttons = new LinkedHashMap<>();
    /**
     * Constructor 
     * @param rootLayout - layout that will give the selected item in the table and will be updated after operation.
     * @param createOrUpdateWindow - window that will show for create/update entity with build in DAO classes.
     * @param daoClassForDelete - DAO which will have a method called delete selected entity in rootLayout.
     */
    public CrudButtonLayout(RefreshableLayout rootLayout, CreateOrUpdateWindow createOrUpdateWindow, Dao daoClassForDelete) {
        //initialization 3 buttons
        buttons.put("Create", new Button("Create new "));
        buttons.put("Update", new Button("Update selected"));
        buttons.put("Delete", new Button("Delete selected"));

        buttons.forEach((key, button) -> {
            button.setSizeFull();
            addComponent(button);
        });
        //set lisntner on these buttons
        setButtonListner(rootLayout, createOrUpdateWindow, daoClassForDelete);
    }

    private void setButtonListner(RefreshableLayout rootLayout, CreateOrUpdateWindow createOrUpdateWindow, Dao daoClassForDelete) {
        //create listner
        buttons.get("Create").addClickListener(event -> {
            getUI().getUI().addWindow(createOrUpdateWindow);
            rootLayout.refreshData();
        });
        //update listner
        buttons.get("Update").addClickListener(event -> {
            Base bean = rootLayout.getSelectedItem();
            if (bean != null) {
                createOrUpdateWindow.bindBeanWithFields(bean);
                getUI().getUI().addWindow(createOrUpdateWindow);
                rootLayout.refreshData();
            } else {
                //if u don't selected item in table and click update
                Notification.show("Selection error:", "Select the object to update", Type.ERROR_MESSAGE);
            }
        });
        //delete listner
        buttons.get("Delete").addClickListener(event -> {
            Base bean = rootLayout.getSelectedItem();
            if (bean != null) {
                try {
                    daoClassForDelete.delete(bean);
                } catch (ForeignKeyError ex) {
                    //possible error such as the removal of a patient/doctor with an existing recipe
                    Notification.show("Remove error:", ex.getMessage(), Type.ERROR_MESSAGE);
                }
                rootLayout.refreshData();
            } else {
                //if u don't selected item in table and click update
                Notification.show("Selection error:", "Select the object to remove", Type.ERROR_MESSAGE);
            }

        });
    }
}

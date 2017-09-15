package hospital.ui.layout;

import hospital.database.Dao;
import hospital.model.Base;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import org.hibernate.StaleStateException;

/**
 * Window using in CrudButtonLayout and showing while u clicking create or
 * update. It includes methods for bind updating bean and method for clear
 * fields(bindClearBean).It also includes inner class CreateOrUpdateButtons that is layout with 2 button: OK/Cancel.
 *
 * @author Kirill
 */
abstract public class CreateOrUpdateWindow extends Window {

    /**
     * Constructor
     */
    public CreateOrUpdateWindow() {
    }
    /**
     * Constructor
     * @param caption - caption for show in left top point.
     */
    public CreateOrUpdateWindow(String caption) {
        super(caption);
    }
    /**
     * method for bind updating item with fields in window.
     * @param base 
     */
    abstract public void bindBeanWithFields(Base base);

    /**
     * method for clear fields and setting empty bean.
     */
    abstract protected void bindClearBean();
    /**
     * Class is layouts that consist OK/Cancel button for create/update window.
     * @param <TypeBean> 
     */
    protected final class CreateOrUpdateButtons<TypeBean extends Base> extends HorizontalLayout {

        public CreateOrUpdateButtons(BeanFieldGroup<TypeBean> binder, Dao createUpdateDao) {
            super();
            setSpacing(true);
            Button acceptButton = new Button("OK", event -> {
                try {
                    //validation
                    binder.commit();
                    //save or update in DB by DAO
                    createUpdateDao.saveOrUpdate(binder.getItemDataSource().getBean());
                    Notification.show("Success", "Operation completed", Notification.Type.TRAY_NOTIFICATION);
                    close();
                    bindClearBean();
                } catch (FieldGroup.CommitException ex) {
                    //Validation error
                    Notification.show("Error", "not valid field", Notification.Type.ERROR_MESSAGE);
                } catch (org.hibernate.AssertionFailure ex) {
                    //Try to send null field
                    Notification.show("Error", "Not all fields are filled", Notification.Type.ERROR_MESSAGE);
                } catch (StaleStateException ex) {
                    close();
                    //Error pop up if u updating item but another user delete it
                    Notification.show("Error", "This object has been deleted", Notification.Type.ERROR_MESSAGE);
                }
            });
            //Close, clear with clicking on Cancel
            Button cancelButton = new Button("Cancel", event -> {
                close();
                bindClearBean();
            });
            addComponent(acceptButton);
            addComponent(cancelButton);
        }

    }
}

package hospital.ui.layout;

import com.vaadin.ui.Layout;

/**
 * Interface allow refresh data in layout.
 *
 * @author Kirill
 */
public interface RefreshableLayout extends Layout, GetSelectedBeanLayout {

    void refreshData();
}

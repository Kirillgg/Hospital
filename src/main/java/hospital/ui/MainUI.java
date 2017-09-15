package hospital.ui;

import hospital.ui.layout.doctor.DoctorLayout;
import hospital.ui.layout.patient.PatientLayout;
import hospital.ui.layout.recipe.RecipeLayout;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import java.util.LinkedHashMap;
import java.util.Map;
import hospital.ui.layout.RefreshableLayout;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Main UI web application. The main part is the TabSheet that switches between
 * 3 main layout: DoctorLayout, PatientLayout, RecipeLayout.
 *
 * @author Kirill
 */
@Push
@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {

    /**
     * layouts - main layouts (DoctorLayout, PatientLayout, RecipeLayout).
     * POOL_TIME - time for sleep updating Tread
     * updateThread - Tread refreshing main layouts from DB
     */
    private final Map<String, RefreshableLayout> layouts = new LinkedHashMap<>();
    private final int POOL_TIME = 1;
    Thread updateThread;

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setMargin(true);
        //Head wepapp
        Label head = new Label("Hospital");
        head.setSizeUndefined();
        mainLayout.addComponent(head);
        mainLayout.setComponentAlignment(head, Alignment.TOP_CENTER);
        //Main TabSheet cointains 3 main layouts
        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        mainLayout.addComponent(tabSheet);
        //initializatin main layouts and adding in tabSheet
        layouts.put("Doctors", new DoctorLayout());
        layouts.put("Pacients", new PatientLayout());
        layouts.put("Recipes", new RecipeLayout());
        layouts.forEach((key, layout) -> {
            tabSheet.addTab(layout, key);
        });
        //initialization updating Thread
        initUpdateThread();
        //set occupies a relative place between Head and TabSheet
        mainLayout.setExpandRatio(tabSheet, 20f);
        mainLayout.setExpandRatio(tabSheet, 1f);
        
        setContent(mainLayout);
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(updateThread, 0, POOL_TIME, TimeUnit.SECONDS);
        

    }
    /**
     * initialization updateThread
     */
    private void initUpdateThread() {
        updateThread = new Thread(() -> {
            if(!Thread.interrupted()) {
                //web socet conntection
                this.access(() -> {
                    //refresh main layouts
                    layouts.forEach((key, layout) -> {
                        layout.refreshData();
                    });
                });
            }
        });
    }
    
    /**
     * method override for stopping updateThread while session closing
     */
    @Override
    public void detach() {
        //stop update thread before close session
        updateThread.interrupt();
        super.detach();

    }
}

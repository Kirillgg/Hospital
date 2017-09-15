package hospital.model;

import java.sql.Date;
import java.util.Objects;

/**
 * Entity mapping table Recipe. Has a relationship : many to one with Doctor;
 * many to one with Patient; many to one with Priority.
 *
 *
 * @author Kirill
 */
public class Recipe extends Base {

    /**
     * description - description of the recipe 
     * patient - patient for which given this recipe 
     * doctor - the doctor who given this recipe 
     * priority - priority (Normal/Cito/Statim) 
     * duration - duration in day 
     * dateCreate - date created recipe
     */
    private String description;
    private Patient patient;
    private Doctor doctor;
    private Priority priority;
    private long duration;
    private Date dateCreate;

    public Recipe() {
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Recipe{" + "description=" + description + ", priority=" + priority + ", duration=" + duration + ", dateCreate=" + dateCreate + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Recipe) {

            Recipe objEqualing = (Recipe) obj;
            return (super.equals(obj)
                    && this.description.equals(objEqualing.description)
                    && this.priority.equals(objEqualing.priority)
                    && this.duration == objEqualing.duration
                    && this.dateCreate.equals(objEqualing.dateCreate));
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();;
        hash = 19 * hash + Objects.hashCode(this.description);
        hash = 19 * hash + Objects.hashCode(this.priority);
        hash = 19 * hash + (int) (this.duration ^ (this.duration >>> 32));
        hash = 19 * hash + Objects.hashCode(this.dateCreate);
        return hash;
    }

}

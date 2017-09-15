package hospital.model;

import java.util.Objects;
import java.util.Set;

/**
 * Entity mapping table Doctor.
 * Has a relationship one to many with Recipe.
 * @author Kirill
 */
public class Doctor extends People {

    /**
     * specialization - specialization
     * recipes - the recipes given by the doctor
     */
    private String specialization;
    private Set<Recipe> recipes;

    public Set<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(Set<Recipe> recipes) {
        this.recipes = recipes;
    }

    public Doctor() {
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Doctor) {
            Doctor objEqualing = (Doctor) obj;
            return (super.equals(obj) && this.specialization.equals(objEqualing.specialization)&& recipes.equals(objEqualing.recipes));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 79 * hash + Objects.hashCode(this.specialization);
        return hash;
    }


}

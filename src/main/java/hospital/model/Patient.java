package hospital.model;

import java.util.Set;

/**
 * Entity mapping table Patient. 
 * Has a relationship one to many with Recipe.
 *
 * @author Kirill
 */
public class Patient extends People {

    /**
     * phone - phone patient
     * recipes - recipes received by the patient
     */
    private int phone;
    private Set<Recipe> recipes;

    public Patient() {
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public Set<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(Set<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Patient) {
            Patient objEqualing = (Patient) obj;
            return (super.equals(obj) && this.phone == objEqualing.phone && recipes.equals(objEqualing.recipes));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 29 * hash + this.phone;
        return hash;
    }
}

package hospital.model;

/**
 * This class contains 3 fields, which describe full name of the People. 
 * Doctor and Patient extend this class.
 *
 * @author Kirill
 */
abstract public class People extends Base {
    /**
     * firstName - First name
     * lastName - Last name
     * middleName - Middle name
     */
    protected String firstName;
    protected String lastName;
    protected String middleName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof People) {
            People objEqualing = (People) obj;
            return (super.equals(obj)
                    && firstName.equals(objEqualing.firstName)
                    && lastName.equals(objEqualing.lastName)
                    && middleName.equals(objEqualing.middleName));
        }
        return false;
    }

    @Override
    public String toString() {
        return lastName + " " + firstName + " " + middleName;
    }
}

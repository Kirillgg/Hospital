package hospital.model;

import java.util.Objects;

/**
 * Entity mapping table Priority.
 *
 * @author Kirill
 */
public class Priority extends Base {

    private String name;

    public Priority() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Priority) {
            Priority objEqualing = (Priority) obj;
            return (super.equals(obj) && this.name.equals(objEqualing.name));
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public String toString() {
        return name;
    }
}

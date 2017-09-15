package hospital.model;

/**
 * Class contains one field ID.
 * All entities of the database extend this class.
 * @author Kirill
 */

abstract public class Base {
    
    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Base) {
            Base objEqualing = (Base) obj;
            return this.id == objEqualing.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

}

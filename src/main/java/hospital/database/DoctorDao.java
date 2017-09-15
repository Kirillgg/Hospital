package hospital.database;

import hospital.model.Doctor;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

/**
 * DAO class for Doctor. Realize method getAll() and getById(). Overrides the
 * deletion method to handle the error.
 *
 * @author Kirill
 */
public class DoctorDao extends Dao<Doctor> {

    /**
     * method for remove doctor in DB. Throws error if we remove doctor who has
     * recipes.
     *
     * @param doctor - the entity to be removed
     */
    @Override
    public void delete(Doctor doctor) {
        try {
            super.delete(doctor);
        } catch (ConstraintViolationException ex) {
            throw new ForeignKeyError("You can't remove the doctor who has recipes");
        }
    }

    /**
     * method getting all doctors from DB.
     *
     * @return List doctors
     */
    @Override
    public List<Doctor> getAll() {
        return transaction((Session session) -> {
            return session.createSQLQuery("select * from doctor").addEntity(Doctor.class).list();
        });
    }

    /**
     * method getting doctor with certain ID if exists in DB
     *
     * @param id - ID doctors
     * @return Doctor or null
     */
    public static Doctor getById(long id) {
        return transaction((Session session) -> {
            return (Doctor) (session.get(Doctor.class, id));
        });
    }
}

package hospital.database;

import hospital.model.Patient;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;

/**
 * DAO class for Patient. Realize method getAll() and getById(). Overrides the
 * deletion method to handle the error.
 *
 * @author Kirill
 */
public class PatientDao extends Dao<Patient> {

    /**
     * method for remove patient in DB. Throws error if we remove patient who
     * has recipes.
     *
     * @param patient - the entity to be removed
     */
    @Override
    public void delete(Patient patient) {
        try {
            super.delete(patient);
        } catch (ConstraintViolationException ex) {
            throw new ForeignKeyError("You can't remove the patient who has recipes");
        }
    }

    /**
     * method getting all patients from DB.
     *
     * @return List patients
     */
    @Override
    public List<Patient> getAll() {
        return transaction((Session session) -> {
            return session.createSQLQuery("select * from patient").addEntity(Patient.class).list();
        });
    }

    /**
     * method getting patient with certain ID if exists in DB
     *
     * @param id - ID patients
     * @return Patient or null
     */
    public static Patient getById(long id) {
        return transaction((Session session) -> {
            return (Patient) (session.get(Patient.class, id));
        });
    }
}

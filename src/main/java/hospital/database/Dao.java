package hospital.database;

import hospital.model.Base;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * Base DAO class than contains realization save/update/delete/transactional.
 * All entities of the database extend this class.
 *
 * @author Kirill
 * @param <TypeBean> - This is the type bean used in extending classes
 * DoctorDao/PatientDao/RecipeDao for selective overriding methods
 */
abstract public class Dao<TypeBean extends Base> {

    /**
     * sessionFactory - hibernate object that providing session/transaction for
     * work with database
     */
    protected static final SessionFactory sessionFactory;

    /**
     * initializing sessiongFactory
     */
    static {
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        } catch (HibernateException ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * interface that you need implement to pass in the transaction
     *
     * @param <T> - return value
     */
    protected interface Command<T> {

        T process(Session session);
    }

    /**
     * method that before the operation with the database open a connection and
     * transaction, and after closes
     *
     * @param <T> - return value
     * @param command - operation for execute in transaction
     * @return
     */
    protected static <T> T transaction(final Command<T> command) {
        final Session session = sessionFactory.openSession();
        final Transaction tx = session.beginTransaction();
        T result = null;
        try {
            result = command.process(session);
            tx.commit();
        } finally {
            session.close();
        }
        return result;
    }

    /**
     * save entity in DB
     *
     * @param entity - entity for save
     * @return received id
     */
    public long save(TypeBean entity) {
        return transaction((Session session) -> {
            session.save(entity);
            return entity.getId();
        });
    }

    /**
     * update entity in DB
     *
     * @param entity - entity for update
     */
    public void update(TypeBean entity) {
        transaction((Session session) -> {
            session.update(entity);
            return null;
        });
    }

    /**
     * delete entity in DB
     *
     * @param entity - entity for delete
     */
    public void delete(TypeBean entity) {
        transaction((Session session) -> {
            session.delete(entity);
            return null;
        });
    }

    /**
     * save entity in DB if not exists id or update existing entity
     *
     * @param entity - entity for save/update
     */
    public void saveOrUpdate(TypeBean entity) {
        transaction((Session session) -> {
            session.saveOrUpdate(entity);
            return null;
        });
    }

    /**
     * method returns all objects of the table
     *
     * @return all objects of the table <TypeBean>
     */
    abstract List<TypeBean> getAll();

}

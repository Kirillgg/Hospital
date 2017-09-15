package hospital.database;

import hospital.model.Priority;
import hospital.model.Recipe;
import java.util.List;
import org.hibernate.Session;

/**
 * DAO class for Recipe. Realize method getAll(), getById(), getAllPriority().
 *
 * @author Kirill
 */
public class RecipeDao extends Dao<Recipe> {

    /**
     * method getting all recipes from DB.
     *
     * @return List recipes
     */
    @Override
    public List<Recipe> getAll() {
        return transaction((Session session) -> {
            return session.createSQLQuery("select * from recipe").addEntity(Recipe.class).list();
        });
    }

    /**
     * method getting recipe with certain ID if exists in DB.
     *
     * @param id - ID recipes
     * @return Recipe or null
     */
    public static Recipe getById(long id) {
        return transaction((Session session) -> {
            return (Recipe) (session.get(Recipe.class, id));
        });
    }

    /**
     * method getting all possible priorities of the recipe.
     *
     * @return List priorities
     */
    public static List<Priority> getAllPriority() {
        return transaction((Session session) -> {
            return session.createSQLQuery("select * from priority").addEntity(Priority.class).list();
        });
    }
}

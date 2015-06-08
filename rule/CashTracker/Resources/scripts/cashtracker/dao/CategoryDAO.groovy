package cashtracker.dao;

import java.util.List;

import cashtracker.model.Category;


public interface CategoryDAO {

	List <Category> findAll();

	Category findById(long id);

	int addCategory(Category c);

	void updateCategory(Category c);

	void deleteCategory(Category c);
}

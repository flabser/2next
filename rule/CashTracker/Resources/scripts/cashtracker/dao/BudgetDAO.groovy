package cashtracker.dao;

import java.util.List;

import cashtracker.model.Budget;


public interface BudgetDAO {

	List <Budget> findAll();

	Budget findById(long id);

	int addBudget(Budget b);

	void updateBudget(Budget b);

	void deleteBudget(Budget b);
}

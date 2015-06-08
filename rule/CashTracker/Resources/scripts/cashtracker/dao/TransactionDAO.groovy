package cashtracker.dao;

import java.util.List;

import cashtracker.model.Account;
import cashtracker.model.Category;
import cashtracker.model.CostCenter;
import cashtracker.model.Transaction;


public interface TransactionDAO {

	Transaction findById(long id);

	List <Transaction> findAll();

	List <Transaction> findAllByAccount(Account a);

	List <Transaction> findAllByCategory(Category c);

	List <Transaction> findAllByCostCenter(CostCenter cc);

	int addTransaction(Transaction t);

	void updateTransaction(Transaction t);

	void deleteTransaction(Transaction t);
}

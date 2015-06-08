package cashtracker.dao;

import java.util.List;

import cashtracker.model.Account;


public interface AccountDAO {

	List <Account> findAll();

	Account findById(long id);

	int addAccount(Account a);

	void updateAccount(Account a);

	void deleteAccount(Account a);
}

package cashtracker.dao;

import java.util.List;

import cashtracker.model.CostCenter;


public interface CostCenterDAO {

	List <CostCenter> findAll();

	CostCenter findById(long id);

	int addCostCenter(CostCenter cc);

	void updateCostCenter(CostCenter cc);

	void deleteCostCenter(CostCenter cc);
}

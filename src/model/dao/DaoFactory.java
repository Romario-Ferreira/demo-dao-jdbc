package model.dao;

import db.DB;
import model.dao.impl.DepartmentDaoJDBC;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {
	
	public static SellerDao instanciateSellerDao() {
		return new SellerDaoJDBC(DB.getConnection());
	}

	public static DepartmentDao instanciateDepartmentDao() {
		return new DepartmentDaoJDBC(DB.getConnection());
	}
	
	
}
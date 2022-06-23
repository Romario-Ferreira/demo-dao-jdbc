package application;

import java.sql.Connection;
import java.util.List;

import db.DB;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		Connection conn = DB.getConnection();
		SellerDao sellerDao = DaoFactory.instanciateSellerDao();
		DepartmentDao depDao = DaoFactory.instanciateDepartmentDao();
		Seller seller;
		Department dep;

		System.out.println("Banco de dados conectado");
		
		System.out.println();
		System.out.println("===== TESTE 1 FindById =====");
		seller = sellerDao.findById(3);
		dep = depDao.findById(3);
		System.out.println(seller);
		System.out.println(dep);
		
		System.out.println();
		System.out.println("===== TESTE 2 FindByDepartment =====");
		List<Seller> sellerList = sellerDao.findByDepartment(new Department(4, null));
		for (Seller s : sellerList) {
			System.out.println(s);
		}
		
	}
}

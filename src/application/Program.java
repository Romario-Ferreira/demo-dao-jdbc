package application;

import java.sql.Connection;
import java.util.Date;
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
		
		System.out.println();
		System.out.println("===== TESTE 3 FindAll =====");
		sellerList = sellerDao.findAll();
		for (Seller s : sellerList) {
			System.out.println(s);
		}
		List<Department> depList = depDao.findAll();
		for (Department d : depList) {
			System.out.println(d);
		}
		
		System.out.println();
		System.out.println("===== TESTE 4 Insert =====");
		depDao.insert(dep = new Department(null, "DevJr"));
		System.out.println("New department included, id = " + dep.getId());

		sellerDao.insert(seller = new Seller(null, "Sampaio", "sampaio@gmail.com", new Date(),3850.00, dep));
		System.out.println("New seller included, id = " + seller.getId());
		
		System.out.println();
		System.out.println("===== TESTE 5 Update =====");
		dep.setName("Dev Pleno");
		depDao.update(dep);
		System.out.println("Departament name updated to: "+ dep.getName());
		seller.setName("Victor");
		sellerDao.update(seller);
		System.out.println("Seller name updated to: " + seller.getName());
		
		System.out.println();
		System.out.println("===== TESTE 6 DeleteById =====");
		sellerDao.deleteById(9);
		System.out.println("Seller delete successfull");
		depDao.deleteById(8);
		System.out.println("Department has been delete");
		
		
		DB.closeConnection();
	}
}

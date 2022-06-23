package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{

	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) {
		this.conn=conn;	
	}
	
	@Override
	public void insert(Seller seller) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Seller seller) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "WHERE seller.Id = ?");
			ps.setInt(1, id);
			rs = ps.executeQuery();
			if(rs.next()) {
			Department dep = instanciateDepartment(rs);
			Seller seller = instanciateSeller(rs, dep);
			
			return seller;
			}
			else {
				return null;
			}
			}
			catch(SQLException e) {
				throw new DbException(e.getMessage());
			}
			finally {
				DB.closeResultSet(rs);
				DB.closeStatement(ps);
			}
	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = conn.prepareStatement(
						"SELECT seller.*,department.Name as DepName "
						+ "FROM seller INNER JOIN department "
						+ "ON seller.DepartmentId = department.Id "
						+ "WHERE DepartmentId = ? "
						+ "ORDER By Name");
				ps.setInt(1, department.getId());
				rs = ps.executeQuery();
				
				List<Seller> listSeller = new ArrayList<Seller>();
				Map<Integer, Department> map = new HashMap<>();

				while(rs.next()) {
					
					Department dep = map.get(rs.getInt("DepartmentId"));
					
					if(dep == null) {
						
						map.put(rs.getInt("DepartmentId"), dep);	
					}
					
					Seller seller = instanciateSeller(rs, dep);
					listSeller.add(seller);	
				}
					return listSeller;
				}
				catch(SQLException e) {
					throw new DbException(e.getMessage());
				}
				finally {
					DB.closeResultSet(rs);
					DB.closeStatement(ps);
				}
	}

	private Seller instanciateSeller (ResultSet rs, Department department) throws SQLException {
			Seller seller = new Seller();
			seller.setId(rs.getInt(1));
			seller.setName(rs.getString(2));
			seller.setEmail(rs.getString(3));
			seller.setBirthDate(rs.getDate(4));
			seller.setBaseSalary(rs.getDouble(5));
			seller.setDepartment(instanciateDepartment(rs));
			return seller;
	}
	
	private Department instanciateDepartment(ResultSet rs) throws SQLException {
			Department department = new Department();
			department.setId(rs.getInt("DepartmentId"));
			department.setName(rs.getString("DepName"));
			return department;
	}
}

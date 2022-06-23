package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?) ",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, seller.getName());
			ps.setString(2, seller.getEmail());
			ps.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
			ps.setDouble(4, seller.getBaseSalary());
			ps.setInt(5, seller.getDepartment().getId());
			
			int row = ps.executeUpdate();
			
			if (row>0) {
				ResultSet rs = ps.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					seller.setId(id);
				}		
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Error inexpected ! No rows affected");
			}
			}
			catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
			finally {
				DB.closeStatement(ps);
			}
	}

	@Override
	public void update(Seller seller) {
		
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(
							"UPDATE seller "
							+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
							+ "WHERE Id = ?");
			ps.setString(1, seller.getName());
			ps.setString(2, seller.getEmail());
			ps.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
			ps.setDouble(4, seller.getBaseSalary());
			ps.setInt(5, seller.getDepartment().getId());
			ps.setInt(6, seller.getId());
			
			ps.executeUpdate();	
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
		}
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
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "ORDER BY Name");
			
			rs = ps.executeQuery();
			List<Seller> sellerList = new ArrayList<Seller>();
			Map<Integer, Department> map = new HashMap<>();
			
			while(rs.next()) {
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if(dep == null) {
					dep = instanciateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				Seller seller = instanciateSeller(rs, dep);
				sellerList.add(seller);
			}
			return sellerList;
		}
		catch(SQLException e ) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(ps);		
		}
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

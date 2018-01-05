package com.ashish.jdbc.batch.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;

import javax.sql.DataSource;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ashish.jdbc.batch.domain.BatchFilepojo;

@Service
public class Persister {

	private Connection connection;
	private String query;
	private SimpleDateFormat format = new SimpleDateFormat("YYYYMMDD");
	@Autowired
	public Persister(DataSource dataSource) {
		try {
			this.connection = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.query = "INSERT INTO TEST_TABLE "
				+ "(ID, MSD1, DOB, MSD2, MSD3, MSD4, MSD5, FLAG) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	}


	public void persist(List<BatchFilepojo> list) throws SQLException {
		PreparedStatement ps = connection.prepareStatement(query);	
		list.stream().forEach(filePojo -> {
			try {
				
				ps.setInt(1, filePojo.getId());
				ps.setString(2, filePojo.getMsd1());
				ps.setDate(3, new Date(format.parse(filePojo.getDob()).getTime()));
				ps.setString(4, filePojo.getMsd2());
				ps.setString(5, filePojo.getMsd3());
				ps.setString(6, filePojo.getMsd4());
				ps.setString(7, filePojo.getMsd5());
				ps.setBoolean(8,  filePojo.getFlag().equals("1")?true:false);
				ps.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		//ps.executeBatch();
	
	}
}

package com.ingleash.multithreaded.dbupdate.domain;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "TEST_TABLE")
public class TestTable implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	private int id;
	@Column
	private String msd1;
	@Column
	private LocalDate dob;
	@Column
	private String msd2;
	@Column
	private String msd3;
	@Column
	private String msd4;
	@Column
	private String msd5;
	@Column
	private boolean flag;

	
	public TestTable(int id, String msd1, LocalDate dob, String msd2, String msd3, String msd4, String msd5,
			boolean flag) {
		this.id = id;
		this.msd1 = msd1;
		this.dob = dob;
		this.msd2 = msd2;
		this.msd3 = msd3;
		this.msd4 = msd4;
		this.msd5 = msd5;
		this.flag = flag;
	}

	public TestTable() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMsd1() {
		return msd1;
	}

	public void setMsd1(String msd1) {
		this.msd1 = msd1;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getMsd2() {
		return msd2;
	}

	public void setMsd2(String msd2) {
		this.msd2 = msd2;
	}

	public String getMsd3() {
		return msd3;
	}

	public void setMsd3(String msd3) {
		this.msd3 = msd3;
	}

	public String getMsd4() {
		return msd4;
	}

	public void setMsd4(String msd4) {
		this.msd4 = msd4;
	}

	public String getMsd5() {
		return msd5;
	}

	public void setMsd5(String msd5) {
		this.msd5 = msd5;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}

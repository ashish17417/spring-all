package com.ashish.jdbc.batch.domain;

import java.time.LocalDate;

public class BatchFilepojo {

	private int id;
	private String msd1;
	private String dob;
	private String msd2;
	private String msd3;
	private String msd4;
	private String msd5;
	private String flag;

	public BatchFilepojo(int id, String msd1, String dob, String msd2, String msd3, String msd4, String msd5,
			String flag) {
		super();
		this.id = id;
		this.msd1 = msd1;
		this.dob = dob;
		this.msd2 = msd2;
		this.msd3 = msd3;
		this.msd4 = msd4;
		this.msd5 = msd5;
		this.flag = flag;
	}

	@Override
	public String toString() {
		return "BatchFilepojo [id=" + id + ", msd1=" + msd1 + ", dob=" + dob + ", msd2=" + msd2 + ", msd3=" + msd3
				+ ", msd4=" + msd4 + ", msd5=" + msd5 + ", flag=" + flag + "]";
	}

	public int getId() {
		return id;
	}

	public String getMsd1() {
		return msd1;
	}

	public String getDob() {
		return dob;
	}

	public String getMsd2() {
		return msd2;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setMsd1(String msd1) {
		this.msd1 = msd1;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public void setMsd2(String msd2) {
		this.msd2 = msd2;
	}

	public void setMsd3(String msd3) {
		this.msd3 = msd3;
	}

	public void setMsd4(String msd4) {
		this.msd4 = msd4;
	}

	public void setMsd5(String msd5) {
		this.msd5 = msd5;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public BatchFilepojo() {
	}

	public String getMsd3() {
		return msd3;
	}

	public String getMsd4() {
		return msd4;
	}

	public String getMsd5() {
		return msd5;
	}

	public String getFlag() {
		return flag;
	}

}

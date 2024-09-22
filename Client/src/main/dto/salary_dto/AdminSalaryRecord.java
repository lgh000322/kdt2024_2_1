package main.dto.salary_dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AdminSalaryRecord {

	private final SimpleLongProperty salaryNum;
	private final SimpleStringProperty salaryName;
	private final SimpleStringProperty salaryDept;
	private final SimpleStringProperty salaryPosition;
	private final SimpleIntegerProperty basicSalary;
	private final SimpleLongProperty keyUserNum;
	


	// 생성자
	public AdminSalaryRecord(Long salaryNum, Long keyUserNum, String salaryName, String salaryDept,
			String salaryPosition, int basicSalary) {

		this.salaryNum = new SimpleLongProperty(salaryNum);
		this.salaryName = new SimpleStringProperty(salaryName);
		this.salaryDept = new SimpleStringProperty(salaryDept);
		this.salaryPosition = new SimpleStringProperty(salaryPosition);
		this.keyUserNum = new SimpleLongProperty(keyUserNum);
		this.basicSalary = new SimpleIntegerProperty(basicSalary);
	
	}

	// Property 반환 메서드

	public LongProperty salaryNumProperty() {
		return salaryNum;
	}

	public StringProperty salaryNameProperty() {
		return salaryName;
	}

	public StringProperty salaryDeptProperty() {
		return salaryDept;
	}

	public StringProperty salaryPositionProperty() {
		return salaryPosition;
	}

	// Getter methods

	public long getSalaryNum() {
		return salaryNum.get();
	}

	public void setSalaryNum(long salaryNum) {
		this.salaryNum.set(salaryNum);
	}

	
	public long getKeyUserNum() {
		return keyUserNum.get();
	}

	public void setKeyUserNum(long keyUserNum) {
		this.keyUserNum.set(keyUserNum);
	}

	public String getSalaryName() {
		return salaryName.get();
	}

	public void setSalaryName(String salaryName) {
		this.salaryName.set(salaryName);
	}

	public String getSalaryDept() {
		return salaryDept.get();
	}

	public void setSalaryDept(String salaryDept) {
		this.salaryDept.set(salaryDept);
	}

	public String getSalaryPosition() {
		return salaryPosition.get();
	}

	public void setSalaryPosition(String salaryPosition) {
		this.salaryPosition.set(salaryPosition);
	}

	public int getBasicSalary() {
		return this.basicSalary.get();
	}

	public void setBasicSalary(int basicSalary) {
		this.basicSalary.set(basicSalary);
	}
	
	
}

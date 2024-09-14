package main.dto.salary_dto;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AdminSalaryRecord {

	private final SimpleLongProperty keyNo;
	private final SimpleLongProperty salaryNum;
	private final SimpleStringProperty salaryName;
	private final SimpleStringProperty salaryPhone;
	private final SimpleStringProperty salaryDept;
	private final SimpleStringProperty salaryPosition;
	private final SimpleIntegerProperty salaryPayment;

	// 생성자
	public AdminSalaryRecord(Long keyNo, Long salaryNum, String salaryName, String salaryPhone, String salaryDept,
			String salaryPosition, Integer salaryPayment) {
		this.keyNo = new SimpleLongProperty(keyNo);
		this.salaryNum = new SimpleLongProperty(salaryNum);
		this.salaryName = new SimpleStringProperty(salaryName);
		this.salaryPhone = new SimpleStringProperty(salaryPhone);
		this.salaryDept = new SimpleStringProperty(salaryDept);
		this.salaryPosition = new SimpleStringProperty(salaryPosition);
		this.salaryPayment = new SimpleIntegerProperty(salaryPayment);
	}

	// Property 반환 메서드
	public LongProperty keyNoProperty() {
		return keyNo;
	}

	public LongProperty salaryNumProperty() {
		return salaryNum;
	}

	public StringProperty salaryNameProperty() {
		return salaryName;
	}

	public StringProperty salaryPhoneProperty() {
		return salaryPhone;
	}

	public StringProperty salaryDeptProperty() {
		return salaryDept;
	}

	public StringProperty salaryPositionProperty() {
		return salaryPosition;
	}

	public IntegerProperty salaryPaymentProperty() {
		return salaryPayment;
	}

	// Getter methods
	public long getKeyNo() {
		return keyNo.get();
	}

	public void setKeyNo(long keyNo) {
		this.keyNo.set(keyNo);
	}

	public long getSalaryNum() {
		return salaryNum.get();
	}

	public void setSalaryNum(long salaryNum) {
		this.salaryNum.set(salaryNum);
	}

	public String getSalaryName() {
		return salaryName.get();
	}

	public void setSalaryName(String salaryName) {
		this.salaryName.set(salaryName);
	}

	public String getSalaryPhone() {
		return salaryPhone.get();
	}

	public void setSalaryPhone(String salaryPhone) {
		this.salaryPhone.set(salaryPhone);
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

	public int getSalaryPayment() {
		return salaryPayment.get();
	}

	public void setSalaryPayment(int salaryPayment) {
		this.salaryPayment.set(salaryPayment);
	}
}

package main.dto.salary_dto;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class SalaryRecord {

	private final SimpleLongProperty salaryNo;
	private final SimpleStringProperty salaryReceived;
	private final SimpleIntegerProperty salaryTotal;

	public SalaryRecord(Long salaryNo, String salaryReceived, Integer salaryTotal) {
		this.salaryNo = new SimpleLongProperty(salaryNo);
		this.salaryReceived = new SimpleStringProperty(salaryReceived);
		this.salaryTotal = new SimpleIntegerProperty(salaryTotal);
	}

	// Getters
	public Long getSalaryNo() {
		return salaryNo.get();
	}

	public String getSalaryReceived() {
		return salaryReceived.get();
	}

	public Integer getSalaryTotal() {
		return salaryTotal.get();
	}

	// Setters
	public void setSalaryNo(Long salaryNo) {
		this.salaryNo.set(salaryNo);
	}

	public void setSalaryReceived(String salaryReceived) {
		this.salaryReceived.set(salaryReceived);
	}

	public void setSalaryTotal(Integer salaryTotal) {
		this.salaryTotal.set(salaryTotal);
	}

	// Property getters for binding
	public SimpleLongProperty salaryNoProperty() {
		return salaryNo;
	}

	public SimpleStringProperty salaryReceivedProperty() {
		return salaryReceived;
	}

	public SimpleIntegerProperty salaryTotalProperty() {
		return salaryTotal;
	}
}

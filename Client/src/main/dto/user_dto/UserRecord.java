package main.dto.user_dto;

import java.time.LocalDate;
import java.time.LocalTime;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import main.domain.work_log.Status;

public class UserRecord {
	private final SimpleLongProperty num;
	private final SimpleStringProperty name;
	private final SimpleStringProperty tel;
	private final SimpleStringProperty dept;
	private final SimpleStringProperty position;
	private final SimpleStringProperty email;

	public UserRecord(Long num, String name, String tel,
			String dept, String position, String email) {
		super();
		this.num = new SimpleLongProperty(num);
		this.name = new SimpleStringProperty(name);
		this.tel = new SimpleStringProperty(tel);
		this.dept = new SimpleStringProperty(dept);
		this.position = new SimpleStringProperty(position);
		this.email = new SimpleStringProperty(email);
		
	}

	public SimpleLongProperty getNum() {
		return num;
	}

	public SimpleStringProperty getName() {
		return name;
	}

	public SimpleStringProperty getTel() {
		return tel;
	}

	public SimpleStringProperty getDept() {
		return dept;
	}

	public SimpleStringProperty getPosition() {
		return position;
	}

	public SimpleStringProperty getEmail() {
		return email;
	}

}

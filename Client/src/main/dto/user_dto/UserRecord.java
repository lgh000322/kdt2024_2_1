package main.dto.user_dto;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.StringProperty;

public class UserRecord {
    private final SimpleLongProperty num;
    private final SimpleStringProperty name;
    private final SimpleStringProperty tel;
    private final SimpleStringProperty dept;
    private final SimpleStringProperty position;
    private final SimpleStringProperty email;

    // 생성자
    public UserRecord(Long num, String name, String tel, String dept, String position, String email) {
        this.num = new SimpleLongProperty(num);
        this.name = new SimpleStringProperty(name);
        this.tel = new SimpleStringProperty(tel);
        this.dept = new SimpleStringProperty(dept);
        this.position = new SimpleStringProperty(position);
        this.email = new SimpleStringProperty(email);
    }

    // Property 반환 메서드
    public LongProperty numProperty() {
        return num;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty telProperty() {
        return tel;
    }

    public StringProperty deptProperty() {
        return dept;
    }

    public StringProperty positionProperty() {
        return position;
    }

    public StringProperty emailProperty() {
        return email;
    }

    // Getter methods
    public long getNum() {
        return num.get();
    }

    public void setNum(long num) {
        this.num.set(num);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getTel() {
        return tel.get();
    }

    public void setTel(String tel) {
        this.tel.set(tel);
    }

    public String getDept() {
        return dept.get();
    }

    public void setDept(String dept) {
        this.dept.set(dept);
    }

    public String getPosition() {
        return position.get();
    }

    public void setPosition(String position) {
        this.position.set(position);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }
}

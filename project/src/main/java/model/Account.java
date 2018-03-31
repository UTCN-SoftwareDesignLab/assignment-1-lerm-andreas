package model;

import java.util.Date;

public class Account {
    private Long id;

    private String type;
    private Long amountOfMoney;
    private Date dateOfCreation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getAmountOfMoney() {
        return amountOfMoney;
    }

    public void setAmountOfMoney(Long amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

}

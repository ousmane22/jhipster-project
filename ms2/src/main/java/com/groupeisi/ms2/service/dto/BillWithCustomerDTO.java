package com.groupeisi.ms2.service.dto;

import java.time.LocalDate;

public class BillWithCustomerDTO {
    private Long id;
    private LocalDate billDate;
    private Double amount;

    private Long customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;

    // Getters and Setters

    public Long getId(Long id) {
        return this.id;
    }

    public void setId(Long billId) {
        this.id = billId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate dueDate) {
        this.billDate = dueDate;
    }


    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
}

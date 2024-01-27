package com.receipt;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Introspected
@Serdeable
public class Receipt {
    private String retailer;
    private String purchaseDate;
    private String purchaseTime;
    private Item[] items;
    private String total;

    public void setRetailer(String retailer){
        this.retailer = retailer;
    }

    public void setPurchaseDate(String purchaseDate){
        this.purchaseDate = purchaseDate;
    }

    public void setPurchaseTime(String purchaseTime){
        this.purchaseTime = purchaseTime;
    }

    public void setItems(Item[] items){
        this.items = items;
    }

    public void setTotal(String total){
        this.total = total;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public String getPurchaseTime() {
        return purchaseTime;
    }

    public Item[] getItems() {
        return items;
    }

    public String getRetailer() {
        return retailer;
    }

    public String getTotal() {
        return total;
    }
}



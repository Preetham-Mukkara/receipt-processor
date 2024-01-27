package com.receipt;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Introspected
@Serdeable
public class Receipt {
    @NonNull
    private String retailer;
    @NotNull
    private String purchaseDate;
    @NotNull
    private String purchaseTime;
    @NotNull
    @Valid
    private Item[] items;
    @NotNull
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



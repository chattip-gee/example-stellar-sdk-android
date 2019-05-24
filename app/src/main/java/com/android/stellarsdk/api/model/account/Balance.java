
package com.android.stellarsdk.api.model.account;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Balance {

    @SerializedName("balance")
    @Expose
    private String balance;
    @SerializedName("buying_liabilities")
    @Expose
    private String buyingLiabilities;
    @SerializedName("selling_liabilities")
    @Expose
    private String sellingLiabilities;
    @SerializedName("asset_type")
    @Expose
    private String assetType;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBuyingLiabilities() {
        return buyingLiabilities;
    }

    public void setBuyingLiabilities(String buyingLiabilities) {
        this.buyingLiabilities = buyingLiabilities;
    }

    public String getSellingLiabilities() {
        return sellingLiabilities;
    }

    public void setSellingLiabilities(String sellingLiabilities) {
        this.sellingLiabilities = sellingLiabilities;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

}

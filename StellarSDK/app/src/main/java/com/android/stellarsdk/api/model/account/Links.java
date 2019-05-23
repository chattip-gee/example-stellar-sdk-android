
package com.android.stellarsdk.api.model.account;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links {

    @SerializedName("self")
    @Expose
    private Self self;
    @SerializedName("transactions")
    @Expose
    private Transactions transactions;
    @SerializedName("operations")
    @Expose
    private Operations operations;
    @SerializedName("payments")
    @Expose
    private Payments payments;
    @SerializedName("effects")
    @Expose
    private Effects effects;
    @SerializedName("offers")
    @Expose
    private Offers offers;
    @SerializedName("trades")
    @Expose
    private Trades trades;
    @SerializedName("data")
    @Expose
    private Data data;

    public Self getSelf() {
        return self;
    }

    public void setSelf(Self self) {
        this.self = self;
    }

    public Transactions getTransactions() {
        return transactions;
    }

    public void setTransactions(Transactions transactions) {
        this.transactions = transactions;
    }

    public Operations getOperations() {
        return operations;
    }

    public void setOperations(Operations operations) {
        this.operations = operations;
    }

    public Payments getPayments() {
        return payments;
    }

    public void setPayments(Payments payments) {
        this.payments = payments;
    }

    public Effects getEffects() {
        return effects;
    }

    public void setEffects(Effects effects) {
        this.effects = effects;
    }

    public Offers getOffers() {
        return offers;
    }

    public void setOffers(Offers offers) {
        this.offers = offers;
    }

    public Trades getTrades() {
        return trades;
    }

    public void setTrades(Trades trades) {
        this.trades = trades;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}


package com.android.stellarsdk.api.model.transaction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Links {

    @SerializedName("self")
    @Expose
    private Self self;
    @SerializedName("account")
    @Expose
    private Account account;
    @SerializedName("ledger")
    @Expose
    private Ledger ledger;
    @SerializedName("operations")
    @Expose
    private Operations operations;
    @SerializedName("effects")
    @Expose
    private Effects effects;
    @SerializedName("precedes")
    @Expose
    private Precedes precedes;
    @SerializedName("succeeds")
    @Expose
    private Succeeds succeeds;

    public Self getSelf() {
        return self;
    }

    public void setSelf(Self self) {
        this.self = self;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Ledger getLedger() {
        return ledger;
    }

    public void setLedger(Ledger ledger) {
        this.ledger = ledger;
    }

    public Operations getOperations() {
        return operations;
    }

    public void setOperations(Operations operations) {
        this.operations = operations;
    }

    public Effects getEffects() {
        return effects;
    }

    public void setEffects(Effects effects) {
        this.effects = effects;
    }

    public Precedes getPrecedes() {
        return precedes;
    }

    public void setPrecedes(Precedes precedes) {
        this.precedes = precedes;
    }

    public Succeeds getSucceeds() {
        return succeeds;
    }

    public void setSucceeds(Succeeds succeeds) {
        this.succeeds = succeeds;
    }

}

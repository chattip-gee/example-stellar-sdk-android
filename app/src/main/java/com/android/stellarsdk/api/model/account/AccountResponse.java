
package com.android.stellarsdk.api.model.account;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AccountResponse {

    @SerializedName("_links")
    @Expose
    private Links links;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("paging_token")
    @Expose
    private String pagingToken;
    @SerializedName("account_id")
    @Expose
    private String accountId;
    @SerializedName("sequence")
    @Expose
    private String sequence;
    @SerializedName("subentry_count")
    @Expose
    private Integer subentryCount;
    @SerializedName("last_modified_ledger")
    @Expose
    private Integer lastModifiedLedger;
    @SerializedName("thresholds")
    @Expose
    private Thresholds thresholds;
    @SerializedName("flags")
    @Expose
    private Flags flags;
    @SerializedName("balances")
    @Expose
    private List<Balance> balances = null;
    @SerializedName("signers")
    @Expose
    private List<Signer> signers = null;
    @SerializedName("data")
    @Expose
    private Data_ data;

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPagingToken() {
        return pagingToken;
    }

    public void setPagingToken(String pagingToken) {
        this.pagingToken = pagingToken;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public Integer getSubentryCount() {
        return subentryCount;
    }

    public void setSubentryCount(Integer subentryCount) {
        this.subentryCount = subentryCount;
    }

    public Integer getLastModifiedLedger() {
        return lastModifiedLedger;
    }

    public void setLastModifiedLedger(Integer lastModifiedLedger) {
        this.lastModifiedLedger = lastModifiedLedger;
    }

    public Thresholds getThresholds() {
        return thresholds;
    }

    public void setThresholds(Thresholds thresholds) {
        this.thresholds = thresholds;
    }

    public Flags getFlags() {
        return flags;
    }

    public void setFlags(Flags flags) {
        this.flags = flags;
    }

    public List<Balance> getBalances() {
        return balances;
    }

    public void setBalances(List<Balance> balances) {
        this.balances = balances;
    }

    public List<Signer> getSigners() {
        return signers;
    }

    public void setSigners(List<Signer> signers) {
        this.signers = signers;
    }

    public Data_ getData() {
        return data;
    }

    public void setData(Data_ data) {
        this.data = data;
    }

}

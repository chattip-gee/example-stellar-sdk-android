
package com.android.stellarsdk.api.model.transaction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionResponse {

    @SerializedName("_links")
    @Expose
    private Links links;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("paging_token")
    @Expose
    private String pagingToken;
    @SerializedName("successful")
    @Expose
    private Boolean successful;
    @SerializedName("hash")
    @Expose
    private String hash;
    @SerializedName("ledger")
    @Expose
    private Integer ledger;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("source_account")
    @Expose
    private String sourceAccount;
    @SerializedName("source_account_sequence")
    @Expose
    private String sourceAccountSequence;
    @SerializedName("fee_paid")
    @Expose
    private Integer feePaid;
    @SerializedName("operation_count")
    @Expose
    private Integer operationCount;
    @SerializedName("envelope_xdr")
    @Expose
    private String envelopeXdr;
    @SerializedName("result_xdr")
    @Expose
    private String resultXdr;
    @SerializedName("result_meta_xdr")
    @Expose
    private String resultMetaXdr;
    @SerializedName("fee_meta_xdr")
    @Expose
    private String feeMetaXdr;
    @SerializedName("memo_type")
    @Expose
    private String memoType;
    @SerializedName("signatures")
    @Expose
    private List<String> signatures = null;
    @SerializedName("valid_after")
    @Expose
    private String validAfter;

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

    public Boolean getSuccessful() {
        return successful;
    }

    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Integer getLedger() {
        return ledger;
    }

    public void setLedger(Integer ledger) {
        this.ledger = ledger;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public String getSourceAccountSequence() {
        return sourceAccountSequence;
    }

    public void setSourceAccountSequence(String sourceAccountSequence) {
        this.sourceAccountSequence = sourceAccountSequence;
    }

    public Integer getFeePaid() {
        return feePaid;
    }

    public void setFeePaid(Integer feePaid) {
        this.feePaid = feePaid;
    }

    public Integer getOperationCount() {
        return operationCount;
    }

    public void setOperationCount(Integer operationCount) {
        this.operationCount = operationCount;
    }

    public String getEnvelopeXdr() {
        return envelopeXdr;
    }

    public void setEnvelopeXdr(String envelopeXdr) {
        this.envelopeXdr = envelopeXdr;
    }

    public String getResultXdr() {
        return resultXdr;
    }

    public void setResultXdr(String resultXdr) {
        this.resultXdr = resultXdr;
    }

    public String getResultMetaXdr() {
        return resultMetaXdr;
    }

    public void setResultMetaXdr(String resultMetaXdr) {
        this.resultMetaXdr = resultMetaXdr;
    }

    public String getFeeMetaXdr() {
        return feeMetaXdr;
    }

    public void setFeeMetaXdr(String feeMetaXdr) {
        this.feeMetaXdr = feeMetaXdr;
    }

    public String getMemoType() {
        return memoType;
    }

    public void setMemoType(String memoType) {
        this.memoType = memoType;
    }

    public List<String> getSignatures() {
        return signatures;
    }

    public void setSignatures(List<String> signatures) {
        this.signatures = signatures;
    }

    public String getValidAfter() {
        return validAfter;
    }

    public void setValidAfter(String validAfter) {
        this.validAfter = validAfter;
    }

}

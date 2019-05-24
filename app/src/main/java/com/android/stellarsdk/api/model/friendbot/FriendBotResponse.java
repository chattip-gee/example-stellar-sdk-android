
package com.android.stellarsdk.api.model.friendbot;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FriendBotResponse {

    @SerializedName("_links")
    @Expose
    private Links links;
    @SerializedName("hash")
    @Expose
    private String hash;
    @SerializedName("ledger")
    @Expose
    private Integer ledger;
    @SerializedName("envelope_xdr")
    @Expose
    private String envelopeXdr;
    @SerializedName("result_xdr")
    @Expose
    private String resultXdr;
    @SerializedName("result_meta_xdr")
    @Expose
    private String resultMetaXdr;

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
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

}

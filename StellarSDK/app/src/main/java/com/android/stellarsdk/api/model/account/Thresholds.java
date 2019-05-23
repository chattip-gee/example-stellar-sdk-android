
package com.android.stellarsdk.api.model.account;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Thresholds {

    @SerializedName("low_threshold")
    @Expose
    private Integer lowThreshold;
    @SerializedName("med_threshold")
    @Expose
    private Integer medThreshold;
    @SerializedName("high_threshold")
    @Expose
    private Integer highThreshold;

    public Integer getLowThreshold() {
        return lowThreshold;
    }

    public void setLowThreshold(Integer lowThreshold) {
        this.lowThreshold = lowThreshold;
    }

    public Integer getMedThreshold() {
        return medThreshold;
    }

    public void setMedThreshold(Integer medThreshold) {
        this.medThreshold = medThreshold;
    }

    public Integer getHighThreshold() {
        return highThreshold;
    }

    public void setHighThreshold(Integer highThreshold) {
        this.highThreshold = highThreshold;
    }

}

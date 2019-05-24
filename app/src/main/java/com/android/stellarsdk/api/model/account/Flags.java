
package com.android.stellarsdk.api.model.account;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Flags {

    @SerializedName("auth_required")
    @Expose
    private Boolean authRequired;
    @SerializedName("auth_revocable")
    @Expose
    private Boolean authRevocable;
    @SerializedName("auth_immutable")
    @Expose
    private Boolean authImmutable;

    public Boolean getAuthRequired() {
        return authRequired;
    }

    public void setAuthRequired(Boolean authRequired) {
        this.authRequired = authRequired;
    }

    public Boolean getAuthRevocable() {
        return authRevocable;
    }

    public void setAuthRevocable(Boolean authRevocable) {
        this.authRevocable = authRevocable;
    }

    public Boolean getAuthImmutable() {
        return authImmutable;
    }

    public void setAuthImmutable(Boolean authImmutable) {
        this.authImmutable = authImmutable;
    }

}

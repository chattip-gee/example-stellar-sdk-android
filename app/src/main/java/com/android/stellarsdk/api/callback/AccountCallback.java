package com.android.stellarsdk.api.callback;

import com.android.stellarsdk.api.model.account.AccountResponse;

/**
 * Created by Sloth on 12/23/2017.
 */

public interface AccountCallback {
    void onApiError(String errorMessage);

    void onApiSuccess(AccountResponse accountResponse);
}

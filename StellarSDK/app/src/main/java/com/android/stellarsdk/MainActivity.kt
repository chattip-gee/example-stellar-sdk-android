package com.android.stellarsdk

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.stellarsdk.api.ApiManager
import com.android.stellarsdk.api.callback.AccountCallback
import com.android.stellarsdk.api.callback.BaseCallback
import com.android.stellarsdk.api.model.account.AccountResponse
import kotlinx.android.synthetic.main.activity_main.*
import org.stellar.sdk.KeyPair


class MainActivity : AppCompatActivity() {

    var pair: KeyPair? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_generate_keypair.setOnClickListener {
            val keyPair = KeyPair.random()
            pair = keyPair

            tv_public_key_id.text = pair!!.accountId
            tv_secret_key_id.text = String(pair!!.secretSeed)
            tv_account_id.text = pair!!.accountId
        }

        btn_get_test_network_lumens.setOnClickListener {
            ApiManager().getFriendBot(pair?.accountId, object : BaseCallback {
                override fun onApiError(errorMessage: String?) {
                    tv_result_friend_bot.text = errorMessage
                    cst_result_friendbot.visibility = View.VISIBLE
                }

                override fun onApiSuccess(success: String?) {
                    tv_result_friend_bot.text = success
                    cst_result_friendbot.visibility = View.VISIBLE
                }
            })
        }

        btn_get_account.setOnClickListener {
            tv_account_detail_id.text = "Balances for account " + pair?.accountId
            ApiManager().getAccount(pair?.accountId, object : AccountCallback {
                override fun onApiError(errorMessage: String?) {
                    tv_account_detail.text = errorMessage
                    cst_account_detail.visibility = View.VISIBLE
                }

                override fun onApiSuccess(accountResponse: AccountResponse?) {
                    for (balance in accountResponse!!.balances) {
                        tv_account_detail.text =
                            String.format("Type: %s, Balance: %s", balance.assetType, balance.balance)
                        cst_account_detail.visibility = View.VISIBLE
                    }
                }
            })
        }
    }

}

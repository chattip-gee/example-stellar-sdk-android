package com.android.stellarsdk

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.stellarsdk.api.callback.OnResponse
import com.android.stellarsdk.api.model.friendbot.FriendBotResponse
import com.android.stellarsdk.api.remote.Horizon
import com.android.stellarsdk.api.remote.ReceiverResponse
import com.android.stellarsdk.api.restapi.ApiManager
import kotlinx.android.synthetic.main.activity_main.*
import org.stellar.sdk.KeyPair
import org.stellar.sdk.responses.SubmitTransactionResponse


class MainActivity : AppCompatActivity() {

    var pair: KeyPair? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onClickGenerateKeyPair()

        onClickGetFriendbot()

        onClickGetAccounts()

        onClickSendMoney()

        onClickReceiveMoney()
    }

    private fun onClickReceiveMoney() {
        btn_receive_money.setOnClickListener {
            cst_result_receive.visibility = View.GONE
            pb_four.visibility = View.VISIBLE
            Horizon.doReceiveMoney(pair!!.accountId, object : OnResponse<ReceiverResponse> {
                override fun onError(error: String) {
                    pb_four.visibility = View.GONE
                    tv_result_receive.text = error
                    cst_result_receive.background =
                        ContextCompat.getDrawable(this@MainActivity, R.drawable.result_fail_background)
                    cst_result_receive.visibility = View.VISIBLE
                }

                override fun onSuccess(response: ReceiverResponse) {
                    pb_four.visibility = View.GONE
                    tv_result_receive.text = response.amount + " " + response.assetName + " from " + response.accountId
                    cst_result_receive.background =
                        ContextCompat.getDrawable(this@MainActivity, R.drawable.result_success_background)
                    cst_result_receive.visibility = View.VISIBLE
                }
            })
        }
    }

    private fun onClickSendMoney() {
        btn_send_money.setOnClickListener {
            cst_result_send.visibility = View.GONE
            pb_three.visibility = View.VISIBLE
            Horizon.doSendMoneyByAsset(
                "SCYNHADZEENTICMPLFSDAA4HGHMGXZSCOLY7APBPUJBPYZWEA4YF4JL2",
                pair!!.secretSeed,
                edt_send_memo.text.toString(),
                edt_send_amount.text.toString(),
                "GeeDollar",
                "1000",
                object : OnResponse<SubmitTransactionResponse> {
                    override fun onError(error: String) {
                        pb_three.visibility = View.GONE
                        tv_result_send.text = error
                        cst_result_send.background =
                            ContextCompat.getDrawable(this@MainActivity, R.drawable.result_fail_background)
                        cst_result_send.visibility = View.VISIBLE
                    }

                    override fun onSuccess(response: SubmitTransactionResponse) {
                        pb_three.visibility = View.GONE
                        tv_result_send.text = "SUCCESS! Has been sent account :)"
                        cst_result_send.background =
                            ContextCompat.getDrawable(this@MainActivity, R.drawable.result_success_background)
                        cst_result_send.visibility = View.VISIBLE
                    }
                })
        }
    }

    private fun onClickGetAccounts() {
        btn_get_account.setOnClickListener {
            cst_account_detail.visibility = View.GONE
            pb_two.visibility = View.VISIBLE
            tv_account_detail_id.text = "Balances for account \n" + pair?.accountId

            Horizon.getBalance(pair!!, object : OnResponse<org.stellar.sdk.responses.AccountResponse> {
                override fun onError(error: String) {
                    pb_two.visibility = View.GONE
                    tv_account_detail.text = error
                    cst_account_detail.background =
                        ContextCompat.getDrawable(this@MainActivity, R.drawable.result_fail_background)
                    cst_account_detail.visibility = View.VISIBLE
                }

                override fun onSuccess(response: org.stellar.sdk.responses.AccountResponse) {
                    for (balance in response.balances) {
                        pb_two.visibility = View.GONE
                        tv_account_detail.text =
                            String.format("Type: %s, Balance: %s", balance.assetType, balance.balance)
                        cst_account_detail.background =
                            ContextCompat.getDrawable(this@MainActivity, R.drawable.result_success_background)
                        cst_account_detail.visibility = View.VISIBLE
                    }
                }
            })
        }
    }

    private fun onClickGetFriendbot() {
        btn_get_test_network_lumens.setOnClickListener {
            cst_result_friendbot.visibility = View.GONE
            pb_one.visibility = View.VISIBLE
            ApiManager().getFriendBot(pair?.accountId, object : OnResponse<FriendBotResponse> {
                override fun onError(error: String) {
                    pb_one.visibility = View.GONE
                    tv_result_friend_bot.text = error
                    cst_result_friendbot.background =
                        ContextCompat.getDrawable(this@MainActivity, R.drawable.result_fail_background)
                    cst_result_friendbot.visibility = View.VISIBLE
                }

                override fun onSuccess(response: FriendBotResponse) {
                    pb_one.visibility = View.GONE
                    tv_result_friend_bot.text = "SUCCESS! You have a new account :)"
                    cst_result_friendbot.background =
                        ContextCompat.getDrawable(this@MainActivity, R.drawable.result_success_background)
                    cst_result_friendbot.visibility = View.VISIBLE
                }
            })
        }
    }

    private fun onClickGenerateKeyPair() {
        btn_generate_keypair.setOnClickListener {
            val keyPair = KeyPair.random()
            pair = keyPair

            tv_public_key_id.text = pair!!.accountId
            tv_secret_key_id.text = String(pair!!.secretSeed)
            tv_account_id.text = pair!!.accountId
        }
    }

}

package com.android.stellarsdk

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.stellarsdk.api.callback.OnResponse
import com.android.stellarsdk.api.model.friendbot.FriendBotResponse
import com.android.stellarsdk.api.remote.AddAssetItem
import com.android.stellarsdk.api.remote.Horizon
import com.android.stellarsdk.api.remote.ReceiverResponse
import com.android.stellarsdk.api.remote.TransactionItem
import com.android.stellarsdk.api.restapi.ApiManager
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.android.synthetic.main.activity_main.*
import org.stellar.sdk.KeyPair
import org.stellar.sdk.responses.SubmitTransactionResponse


class MainActivity : AppCompatActivity() {

    var pair: KeyPair? = null

    //TODO REFACTOR
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onClickGenerateKeyPair()

        onClickGetFriendbot()

        onClickGetAccounts()

        onClickSendMoney()

        onClickReceiveMoney()

        setDestination()

        onClickAddAsset()

    }

    private fun onClickAddAsset() {
        btn_add_asset.setOnClickListener {
            if (pair == null) Toast.makeText(applicationContext, "You don't have account", Toast.LENGTH_LONG).show()

            pair?.apply {
                if (isNonNativeAsset()) {
                    pb_five.visibility = View.VISIBLE
                    cst_result_add_asset.visibility = View.GONE

                    val item = AddAssetItem(secretKey = secretSeed, assetName = edt_asset_code.text.toString(), limit = edt_limit.text.toString())
                    Horizon.addAsset(item, object : OnResponse<SubmitTransactionResponse> {
                        override fun onError(error: String) {
                            pb_five.visibility = View.GONE
                            cst_result_add_asset.visibility = View.VISIBLE
                            tv_result_add_asset.text = error
                        }

                        override fun onSuccess(response: SubmitTransactionResponse) {
                            pb_five.visibility = View.GONE
                            cst_result_add_asset.visibility = View.VISIBLE
                            tv_result_add_asset.text = "SUCCESS! Added Asset"
                        }
                    })
                } else {
                    Toast.makeText(applicationContext, "Please fill up this form.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setDestination() {
        select_others.setOnClickListener {
            select_others.isChecked = true
            select_xlm.isChecked = false
            li_fill_others.visibility = View.VISIBLE
        }

        select_xlm.setOnClickListener {
            select_others.isChecked = false
            select_xlm.isChecked = true
            li_fill_others.visibility = View.GONE
        }
    }

    private fun onClickReceiveMoney() {
        btn_receive_money.setOnClickListener {
            if (pair == null) Toast.makeText(applicationContext, "You don't have account", Toast.LENGTH_LONG).show()

            pair?.apply {
                cst_result_receive.visibility = View.GONE
                pb_four.visibility = View.VISIBLE
                Horizon.doReceiveMoney(accountId, object : OnResponse<ReceiverResponse> {
                    override fun onError(error: String) {
                        pb_four.visibility = View.GONE
                        tv_result_receive.text = error
                        cst_result_receive.background =
                                ContextCompat.getDrawable(this@MainActivity, R.drawable.result_fail_background)
                        cst_result_receive.visibility = View.VISIBLE
                    }

                    override fun onSuccess(response: ReceiverResponse) {
                        pb_four.visibility = View.GONE
                        tv_result_receive.text = "${response.amount} ${response.assetName} from ${response.accountId}"
                        cst_result_receive.background =
                                ContextCompat.getDrawable(this@MainActivity, R.drawable.result_success_background)
                        cst_result_receive.visibility = View.VISIBLE
                    }
                })
            }
        }
    }

    private fun onClickSendMoney() {
        btn_send_money.setOnClickListener {
            if (pair == null) Toast.makeText(applicationContext, "You don't have account", Toast.LENGTH_LONG).show()

            pair?.apply {
                cst_result_send.visibility = View.GONE
                if (!edt_send_destination.text.isNullOrEmpty() && !edt_send_memo.text.isNullOrEmpty() && !edt_send_amount.text.isNullOrEmpty()) {
                    val lumenTransaction = TransactionItem(destination = edt_send_destination.text.toString(), secretKey = secretSeed, memo = edt_send_memo.text.toString(), amount = edt_send_amount.text.toString(), assetName = null)
                    if (isNonNativeAsset()) {
                        val otherTransaction = lumenTransaction.copy(assetName = edt_asset_code.text.toString())
                        doSendMoney(otherTransaction)
                    } else {
                        doSendMoney(lumenTransaction)
                    }
                } else Toast.makeText(applicationContext, "Please fill up this form.", Toast.LENGTH_LONG).show()

            }
        }
    }

    private fun isNonNativeAsset(): Boolean {
        return !(this.edt_asset_code.text.isNullOrEmpty() && edt_limit.text.isNullOrEmpty())
    }

    private fun doSendMoney(item: TransactionItem) {
        pb_three.visibility = View.VISIBLE
        Horizon.sendMoney(item, object : OnResponse<SubmitTransactionResponse> {
            override fun onError(error: String) {
                pb_three.visibility = View.GONE
                tv_result_send.text = error
                cst_result_send.background = ContextCompat.getDrawable(this@MainActivity, R.drawable.result_fail_background)
                cst_result_send.visibility = View.VISIBLE
                cst_transaction_receive.visibility = View.GONE
            }

            override fun onSuccess(response: SubmitTransactionResponse) {
                pb_three.visibility = View.GONE
                tv_result_send.text = "SUCCESS! Has been sent account :)"
                cst_result_send.background = ContextCompat.getDrawable(this@MainActivity, R.drawable.result_success_background)
                cst_result_send.visibility = View.VISIBLE
                cst_transaction_receive.visibility = View.VISIBLE
            }
        })
    }

    private fun onClickGetAccounts() {
        btn_get_account.setOnClickListener {
            if (pair == null) Toast.makeText(applicationContext, "You don't have account", Toast.LENGTH_LONG).show()

            pair?.apply {
                cst_account_detail.visibility = View.GONE
                pb_two.visibility = View.VISIBLE
                tv_account_detail_id.text = "Balances for account \n$accountId"

                Horizon.getBalance(this, object : OnResponse<org.stellar.sdk.responses.AccountResponse> {
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
    }

    private fun onClickGetFriendbot() {
        btn_get_test_network_lumens.setOnClickListener {
            if (pair == null) Toast.makeText(applicationContext, "Please generate KeyPair", Toast.LENGTH_LONG).show()

            pair?.apply {
                cst_result_friendbot.visibility = View.GONE
                pb_one.visibility = View.VISIBLE
                ApiManager().getFriendBot(accountId, object : OnResponse<FriendBotResponse> {
                    override fun onError(error: String) {
                        pb_one.visibility = View.GONE
                        tv_result_friend_bot.text = error
                        cst_result_friendbot.background =
                                ContextCompat.getDrawable(this@MainActivity, R.drawable.result_fail_background)
                        cst_result_friendbot.visibility = View.VISIBLE
                        cst_transaction.visibility = View.GONE
                    }

                    override fun onSuccess(response: FriendBotResponse) {
                        pb_one.visibility = View.GONE
                        tv_result_friend_bot.text = "SUCCESS! You have a new account :)"
                        cst_result_friendbot.background =
                                ContextCompat.getDrawable(this@MainActivity, R.drawable.result_success_background)
                        cst_result_friendbot.visibility = View.VISIBLE
                        cst_transaction.visibility = View.VISIBLE
                    }
                })
            }
        }
    }

    private fun onClickGenerateKeyPair() {
        btn_generate_keypair.setOnClickListener {
            val keyPair = KeyPair.random()
            pair = keyPair
            if (pair == null) Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_LONG).show()
            pair?.apply {
                generateQRCode(accountId, img_qrcode, 500)
                tv_copy_public_key_id.visibility = View.VISIBLE
                tv_copy_secret_key_id.visibility = View.VISIBLE

                tv_public_key_id.text = accountId
                tv_secret_key_id.text = String(secretSeed)
                tv_account_id.text = accountId

                tv_copy_public_key_id.setOnClickListener { copyAddressToClipBoard(accountId) }
                tv_copy_secret_key_id.setOnClickListener { copyAddressToClipBoard(String(secretSeed)) }
            }
        }
    }

    private fun generateQRCode(data: String, imageView: ImageView, size: Int) {
        val barcodeEncoder = BarcodeEncoder()
        val bitmap = barcodeEncoder.encodeBitmap(data, BarcodeFormat.QR_CODE, size, size)
        imageView.setImageBitmap(bitmap)
        imageView.visibility = View.VISIBLE
    }

    private fun copyAddressToClipBoard(data: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("BlockEQ Address", data)
        clipboard.primaryClip = clip

        Toast.makeText(this, "Address copied", Toast.LENGTH_LONG).show()
    }

}

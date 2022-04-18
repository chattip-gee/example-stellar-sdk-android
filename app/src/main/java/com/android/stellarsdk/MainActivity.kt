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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.stellarsdk.adapter.BalanceAdapter
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
import org.stellar.sdk.responses.AccountResponse
import org.stellar.sdk.responses.SubmitTransactionResponse


class MainActivity : AppCompatActivity() {
    //TODO REFACTOR

    private var pair: KeyPair? = null
    private var viewAdapter: RecyclerView.Adapter<*>? = null
    private val dataSet = ArrayList<AccountResponse.Balance>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupView()
        setupEvent()
    }

    private fun setupView() {
        setDestination()

        setupRecycleView()
    }

    private fun setupRecycleView() {
        viewAdapter = BalanceAdapter(dataSet)
        val viewManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        rv_account_detail.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private fun setupEvent() {
        onClickGenerateKeyPair()

        onClickGetFriendbot()

        onClickGetAccounts()

        onClickSendMoney()

        onClickReceiveMoney()

        onClickAddAsset()
    }

    private fun onClickAddAsset() {
        btn_add_asset.setOnClickListener {
            if (pair == null) Toast.makeText(applicationContext, "You don't have account", Toast.LENGTH_SHORT).show()

            pair?.apply {
                if (isAddAsset()) {
                    pb_add_asset.visibility = View.VISIBLE
                    cst_result_add_asset.visibility = View.GONE

                    val item = AddAssetItem(
                        issuer = edt_add_asset_issuer.text.toString(),
                        secretKey = secretSeed,
                        assetName = edt_add_asset_code.text.toString(),
                        limit = edt_add_asset_limit.text.toString()
                    )
                    Horizon.addAsset(item, object : OnResponse<SubmitTransactionResponse> {
                        override fun onError(error: String) {
                            pb_add_asset.visibility = View.GONE
                            tv_result_add_asset.text = error
                            cst_result_add_asset.background =
                                ContextCompat.getDrawable(this@MainActivity, R.drawable.result_fail_background)
                            cst_result_add_asset.visibility = View.VISIBLE
                        }

                        override fun onSuccess(response: SubmitTransactionResponse) {
                            pb_add_asset.visibility = View.GONE
                            tv_result_add_asset.text = "SUCCESS! Added Asset"
                            cst_result_receive.background =
                                ContextCompat.getDrawable(this@MainActivity, R.drawable.result_success_background)
                            cst_result_add_asset.visibility = View.VISIBLE
                        }
                    })
                } else {
                    Toast.makeText(applicationContext, "Please fill up this form.", Toast.LENGTH_SHORT).show()
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
            if (pair == null) Toast.makeText(applicationContext, "You don't have account", Toast.LENGTH_SHORT).show()

            pair?.apply {
                cst_result_receive.visibility = View.GONE
                pb_receive_money.visibility = View.VISIBLE
                Horizon.doReceiveMoney(accountId, object : OnResponse<ReceiverResponse> {
                    override fun onError(error: String) {
                        pb_receive_money.visibility = View.GONE
                        tv_result_receive.text = error
                        cst_result_receive.background =
                            ContextCompat.getDrawable(this@MainActivity, R.drawable.result_fail_background)
                        cst_result_receive.visibility = View.VISIBLE
                    }

                    override fun onSuccess(response: ReceiverResponse) {
                        pb_receive_money.visibility = View.GONE
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
            if (pair == null) Toast.makeText(applicationContext, "You don't have account", Toast.LENGTH_SHORT).show()

            pair?.apply {
                cst_result_send.visibility = View.GONE
                if (!edt_send_destination.text.isNullOrEmpty() && !edt_send_memo.text.isNullOrEmpty() && !edt_send_amount.text.isNullOrEmpty()) {
                    val lumenTransaction = TransactionItem(
                        destination = edt_send_destination.text.toString(),
                        secretKey = secretSeed,
                        memo = edt_send_memo.text.toString(),
                        amount = edt_send_amount.text.toString(),
                        assetName = null
                    )

                    if (select_others.isChecked) {
                        sendMoneyByOtherAsset(lumenTransaction)
                    } else doSendMoney(lumenTransaction)

                } else Toast.makeText(applicationContext, "Please fill up this form.", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun sendMoneyByOtherAsset(lumenTransaction: TransactionItem) {
        if (!edt_asset_code.text.isNullOrEmpty()) {
            val otherTransaction = lumenTransaction.copy(assetName = edt_asset_code.text.toString())
            doSendMoney(otherTransaction)
        } else Toast.makeText(applicationContext, "Please fill up this form.", Toast.LENGTH_SHORT).show()
    }

    private fun isAddAsset(): Boolean {
        return !edt_add_asset_issuer.text.isNullOrEmpty() && !edt_add_asset_code.text.isNullOrEmpty() && !edt_add_asset_limit.text.isNullOrEmpty()
    }

    private fun doSendMoney(item: TransactionItem) {
        pb_send_money.visibility = View.VISIBLE
        Horizon.sendMoney(item, object : OnResponse<SubmitTransactionResponse> {
            override fun onError(error: String) {
                pb_send_money.visibility = View.GONE
                tv_result_send.text = error
                cst_result_send.background =
                    ContextCompat.getDrawable(this@MainActivity, R.drawable.result_fail_background)
                cst_result_send.visibility = View.VISIBLE
                cst_transaction_receive.visibility = View.GONE
            }

            override fun onSuccess(response: SubmitTransactionResponse) {
                pb_send_money.visibility = View.GONE
                tv_result_send.text = "SUCCESS! Has been sent account :)"
                cst_result_send.background =
                    ContextCompat.getDrawable(this@MainActivity, R.drawable.result_success_background)
                cst_result_send.visibility = View.VISIBLE
                cst_transaction_receive.visibility = View.VISIBLE
            }
        })
    }

    private fun onClickGetAccounts() {
        btn_get_account.setOnClickListener {
            if (pair == null) Toast.makeText(applicationContext, "You don't have account", Toast.LENGTH_SHORT).show()

            pair?.apply {
                cst_account_detail.visibility = View.GONE
                pb_get_account.visibility = View.VISIBLE
                tv_account_detail_id.text = "Balances for account \n$accountId"

                Horizon.getBalance(this, object : OnResponse<AccountResponse> {
                    override fun onError(error: String) {
                        pb_get_account.visibility = View.GONE
                        rv_account_detail.visibility = View.GONE
                        cst_account_detail.background =
                            ContextCompat.getDrawable(this@MainActivity, R.drawable.result_fail_background)
                        cst_account_detail.visibility = View.VISIBLE
                    }

                    override fun onSuccess(response: AccountResponse) {
                        dataSet.clear()
                        for (balance in response.balances) {
                            dataSet.add(balance)
                            viewAdapter?.notifyDataSetChanged()
                            pb_get_account.visibility = View.GONE
                            cst_account_detail.background = ContextCompat.getDrawable(this@MainActivity, R.drawable.result_success_background)
                            cst_account_detail.visibility = View.VISIBLE
                        }
                    }
                })
            }
        }
    }

    private fun onClickGetFriendbot() {
        btn_get_test_network_lumens.setOnClickListener {
            if (pair == null) Toast.makeText(applicationContext, "Please generate KeyPair", Toast.LENGTH_SHORT).show()

            pair?.apply {
                cst_result_friendbot.visibility = View.GONE
                pb_get_test_network_lumens.visibility = View.VISIBLE
                ApiManager().getFriendBot(accountId, object : OnResponse<FriendBotResponse> {
                    override fun onError(error: String) {
                        pb_get_test_network_lumens.visibility = View.GONE
                        tv_result_friend_bot.text = error
                        cst_result_friendbot.background =
                            ContextCompat.getDrawable(this@MainActivity, R.drawable.result_fail_background)
                        cst_result_friendbot.visibility = View.VISIBLE
                        cst_transaction.visibility = View.GONE
                    }

                    override fun onSuccess(response: FriendBotResponse) {
                        pb_get_test_network_lumens.visibility = View.GONE
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
            if (pair == null) Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT).show()
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

        Toast.makeText(this, "Address copied", Toast.LENGTH_SHORT).show()
    }

}

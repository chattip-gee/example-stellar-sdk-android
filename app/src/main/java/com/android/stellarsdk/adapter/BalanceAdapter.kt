package com.android.stellarsdk.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.stellarsdk.R
import kotlinx.android.synthetic.main.item_detail_balance.view.*
import org.stellar.sdk.responses.AccountResponse

class BalanceAdapter(private val dataSet: MutableList<AccountResponse.Balance>) :
    RecyclerView.Adapter<BalanceAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail_balance, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataSet[position]
        holder.itemView.apply {
            val currency = if (item.assetType == "native") "lumens"
            else item.assetCode
            tv_account_detail.text = String.format("Currency: %s, Balance: %s", currency, item.balance)
        }
    }

    override fun getItemCount() = dataSet.size
}
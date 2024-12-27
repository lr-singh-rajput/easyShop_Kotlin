package com.SapnokitPaat.EaseShop.Adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.SapnokitPaat.EaseShop.Model.transactionModel
import com.SapnokitPaat.EaseShop.databinding.RvTransactionBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class transactionAdaptor (private val T_List: List<transactionModel>,
                            private val onItemClick: (transactionModel) -> Unit
): RecyclerView.Adapter<transactionAdaptor.T_ViewHolder>(){

    interface OnItemClickListener{
        fun onDeleteClick(id:String)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): transactionAdaptor.T_ViewHolder {
        val binding = RvTransactionBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return transactionAdaptor.T_ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: transactionAdaptor.T_ViewHolder, position: Int) {
        val item = T_List[position]
      //  holder.bind(T_List[position])
       holder. binding.showgetT.text = item.T_got.toString()
        holder. binding.showgiveT.text = item.T_give.toString()
        holder.binding.itemdetaileshow.text = item.T_detail
        holder.binding.showbillno.text = item.T_billNo

        // Convert milliseconds to formatted date and time string
        val dateFormat = SimpleDateFormat("dd-MM-yyyy  hh:mm a", Locale.getDefault())
        val formattedDateTime = dateFormat.format(Date(item.mfgDt))
        holder. binding.edtdt.text = formattedDateTime

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }
    override fun getItemCount(): Int {

        return T_List.size

    }
    class T_ViewHolder(val binding: RvTransactionBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(item: transactionModel) {
            binding.showgetT.text = item.T_got.toString()
            binding.showgiveT.text = item.T_give.toString()
            binding.itemdetaileshow.text = item.T_detail
            binding.showbillno.text = item.T_billNo

            // Convert milliseconds to formatted date and time string
            val dateFormat = SimpleDateFormat("yyyy-MM-dd \n  hh:mm a", Locale.getDefault())
            val formattedDateTime = dateFormat.format(Date(item.mfgDt))
            binding.edtdt.text = formattedDateTime


        }


    }
}
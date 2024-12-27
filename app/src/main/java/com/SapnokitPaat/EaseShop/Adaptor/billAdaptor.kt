package com.SapnokitPaat.EaseShop.Adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.SapnokitPaat.EaseShop.Adaptor.billAdaptor.BL_viewHolder
import com.SapnokitPaat.EaseShop.Model.BillModel
import com.SapnokitPaat.EaseShop.databinding.RvBillBinding
import java.text.DecimalFormat


class billAdaptor (val B_list : List<BillModel>):RecyclerView.Adapter<BL_viewHolder>(){


    inner class BL_viewHolder (val binding: RvBillBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item:BillModel) {
            binding.shownamervbill.text= item.itemName
          //  binding.showdetailrvbill.text= item.detail
            binding.showratervbill.text= item.rate.toString()
            binding.showquntityrvbill.text= item.quantity.toString()
            binding.showgstrvbill.text= item.gst.toString()
            binding.showamountrvbill.text= item.amount.toString()
            //binding.showtotalrvbill.text= item.amountwithgst.toString()
            val total= item.amountwithgst.toString().toDouble()
            val formattedValue = DecimalFormat("#.##").format(total)
            binding.showtotalrvbill.text = formattedValue
            // Convert milliseconds to formatted date and time string
        //    val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
        //    val formattedDateTime = dateFormat.format(Date(item.date))
        //    binding.showdatabill.text = formattedDateTime


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BL_viewHolder {
        val binding = RvBillBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BL_viewHolder(binding)
    }

    override fun getItemCount(): Int {
        return B_list.size
    }

    override fun onBindViewHolder(holder: BL_viewHolder, position: Int) {
        val item = B_list[position]
        holder.binding.shownamervbill.text=item.itemName
       // holder.binding.showdetailrvbill.text= item.detail
       // holder.binding.showratervbill.text=item.rate.toString()
        val rateFormat =item.rate.toString().toDouble()
        val formattedRate = DecimalFormat("#.#").format(rateFormat)
        holder.binding.showratervbill.text = formattedRate

        holder.binding.showquntityrvbill.text= item.quantity.toString()

        val gstPercentage = item.gst

        if(gstPercentage ==0){
            holder.binding.showgstrvbill.text= "inGST"
        }else{
            holder.binding.showgstrvbill.text= "$gstPercentage %"
            // holder.binding.showgstrsrvbill.text = item.totalgst.toString()
            val gstrsFormat =item.totalgst.toString().toDouble()
            val formattedGstA = DecimalFormat("#").format(gstrsFormat)
            holder.binding.showgstrsrvbill.text = formattedGstA
        }





        holder.binding.showunitrvbill.text= item.unit
        //holder.binding.showamountrvbill.text= item.amount.toString()
        val amountFormat =item.amount.toString().toDouble()
        val formattedAmaunt = DecimalFormat("#").format(amountFormat)
        holder.binding.showamountrvbill.text = formattedAmaunt

        //holder.binding.showtotalrvbill.text= item.amountwithgst.toString()
        val total= item.amountwithgst.toString().toDouble()
        val formattedValue = DecimalFormat("#").format(total)
        holder.binding.showtotalrvbill.text = formattedValue


        // Convert milliseconds to formatted date and time string
       // val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
       // val formattedDateTime = dateFormat.format(Date(item.date))
      //  holder. binding.edtdt.text = formattedDateTime

    }

    fun calculateTotalAmount(): Double {
    var totalAmount = 0.0
        for (item in B_list){
            totalAmount += item.amountwithgst

        }
        return totalAmount
    }


}
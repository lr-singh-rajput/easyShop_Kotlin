package com.SapnokitPaat.EaseShop.Adaptor

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.SapnokitPaat.EaseShop.Model.customerModel
import com.SapnokitPaat.EaseShop.databinding.RvcustomersBinding

class customerAdaptor (private var C_List: List<customerModel>,
                       private val onItemClick: (customerModel) -> Unit
): RecyclerView.Adapter<customerAdaptor.C_ViewHolder>(){


    class C_ViewHolder (val binding: RvcustomersBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item: customerModel,onItemClick: (customerModel) -> Unit){
            binding.showName.text = item.C_name

            val totalC = item.C_moneyP
            binding.totalPriceShow.text ="₹$totalC"


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): C_ViewHolder {
        val binding = RvcustomersBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return C_ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return C_List.size

    }

    override fun onBindViewHolder(holder: C_ViewHolder, position: Int) {
        val item = C_List[position]
        holder.bind(item,onItemClick)
        holder.binding.showName.text = item.C_name
        val totalC = item.C_moneyP
        holder.binding.totalPriceShow.text ="₹$totalC"
        if (item.C_moneyP >= 0.toString()){
            holder.binding.totalPriceShow.setTextColor(Color.GREEN)
        }else{
            holder.binding.totalPriceShow.setTextColor(Color.RED)

        }

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }

    }

    fun setData(newList: List<customerModel>){
        C_List = newList
        notifyDataSetChanged()
    }



}


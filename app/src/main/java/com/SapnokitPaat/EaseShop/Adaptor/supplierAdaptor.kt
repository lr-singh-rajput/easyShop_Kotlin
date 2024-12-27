package com.SapnokitPaat.EaseShop.Adaptor

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.SapnokitPaat.EaseShop.Model.supplierModel
import com.SapnokitPaat.EaseShop.databinding.RvsuppliersBinding

class supplierAdaptor (
    private var S_List: List<supplierModel>,
    private val onItemClick: (supplierModel) -> Unit
    ): RecyclerView.Adapter<supplierAdaptor.S_ViewHolder>(){


    class S_ViewHolder(val binding: RvsuppliersBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(item: supplierModel, onItemClick: (supplierModel) -> Unit) {
            binding.showNameS.text = item.S_name

            val totalS = item.S_money
            binding.totalS.text ="₹$totalS"
            if (item.S_money >= 0.toString()){
                binding.totalS.setTextColor(Color.GREEN)
            }else{
                binding.totalS.setTextColor(Color.RED)

            }

        }

    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): S_ViewHolder {
    val binding = RvsuppliersBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    return S_ViewHolder(binding)
    // val binding = RvcustomersBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        //return supplierAdaptor.S_ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return S_List.size


    }

    override fun onBindViewHolder(holder: S_ViewHolder, position: Int) {
        val item = S_List[position]
        holder.bind(item,onItemClick)
       // holder.binding.showNameS.text = item.S_name
     //  holder. binding.idshow.text = item.S_id

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    fun setData(newList: List<supplierModel>) {
        S_List = newList
        notifyDataSetChanged()
    }


}



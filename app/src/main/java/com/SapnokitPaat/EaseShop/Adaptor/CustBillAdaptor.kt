package com.SapnokitPaat.EaseShop.Adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.SapnokitPaat.EaseShop.Model.billCutomerModel
import com.SapnokitPaat.EaseShop.databinding.RvcustomersBinding


class CustBillAdaptor(private var C_List: List<billCutomerModel>,
                      private val onItemClick: (billCutomerModel) -> Unit
): RecyclerView.Adapter<CustBillAdaptor.CB_ViewHolder>() {


    class CB_ViewHolder(val binding: RvcustomersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: billCutomerModel, onItemClick: (billCutomerModel) -> Unit) {
            binding.showName.text = item.C_name

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CB_ViewHolder {
        val binding = RvcustomersBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CB_ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return C_List.size
    }

    override fun onBindViewHolder(holder: CB_ViewHolder, position: Int) {
        val item = C_List[position]
        holder.bind(item,onItemClick)
        // holder.binding.showName.text = item.C_name
        // holder.binding.showidC.text = item.C_id

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }

    }
    fun setData(newList:List<billCutomerModel>){
        C_List = newList
        notifyDataSetChanged()
    }

}







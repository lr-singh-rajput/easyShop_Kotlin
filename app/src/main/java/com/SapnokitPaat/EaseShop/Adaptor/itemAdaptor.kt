package com.SapnokitPaat.EaseShop.Adaptor

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.SapnokitPaat.EaseShop.Model.itemModel
import com.SapnokitPaat.EaseShop.R
import com.SapnokitPaat.EaseShop.databinding.RvitemshowBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class itemAdaptor(private var itemList: List<itemModel>,
                  private val onItemClick: (itemModel) -> Unit
): RecyclerView.Adapter<itemAdaptor.ItemViewHolder>() {

    interface OnItemClickListener{
        fun onDeleteClick(id:String)

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = RvitemshowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
      // holder.bind(itemList[position])
        val item = itemList[position]
        holder.bind(item,onItemClick)

         //   holder.binding.nameitemshow.text = item.itemname
          //  holder.binding.showstock.text = item.stock.toString()
          //  holder.binding.shellpriceshow.text = item.shellprice.toString()

        // Load image using Picasso
        //Picasso.get().load(item.imageitem).into(holder.binding.showImage)

        Picasso.get()
            .load(item.imageitem)
            .placeholder(R.drawable.addimage)
            .into(holder.binding.showImage, object : Callback {
                override fun onSuccess() {
                    // Image loaded successfully
                }

                override fun onError(e: Exception?) {
                    // Error occurred while loading image
                    Log.e("Picasso", "Error loading image: ${e?.localizedMessage}")
                }
            })



        // Load image using Picasso
       // Picasso.get()
       //     .load(item.imageitem)
       //     .placeholder(R.drawable.add) // Placeholder image while loading
       //     .error(R.drawable.calculater) // Error image if the URL is invalid or loading fails
        //    .into(holder.binding.showImage)




        holder.itemView.setOnClickListener {
            onItemClick(item)
        }

    }

    override fun getItemCount(): Int {
    return itemList.size
    }


     class ItemViewHolder (val binding: RvitemshowBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(item: itemModel,onItemClick: (itemModel) -> Unit) {

            binding.nameitemshow.text = item.itemname
            binding.showstock.text = item.stock.toString()
            binding.shellpriceshow.text = item.shellprice.toString()


            // update and delete code

        }
    }
    fun setData(newList : List <itemModel>){
        itemList = newList
        notifyDataSetChanged()
    }
}





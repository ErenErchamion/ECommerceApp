package com.example.e_commerce_app.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_commerce_app.R
import com.example.e_commerce_app.data.BrandData

class RecyclerAdapter(val context: Context):RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    var brandList:ArrayList<BrandData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
       val v=LayoutInflater.from(parent.context).inflate(R.layout.recyclerlayout,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        val brand:BrandData=brandList.get(position)
        holder.itemTitle.text=brand.brandName
        Glide.with(context).load(brand.brandImagePath).into(holder.itemImage)
    }

    override fun getItemCount(): Int {
      return brandList.size
    }
    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        var itemImage:ImageView
        var itemTitle:TextView

        init {
            itemImage=itemView.findViewById(R.id.imageView)
            itemTitle=itemView.findViewById(R.id.textView)
        }

    }

    fun setList(newBrandList:ArrayList<BrandData>){
        brandList=newBrandList
        notifyDataSetChanged()
    }
}

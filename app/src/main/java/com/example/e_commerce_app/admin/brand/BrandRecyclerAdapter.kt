package com.example.e_commerce_app.admin.brand

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_commerce_app.R
import com.example.e_commerce_app.data.BrandData

class BrandRecyclerAdapter(val context: Context):RecyclerView.Adapter<BrandRecyclerAdapter.ViewHolder>() {
    var brandList:ArrayList<BrandData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val v=LayoutInflater.from(parent.context).inflate(R.layout.brandrecyclerlayout,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.currentBrand=brandList.get(position)
        holder.itemTitle.text=   holder.currentBrand.brandName
        Glide.with(context).load(   holder.currentBrand.brandImagePath).into(holder.itemImage)
    }

    override fun getItemCount(): Int {
      return brandList.size
    }
    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView),View.OnClickListener{
        lateinit var currentBrand:BrandData
        var itemImage:ImageView
        var itemTitle:TextView

        init {
            itemImage=itemView.findViewById(R.id.imageViewBrandRecycler)
            itemTitle=itemView.findViewById(R.id.textViewBrandTitle)
            itemView.setOnClickListener(this)

        }

        override fun onClick(v: View?) {
            val intent=Intent(context, BrandUpdateActivity::class.java)
            intent.putExtra("brand",currentBrand)
            context.startActivity(intent)
        }

    }

    fun setList(newBrandList:ArrayList<BrandData>){
        brandList=newBrandList
        notifyDataSetChanged()
    }
}

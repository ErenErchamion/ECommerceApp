package com.example.e_commerce_app.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_app.R
import com.example.e_commerce_app.data.BrandData

class RecyclerAdapter:RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
   lateinit var brandList:ArrayList<BrandData>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
       val v=LayoutInflater.from(parent.context).inflate(R.layout.recyclerlayout,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        var itemImage:ImageView
        var itemTitle:TextView

        init {
            itemImage=itemView.findViewById(R.id.imageView)
            itemTitle=itemView.findViewById(R.id.textView)
        }

    }
}

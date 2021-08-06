package com.example.e_commerce_app.admin.category

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
import com.example.e_commerce_app.data.CategoryData

class CategoryRecyclerAdapter(val context: Context): RecyclerView.Adapter<CategoryRecyclerAdapter.ViewHolder>() {
    var catList:ArrayList<CategoryData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.categoryrecyclerlayout,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.currentCategory=catList.get(position)
        holder.itemTitle.text=holder.currentCategory.catName
        Glide.with(context).load(holder.currentCategory.catImagePath).into(holder.itemImage)
    }

    override fun getItemCount(): Int {
        return catList.size
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        lateinit var currentCategory: CategoryData
        var itemImage: ImageView
        var itemTitle: TextView

        init {
            itemImage=itemView.findViewById(R.id.imageViewRecyclerCategoryImage)
            itemTitle=itemView.findViewById(R.id.textViewCategoryTitle)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v:View) {
            val intent = Intent(context, CategoryUpdateActivity::class.java)
            intent.putExtra("parrentcategory",currentCategory)

            context.startActivity(intent)
        }

    }

    fun setList(newCategoryList:ArrayList<CategoryData>){
        catList=newCategoryList
        notifyDataSetChanged()
    }
}
package com.example.e_commerce_app.admin.category

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_commerce_app.R
import com.example.e_commerce_app.data.BrandData
import com.example.e_commerce_app.data.CategoryData

class CategoryRecyclerAdapter(val context: Activity): RecyclerView.Adapter<CategoryRecyclerAdapter.ViewHolder>() {
    var catList:ArrayList<CategoryData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.categoryrecyclerlayout,parent,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.currentCategory=catList.get(position)

        holder.categoryitemTitle.text=holder.currentCategory.catName
        Glide.with(context).load(holder.currentCategory.catImagePath).into(holder.categoryitemImage)
        bindSubCatList(holder.subCatRootLy,holder.currentCategory.subCat)

    }

    fun bindSubCatList(rootLy:LinearLayout,subCatList:List<CategoryData>?){
        rootLy.removeAllViews()
        // TODO: 8/6/2021 subCatList for ile dönceksin
        // TODO: 8/6/2021 subCat xml inden view inflate edeceksin
        // TODO: 8/6/2021 for içinde inflate ettigin viewin imageview,categoryName gibi alanlarını dolduracaksın
        // TODO: 8/6/2021 rootLy.addview methoduyla rootLy a olusan view'i ekleyeceksin
    }

    override fun getItemCount(): Int {
        return catList.size
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        lateinit var currentCategory: CategoryData
        var categoryitemImage: ImageView
        var categoryitemTitle: TextView
        var subCatRootLy:LinearLayout

        init {
            categoryitemImage=itemView.findViewById(R.id.imageViewRecyclerCategoryImage)
            categoryitemTitle=itemView.findViewById(R.id.textViewCategoryTitle)
            subCatRootLy=itemView.findViewById(R.id.subCatRootLy)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v:View) {
            val intent = Intent(context, CategoryUpdateActivity::class.java)
            intent.putExtra("parrentcategory",currentCategory)
            context.startActivityForResult(intent,1)
        }

    }

    fun setList(newCategoryList:ArrayList<CategoryData>){
        catList=newCategoryList
        notifyDataSetChanged()
    }
}
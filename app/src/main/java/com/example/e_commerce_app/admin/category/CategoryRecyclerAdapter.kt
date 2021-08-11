package com.example.e_commerce_app.admin.category

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.Image
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
import org.w3c.dom.Text

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
        if (subCatList!=null){
            for (categoryData:CategoryData in subCatList){
                val childRootLy=LayoutInflater.from(context).inflate(R.layout.categorysubrecyclerlayout,null)

                val subCatImageView:ImageView=childRootLy.findViewById(R.id.imageViewCategoryRecyclerSub)
                val subCatTextView:TextView=childRootLy.findViewById(R.id.textViewCategoryRecyclerSub)

                Glide.with(context).load(categoryData.catImagePath).into(subCatImageView)
                subCatTextView.setText(categoryData.catName)

                childRootLy.setOnClickListener{
                    val intent = Intent(context, CategoryUpdateActivity::class.java)
                    intent.putExtra("subCategory",categoryData)
                    intent.putExtra("subCategoryParentCategory",categoryData.catParrentId)
                    intent.putExtra("index",subCatList.indexOf(categoryData))

                    context.startActivityForResult(intent,1)
                }

                rootLy.addView(childRootLy)
            }
        }
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
package com.example.e_commerce_app.admin.category

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_app.R
import com.example.e_commerce_app.data.CategoryCallBack
import com.example.e_commerce_app.data.CategoryData

class CategoryListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_list)
        init()
    }
    fun init(){
        val recylerView=findViewById<RecyclerView>(R.id.recyclerViewCategory)
        recylerView.layoutManager= LinearLayoutManager(this)
        recylerView.adapter= CategoryRecyclerAdapter(this)
        CategoryWSHelper.getCategoriesFromFireStore(object : CategoryCallBack {
            override fun CatListResponse(catList: ArrayList<CategoryData>) {
                runOnUiThread {
                    (recylerView.adapter as CategoryRecyclerAdapter).setList(catList)
                }
            }
        })
    }
}
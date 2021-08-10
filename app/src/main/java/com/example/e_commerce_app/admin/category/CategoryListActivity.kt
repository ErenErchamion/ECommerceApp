package com.example.e_commerce_app.admin.category

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_app.R
import com.example.e_commerce_app.admin.brand.BrandAddActivity
import com.example.e_commerce_app.data.CategoryCallBack
import com.example.e_commerce_app.data.CategoryData

class CategoryListActivity : AppCompatActivity() {
    lateinit var recylerView:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_list)
        val backBtn=findViewById(R.id.backBtn) as ImageView
        val actionBtn=findViewById(R.id.actionBtn) as ImageView
        val titleTextView=findViewById(R.id.titleTv) as TextView
        backBtn.setOnClickListener(){
            onBackPressed()
        }
        actionBtn.setOnClickListener(){
            val intent = Intent(this, CategoryAddActivity::class.java)
            startActivity(intent)

        }


        init()

    }
    fun init(){
        recylerView=findViewById<RecyclerView>(R.id.recyclerViewCategory)
        recylerView.layoutManager= LinearLayoutManager(this)
        recylerView.adapter= CategoryRecyclerAdapter(this)
        getListFromWS(recylerView)
    }

    private fun getListFromWS(recylerView: RecyclerView) {
        CategoryWSHelper.getCategoriesFromFireStore(object : CategoryCallBack {
            override fun CatListResponse(catList: ArrayList<CategoryData>) {
                runOnUiThread {
                    (recylerView.adapter as CategoryRecyclerAdapter).setList(catList)
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode== RESULT_OK)
            getListFromWS(recylerView)
        super.onActivityResult(requestCode, resultCode, data)

    }
}
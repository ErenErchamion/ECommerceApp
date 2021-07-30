package com.example.e_commerce_app.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_app.R
import com.example.e_commerce_app.data.BrandCallBack
import com.example.e_commerce_app.data.BrandData

class BrandListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_categgory)
        init()
    }

    fun init(){
        val recylerView=findViewById<RecyclerView>(R.id.recycler_view)
        recylerView.layoutManager=LinearLayoutManager(this)
        recylerView.adapter=RecyclerAdapter(this)
        WSHelper.getBrandsFromFireStore(object :BrandCallBack{
            override fun brandListResponse(brandList: ArrayList<BrandData>) {
             runOnUiThread {
                 (recylerView.adapter as RecyclerAdapter).setList(brandList)
             }
            }
        })
    }
}
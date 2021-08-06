package com.example.e_commerce_app.admin.brand

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce_app.R
import com.example.e_commerce_app.data.BrandCallBack
import com.example.e_commerce_app.data.BrandData

class BrandListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_brand_list)
        val backBtn=findViewById(R.id.backBtn) as ImageView
        val actionBtn=findViewById(R.id.actionBtn) as ImageView
        val titleTextView=findViewById(R.id.titleTv) as TextView
        init()
        backBtn.setOnClickListener(){
            onBackPressed()
        }
        actionBtn.setOnClickListener(){
            val intent = Intent(this, BrandAddActivity::class.java)
            startActivity(intent)

        }
    }


    fun init(){
        val recylerView=findViewById<RecyclerView>(R.id.recycler_view)
        recylerView.layoutManager=LinearLayoutManager(this)
        recylerView.adapter= BrandRecyclerAdapter(this)
        BrandWSHelper.getBrandsFromFireStore(object : BrandCallBack {
            override fun brandListResponse(brandList: ArrayList<BrandData>) {
                runOnUiThread {
                    (recylerView.adapter as BrandRecyclerAdapter).setList(brandList)
                }
            }
        })
    }


}
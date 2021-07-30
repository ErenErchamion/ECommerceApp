package com.example.e_commerce_app.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.e_commerce_app.R
import com.example.e_commerce_app.data.BrandData

class AdminPanelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)
    }
    fun openBrandActivity(){
        val intent = Intent(this, AdminAddBrandActivity::class.java)
        val brandData=BrandData()
        intent.putExtra("brand",brandData)
        startActivity(intent)

    }


}
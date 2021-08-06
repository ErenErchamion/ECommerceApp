package com.example.e_commerce_app.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.e_commerce_app.R
import com.example.e_commerce_app.admin.brand.BrandListActivity

class AdminPanelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)
    }
    fun openBrandActivity(view: View){
        val intent = Intent(this, BrandListActivity::class.java)
        startActivity(intent)

    }


}
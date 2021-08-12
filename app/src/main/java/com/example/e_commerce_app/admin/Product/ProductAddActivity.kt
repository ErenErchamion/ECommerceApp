package com.example.e_commerce_app.admin.Product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.e_commerce_app.R
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import java.sql.Timestamp as Timestamp

class ProductAddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_add)


        var imgs = listOf<Int>(R.drawable.addimage,R.drawable.addimage)


        var adapter = ProductViewPagerAdapter(imgs,this)
        val pager=findViewById(R.id.pager) as ViewPager


       /* val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        Toast.makeText(applicationContext, ""+currentDate, Toast.LENGTH_SHORT).show()*/


    }
}
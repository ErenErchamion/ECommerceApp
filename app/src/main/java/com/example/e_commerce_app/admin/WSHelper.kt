package com.example.e_commerce_app.admin

import android.content.ContentValues
import android.util.Log
import com.example.e_commerce_app.data.BrandCallBack
import com.example.e_commerce_app.data.BrandData
import com.google.firebase.firestore.FirebaseFirestore

class WSHelper {
    fun getBrandsFromFireStore(brandCallBack: BrandCallBack){
        val brandList:ArrayList<BrandData> = ArrayList()
        val db = FirebaseFirestore.getInstance()
        db.collection("Brands")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val brand= BrandData()
                    var name=document.getString("brandName")
                    var imagepath=document.getString("brandImagePath")

                    brand.brandId= document.getString("brandId")!!
                    brand.brandName=name!!
                    brand.brandImagePath=imagepath!!
                    brandList.add(brand)

                }
                brandCallBack.brandListResponse(brandList)
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }

}
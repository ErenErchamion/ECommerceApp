package com.example.e_commerce_app.admin.category

import android.content.ContentValues
import android.util.Log
import com.example.e_commerce_app.data.BrandCallBack
import com.example.e_commerce_app.data.BrandData
import com.example.e_commerce_app.data.CategoryCallBack
import com.example.e_commerce_app.data.CategoryData
import com.google.firebase.firestore.FirebaseFirestore

class CategoryWSHelper
{
    companion object {
        fun getCategoriesFromFireStore(categoryCallBack: CategoryCallBack){
            val catList:ArrayList<CategoryData> = ArrayList()
            val db = FirebaseFirestore.getInstance()
            db.collection("Categories")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val category= CategoryData()

                        category.catId= document.getString("catId")!!
                        category.catName=document.getString("catName")!!
                        category.catImagePath=document.getString("catImagePath")!!
                        category.catParrentId=document.getString("catParrentId")!!
                        category.subCat= document.get("subCat") as List<CategoryData>?
                        catList.add(category)


                    }
                    // burda response callback tetiklenir
                    categoryCallBack.CatListResponse(catList)
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "Error getting documents: ", exception)
                }
        }
    }
}
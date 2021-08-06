package com.example.e_commerce_app.data

import java.io.Serializable

class CategoryData:Serializable {
    lateinit var catId:String
    lateinit var catName:String
    lateinit var catParrentId:String
     var catImagePath:String?=null
    var subCat:List<CategoryData>?=null
}
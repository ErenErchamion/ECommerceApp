package com.example.e_commerce_app.data

import java.io.Serializable

class CategoryData:Serializable {
    var catId:String?=null
    var catName:String?=null
    var catParrentId:String?=null
    var catImagePath:String?=null
    var subCat:List<CategoryData>?=null
}
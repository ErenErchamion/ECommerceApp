package com.example.e_commerce_app.data

class CategoryData {
    lateinit var catId:String
    lateinit var catName:String
    lateinit var catParrentId:String
    lateinit var catImagePath:String
    lateinit var subCat:List<CategoryData>
}
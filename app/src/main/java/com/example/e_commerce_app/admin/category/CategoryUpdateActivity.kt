package com.example.e_commerce_app.admin.category

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.annotation.Nullable
import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.e_commerce_app.R
import com.example.e_commerce_app.data.CategoryData
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.io.File

class CategoryUpdateActivity : AppCompatActivity() {
    lateinit var parrentCategory:CategoryData

    lateinit var subCategoryParrentId:String
    var subCategoryIndex=0
    lateinit var subCategory:CategoryData
    private lateinit var fileUri:Uri
    private var imageUrl:String?=null

    lateinit var oldimagename:String
    var isSub=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_update)

        val intent = intent
        val updateButton:Button=findViewById(R.id.buttonUpdateCategory)
        val deleteButton:Button=findViewById(R.id.buttonDeleteCategory)
        val addsubCatButton:Button=findViewById(R.id.buttonAddSubCategory)
        val updateCatImageView:ImageView=findViewById(R.id.imageViewEditCategoryImage)

        if (intent.hasExtra("parrentcategory")) {
            parrentCategory= intent.getSerializableExtra("parrentcategory") as CategoryData
            setOldCategory(parrentCategory)
            isSub=false

        } else {
            subCategoryParrentId=intent.getSerializableExtra("subCategoryParentCategory") as String
            subCategory=intent.getSerializableExtra("subCategory") as CategoryData
            subCategoryIndex=intent.getSerializableExtra("index") as Int
            setOldCategory(subCategory)
            isSub=true
            addsubCatButton.isVisible=false

        }

        setResult(RESULT_OK)


        updateButton.setOnClickListener(){
            if(isSub==false){
                uploadImageToFirebaseUpdate(fileUri)
            }
            else{
                uploadImageToFirebaseUpdateSub(fileUri)
            }
        }
        deleteButton.setOnClickListener(){
            if(isSub==false){
                deleteOldCategory(parrentCategory)
            }
        }
        updateCatImageView.setOnClickListener(){
            Upload()
        }
        addsubCatButton.setOnClickListener(){
            uploadImageToFirebaseAddSub(fileUri)
        }
    }

    @SuppressLint("SetTextI18n")
    fun setOldCategory(oldCategory:CategoryData){

        val editTextSubCategoryName:TextView=findViewById(R.id.editTextCategoryEditName)
        val imageViewSubCategoryImage:ImageView=findViewById(R.id.imageViewEditCategoryImage)
        val editTextTextSubCategoryImageName:TextView=findViewById(R.id.editTextCategoryEditImageName)

        val imageurl=(""+oldCategory.catImagePath)

        val httpsReference = FirebaseStorage.getInstance().getReferenceFromUrl(""+imageurl)
        oldimagename=(""+httpsReference.name).dropLast(4)

        editTextTextSubCategoryImageName.setText(""+oldimagename)
        editTextSubCategoryName.setText(""+oldCategory.catName)

        editTextTextSubCategoryImageName.setText(""+oldimagename)

        val media =(""+oldCategory.catImagePath)
        if (media !== null) {
            Glide.with(this).asBitmap().load(media).into(object : CustomTarget<Bitmap?>() {
                override fun onResourceReady(resource: Bitmap, @Nullable transition: Transition<in Bitmap?>?) {
                    imageViewSubCategoryImage.setImageBitmap(resource)
                    getImageUriFromBitmap(resource)

                }
                override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
            })

        }
    }


    fun getImageUriFromBitmap(photo: Bitmap){

        val file = File(this.cacheDir,"CUSTOM NAME") //Get Access to a local file.
        file.delete() // Delete the File, just in Case, that there was still another File
        file.createNewFile()
        val fileOutputStream = file.outputStream()
        val byteArrayOutputStream = ByteArrayOutputStream()
        photo.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream)
        val bytearray = byteArrayOutputStream.toByteArray()
        fileOutputStream.write(bytearray)
        fileOutputStream.flush()
        fileOutputStream.close()
        byteArrayOutputStream.close()

        fileUri= file.toUri()
    }


    fun Upload(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Please select..."
            ),
            100
        )
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {

        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if (requestCode == 100
            && resultCode == Activity.RESULT_OK
            && data != null
            && data.data != null
        ) {

            // Get the Uri of data
            if (data!=null&&data.data!=null){
                fileUri = data.data!!
                val updateCatImageView:ImageView=findViewById(R.id.imageViewEditCategoryImage)
                updateCatImageView.setImageURI(fileUri)
            }

        }

    }

    private fun uploadImageToFirebaseAddSub(fileUri: Uri?) {
        if (fileUri != null) {

            val fileNameText = findViewById<EditText>(R.id.editTextCategoryEditImageName)
            var filename=fileNameText.text.toString()
            var fileName = filename +".jpg"


            var refStorage = FirebaseStorage.getInstance().reference.child("Categories/$fileName")

            refStorage.putFile(fileUri)
                .addOnSuccessListener(
                    OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                            imageUrl = it.toString()
                            Toast.makeText(applicationContext, "Resim Başarıyla Yüklendi", Toast.LENGTH_SHORT).show()
                            addSubCategory(parrentCategory)

                        }
                    }


                )

                ?.addOnFailureListener(OnFailureListener { e ->
                    print(e.message)
                })
        }

    }

    private fun uploadImageToFirebaseUpdate(fileUri: Uri?) {
        if (fileUri != null) {

            val fileNameText = findViewById<EditText>(R.id.editTextCategoryEditImageName)
            var filename=fileNameText.text.toString()
            var fileName = filename +".jpg"


            var refStorage = FirebaseStorage.getInstance().reference.child("Categories/$fileName")

            refStorage.putFile(fileUri)
                .addOnSuccessListener(
                    OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                            imageUrl = it.toString()
                            Toast.makeText(applicationContext, "Resim Başarıyla Yüklendi", Toast.LENGTH_SHORT).show()
                            if(fileNameText.toString()!=oldimagename){
                                deleteOldImage(oldimagename)
                                updateCategory(parrentCategory)
                            }

                        }
                    }


                )

                ?.addOnFailureListener(OnFailureListener { e ->
                    print(e.message)
                })
        }

    }
    fun deleteOldImage(deleteimagename:String){

    var fileName = deleteimagename +".jpg"

    val storageUrl = ("Categories/"+fileName)
    val storageReference = FirebaseStorage.getInstance().reference.child(storageUrl)
    storageReference.delete().addOnSuccessListener {
        Toast.makeText(applicationContext, "Çöp Dosyalar Başarıyla Silindi", Toast.LENGTH_SHORT).show()

    }
}

    fun deleteOldCategory(oldCategory: CategoryData){
        val db = FirebaseFirestore.getInstance()
        if(isSub==false){
            db.collection("Categories").document(""+oldCategory.catId)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Lütfen Bekleyiniz Çöp Dosyalar Siliniyor", Toast.LENGTH_SHORT).show()
                    deleteOldImage(oldimagename)
                }
        }

    }

    fun updateCategory(oldCategory: CategoryData){
        val editTextCategoryEditName:TextView=findViewById(R.id.editTextCategoryEditName)
        val editTextTextCategoryEditImageName:TextView=findViewById(R.id.editTextCategoryEditImageName)
        val db = FirebaseFirestore.getInstance()

        val newCategoryName=editTextCategoryEditName.text.toString()
        val newCategoryImageName=editTextTextCategoryEditImageName.text.toString()

        if(isSub==false){
            val docref = db.collection("Categories").document(""+oldCategory.catId)

            docref
                .update("catName", ""+newCategoryName,"catImagePath",""+imageUrl)


                .addOnSuccessListener {
                    if(oldimagename!=editTextTextCategoryEditImageName.toString()){
                        Toast.makeText(applicationContext, "Çöp Dosyalar Siliniyor", Toast.LENGTH_SHORT).show()
                        deleteOldImage(oldimagename)

                    }

                }
        }


                                            }

    fun addSubCategory(oldCategory: CategoryData){

        val editTextCategoryEditName:TextView=findViewById(R.id.editTextCategoryEditName)
        val editTextTextCategoryEditImageName:TextView=findViewById(R.id.editTextCategoryEditImageName)
        val db = FirebaseFirestore.getInstance()

        val subCategoryName=editTextCategoryEditName.text.toString()
        val subCategoryImageName=editTextTextCategoryEditImageName.text.toString()

        val newSubCategory=CategoryData()
        val parrentCategory=oldCategory

        with(newSubCategory){
            newSubCategory.catName=subCategoryName
            newSubCategory.catParrentId=parrentCategory.catId
            newSubCategory.catImagePath=imageUrl
            newSubCategory.catId=parrentCategory.catId+(""+subCategoryName)
        }
        val arrayList:ArrayList<CategoryData>  = ArrayList()
        if(parrentCategory.subCat!=null)
            arrayList.addAll(parrentCategory.subCat!!)
            arrayList.add(newSubCategory)
            parrentCategory.subCat = arrayList



        val docref = db.collection("Categories").document(""+parrentCategory.catId)
            .set(parrentCategory)

    }

    private fun uploadImageToFirebaseUpdateSub(fileUri: Uri?) {
        if (fileUri != null) {

            val fileNameText = findViewById<EditText>(R.id.editTextCategoryEditImageName)
            var filename=fileNameText.text.toString()
            var fileName = filename +".jpg"


            var refStorage = FirebaseStorage.getInstance().reference.child("Categories/$fileName")

            refStorage.putFile(fileUri)
                .addOnSuccessListener(
                    OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                            imageUrl = it.toString()
                            Toast.makeText(applicationContext, "Resim Başarıyla Yüklendi", Toast.LENGTH_SHORT).show()
                            if(fileNameText.toString()!=oldimagename){
                                deleteOldImage(oldimagename)
                                updateCategorySub()

                            }

                        }
                    }


                )

                ?.addOnFailureListener(OnFailureListener { e ->
                    print(e.message)
                })
        }

    }



    fun updateCategorySub(){
        val editTextCategoryEditName:TextView=findViewById(R.id.editTextCategoryEditName)
        val categorySubName=editTextCategoryEditName.text.toString()
        val db = FirebaseFirestore.getInstance()
        var arrayList:ArrayList<CategoryData>  = ArrayList()
        val newCategory=CategoryData()

        val docref = db.collection("Categories").document(""+subCategoryParrentId)
            docref.get()
            .addOnSuccessListener { document ->
                newCategory.catId= document.getString("catId")
                newCategory.catName=document.getString("catName")
                newCategory.catImagePath=document.getString("catImagePath")
                newCategory.catParrentId=document.getString("catParrentId")


                val categoryList=CategoryWSHelper.categoryHashMapListToCategoryDataList(document.get("subCat") as List<HashMap<String, String>>)
                     categoryList.get(subCategoryIndex).catName=categorySubName
                     categoryList.get(subCategoryIndex).catImagePath=imageUrl
                     newCategory.subCat=categoryList

               /* arrayList.addAll(categoryList)
                arrayList.removeAt(subCategoryIndex)
                newCategory.subCat=arrayList*/

                val docrefupdate = db.collection("Categories").document(""+subCategoryParrentId)
                    .set(newCategory)
            }








    }



    }
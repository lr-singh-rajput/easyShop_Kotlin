package com.SapnokitPaat.EaseShop.utils

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

fun uploadImage(uri: Uri,itemImage: String,callback:(String?)->Unit ){
    val USER_ID = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    var imageUrl:String
FirebaseStorage.getInstance().getReference(itemImage)
    .child(USER_ID)
    .child(UUID.randomUUID().toString())
    .putFile(uri)
    .addOnSuccessListener {
        it.storage.downloadUrl.addOnSuccessListener {
            imageUrl=it.toString()
            callback(imageUrl)
        }
    }

}
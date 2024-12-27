package com.SapnokitPaat.EaseShop.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.SapnokitPaat.EaseShop.Model.supplierModel
import com.SapnokitPaat.EaseShop.databinding.ActivityAddsupplierBinding
import com.SapnokitPaat.EaseShop.utils.SUPPLIERS
import com.SapnokitPaat.EaseShop.utils.USER_DATA

class addsupplierActivity : AppCompatActivity() {
    private val binding : ActivityAddsupplierBinding by lazy {
        ActivityAddsupplierBinding.inflate(layoutInflater)
    }
    private val db = Firebase.firestore
    private lateinit var S_Model:supplierModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //set default country code
        binding.savesuppliercontry.setText("91").toString()
// not show error only red line
        S_Model= supplierModel()
        binding.backbtn.setOnClickListener {
            finish()
        }
        binding.saveSupplierbtn.setOnClickListener {
           if ( binding.savenamesupplier.text.toString().isEmpty()

           ){
               Toast.makeText(this, "Pleasse fill the all field", Toast.LENGTH_SHORT).show()

           }else{
               S_Model.S_name=binding.savenamesupplier.text.toString()
               S_Model.S_county=binding.savesuppliercontry.text.toString()
               S_Model.S_number=binding.savesupplierNo.text.toString()

               val savesupplierNo:String =binding.savesupplierNo.text.toString()
               /*
               if (!isValidPhoneNumber(savesupplierNo)){
                   Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT)
                       .show()
                   return@setOnClickListener
               }*/

               /*
              val show= binding.textView39.text.toString()
              val no= binding.savesupplierNo.text.toString()
              val savesuppliercode= binding.savesuppliercode.text.toString()

               var   MobileNumber=savesuppliercode + no
               binding.textView52.text = "+$MobileNumber"
               val format= binding.textView52.text.toString()
               binding.textView39.text = "$format"

               */

               val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
               // Add a new document with a generated ID
               val newDocumentRef = db.collection(USER_DATA)
               .document(SUPPLIERS)
               .collection(userId)
               .document()

               S_Model.S_id = newDocumentRef.id

               // Now save the model to Firestore with the generated document ID
               newDocumentRef.set(S_Model)
                   .addOnSuccessListener {

                       Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show()
                    //   Toast.makeText(this, "please reload activity", Toast.LENGTH_SHORT).show()

                   }
                   .addOnFailureListener { e ->
                       Toast.makeText(this, "Failed to save: ${e.message}", Toast.LENGTH_SHORT).show()
                   }
               finish()

           }
        }
    }
/*
    private fun isValidPhoneNumber(savesupplierNo: String): Boolean {

        val phonePattern = "^[+]?[0-9]{10,13}\$".toRegex()
        return phonePattern.matches(savesupplierNo)
    }
    */
}
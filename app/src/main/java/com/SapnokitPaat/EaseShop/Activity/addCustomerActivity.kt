package com.SapnokitPaat.EaseShop.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.SapnokitPaat.EaseShop.Model.customerModel
import com.SapnokitPaat.EaseShop.databinding.ActivityAddCustomerBinding
import com.SapnokitPaat.EaseShop.utils.CUSTOMERS
import com.SapnokitPaat.EaseShop.utils.USER_DATA

class addCustomerActivity : AppCompatActivity() {
    private val binding:ActivityAddCustomerBinding by lazy {
        ActivityAddCustomerBinding.inflate(layoutInflater)
    }

    private val db = Firebase.firestore
    private lateinit var C_Model: customerModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //set default country code
        binding.savecustomerNo2.setText("91").toString()

      //  val pass= binding.savenamecustomar
      //  val minLength = 5
        //pass.filters = arrayOf(InputFilter.LengthFilter(minLength))

        C_Model= customerModel()
        binding.CBackbtn.setOnClickListener {
            finish()
        }



        binding.savecustomerbtn.setOnClickListener {
            if ( binding.savenamecustomar.text.toString().isEmpty()

            ){
                Toast.makeText(this, "Pleasse fill the all field", Toast.LENGTH_SHORT).show()

            }else{
                C_Model.C_name=binding.savenamecustomar.text.toString()
                C_Model.C_country=binding.savecustomerNo2.text.toString()
               // C_Model.C_gstNo=binding.savegstcustomar.text.toString()
                C_Model.C_number=binding.savecustomerNo.text.toString()

                val saveCustomerNo:String =binding.savecustomerNo.text.toString()
                /*
                if (!isValidPhoneNumber(saveCustomerNo)){
                    Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }*/


                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                // Add a new document with a generated ID
                val newDocumentRef = db.collection(USER_DATA)
                                    .document(CUSTOMERS)
                                    .collection(userId)
                                    .document()

                C_Model.C_id = newDocumentRef.id

                // Now save the model to Firestore with the generated document ID
                newDocumentRef.set(C_Model)
                    .addOnSuccessListener {
                        finish()
                        Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show()
                     //   Toast.makeText(this, "click top reload button activity", Toast.LENGTH_SHORT).show()

                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to save: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                finish()
            }
        }


    }
/*
    private fun isValidPhoneNumber(saveCustomerNo: String): Boolean {

        val phonePattern = "^[+]?[0-9]{10,13}\$".toRegex()
        return phonePattern.matches(saveCustomerNo)
    }
    */
}
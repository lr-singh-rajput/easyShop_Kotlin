package com.SapnokitPaat.EaseShop.ActivityBills

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.SapnokitPaat.EaseShop.Model.billCutomerModel
import com.SapnokitPaat.EaseShop.databinding.ActivityAddCustomerBillBinding
import com.SapnokitPaat.EaseShop.utils.BILL
import com.SapnokitPaat.EaseShop.utils.USER_DATA
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class addCustomerBillActivity : AppCompatActivity() {
    private val binding: ActivityAddCustomerBillBinding by lazy {
        ActivityAddCustomerBillBinding .inflate(layoutInflater)
    }
    private val db = Firebase.firestore
    private lateinit var C_Model: billCutomerModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //set default country code
        binding.savecustomerCountryBill.setText("91").toString()

        binding.billCBackbtn.setOnClickListener {
            finish()
        }
        C_Model= billCutomerModel()

        binding.savecustomerbtnbillC.setOnClickListener {
            if ( binding.savenamecustomarbillC.text.toString().isEmpty()
            ){
                Toast.makeText(this, "Pleasse fill the name ", Toast.LENGTH_SHORT).show()

            }else{
                C_Model.C_name=binding.savenamecustomarbillC.text.toString()
                C_Model.C_country=binding.savecustomerCountryBill.text.toString()
                C_Model.C_number=binding.savecustomerNobillC.text.toString()
                C_Model.date = System.currentTimeMillis()// Automatically detect current date and time

                val savesupplierNo:String =binding.savecustomerNobillC.text.toString()
                /*
                if (!isValidPhoneNumber(savesupplierNo)){
                    Toast.makeText(this, "Invalid phon number", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
                */




                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                // Add a new document with a generated ID
                val newDocumentRef = db
                    .collection(USER_DATA)
                    .document(BILL)
                    .collection(userId)
                    .document()


                C_Model.C_id = newDocumentRef.id

                // Now save the model to Firestore with the generated document ID
                newDocumentRef.set(C_Model)
                    .addOnSuccessListener {
                        // Send customer ID to next activity

                        Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show()

                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to save: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                val intent = Intent(this, createBillActivity::class.java)
                intent.putExtra("CUSTOMERBILL_ID", C_Model.C_id)
                intent.putExtra("CUSTOMERBILL_NAME", C_Model.C_name)
                startActivity(intent)
                finish()
            }
        }





    }

    private fun isValidPhoneNumber(savesupplierNo: String): Boolean {
        val phonePattern = "^[+]?[0-9]{10,13}\$".toRegex()
        return phonePattern.matches(savesupplierNo)
    }
}
package com.SapnokitPaat.EaseShop.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.SapnokitPaat.EaseShop.Model.transactionModel
import com.SapnokitPaat.EaseShop.databinding.ActivityAddcustomarTransactionBinding
import com.SapnokitPaat.EaseShop.utils.CUSTOMERS
import com.SapnokitPaat.EaseShop.utils.USER_DATA
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class addcustomarTransactionActivity : AppCompatActivity() {
     private val binding: ActivityAddcustomarTransactionBinding by lazy {
         ActivityAddcustomarTransactionBinding.inflate(layoutInflater)
     }

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault()).apply {
            timeZone = TimeZone.getDefault() // Use the device's default timezone
        }


        binding.ATBackACTbtn.setOnClickListener {
            finish()
        }


        val customerId = intent.getStringExtra("CUSTOMER_ID")



        binding.saveCustomartransactionbtn.setOnClickListener {

            val got = binding.saveGetCostemarTransaction.text.toString()
            val gave = binding.saveGaveCostomarTransaction.text.toString()
            if (gave.isNotEmpty() || got.isNotEmpty()){

                try {
                    val transaction = transactionModel().apply {
                        T_give = binding.saveGetCostemarTransaction.text.toString()
                        T_got = binding.saveGaveCostomarTransaction.text.toString()
                        T_detail = binding.savedetailCostomarTransaction.text.toString()
                        T_billNo = binding.savebillCustomarTransaction.text.toString()
                        mfgDt = System.currentTimeMillis()// Automatically detect current date and time
                    }
                    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                    val newDocumentRef = db.collection(USER_DATA)
                        .document(CUSTOMERS)
                        .collection(userId)
                        .document(customerId!!)
                        .collection(CUSTOMERS)
                        .document()


                    if (newDocumentRef != null) {
                        transaction.T_id = newDocumentRef.id
                    }

                    // Now save the model to Firestore with the generated document ID
                    if (newDocumentRef != null) {
                        newDocumentRef.set(transaction)
                            .addOnSuccessListener {
                                finish()
                                Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT)
                                    .show()


                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    this,
                                    "Failed to save: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                } catch (e: ParseException) {
                    val expectedFormat = "yyyy-MM-dd hh:mm AM/PM"
                    Toast.makeText(
                        this@addcustomarTransactionActivity,
                        "Invalid date format. Please enter a date in the format: $expectedFormat",
                        Toast.LENGTH_LONG
                    ).show()
                }
            finish()
        }else{
            Toast.makeText(this, "enter field", Toast.LENGTH_SHORT).show()
        }
        }

    }


}
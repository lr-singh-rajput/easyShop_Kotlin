package com.SapnokitPaat.EaseShop.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.SapnokitPaat.EaseShop.Model.transactionModel
import com.SapnokitPaat.EaseShop.databinding.ActivityAddTransactionBinding
import com.SapnokitPaat.EaseShop.utils.SUPPLIERS
import com.SapnokitPaat.EaseShop.utils.USER_DATA
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class addTransactionActivity : AppCompatActivity() {



    private val binding: ActivityAddTransactionBinding by lazy {
        ActivityAddTransactionBinding.inflate(layoutInflater)
    }



    private val db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault()).apply {
            timeZone = TimeZone.getDefault() // Use the device's default timezone
        }



        binding.ATBackbtn.setOnClickListener {
            finish()
        }




        // Yahaan ID receive karenge
        val supplierId = intent.getStringExtra("SUPPLIER_ID")

       binding.savetransactionbtn.setOnClickListener {
           val got = binding.saveGetTransaction.text.toString()
           val gave = binding.saveGaveTransaction.text.toString()
           if (gave.isNotEmpty() || got.isNotEmpty()){
               try {
                   val transaction = transactionModel().apply {
                       T_give = binding.saveGetTransaction.text.toString()
                       T_got = binding.saveGaveTransaction.text.toString()
                       T_detail = binding.savedetailTransaction.text.toString()
                       T_billNo = binding.savebillTransaction.text.toString()
                       mfgDt =
                           System.currentTimeMillis()// Automatically detect current date and time
                   }

                   val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                   val newDocumentRef =
                       db.collection(USER_DATA).document(SUPPLIERS).collection(userId)
                           .document(supplierId!!)
                           .collection(SUPPLIERS).document()



                   if (newDocumentRef != null) {
                       transaction.T_id = newDocumentRef.id
                   }

                   // Now save the model to Firestore with the generated document ID
                   if (newDocumentRef != null) {
                       newDocumentRef.set(transaction)
                           .addOnSuccessListener {
                               finish()
                               Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show()
                            //   Toast.makeText(this, "please reload activity", Toast.LENGTH_SHORT).show()


                           }
                           .addOnFailureListener { e ->
                               Toast.makeText(
                                   this,
                                   "Failed to save: ${e.message}",
                                   Toast.LENGTH_SHORT
                               ).show()
                           }
                       finish()
                   }
               } catch (e: ParseException) {
                   val expectedFormat = "yyyy-MM-dd hh:mm AM/PM"
                   Toast.makeText(
                       this@addTransactionActivity,
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
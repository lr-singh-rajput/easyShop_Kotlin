package com.SapnokitPaat.EaseShop.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.SapnokitPaat.EaseShop.Model.transactionModel
import com.SapnokitPaat.EaseShop.databinding.ActivityUpdateTrancationCBinding
import com.SapnokitPaat.EaseShop.utils.CUSTOMERS
import com.SapnokitPaat.EaseShop.utils.USER_DATA
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class updateTrancationCActivity : AppCompatActivity() {
    private val binding:ActivityUpdateTrancationCBinding by lazy {
        ActivityUpdateTrancationCBinding.inflate(layoutInflater)
    }
    private lateinit var T_Model: transactionModel
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var cardProgressBar: CardView
    private lateinit var buttonUpdate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadingProgressBar = binding.loadingPrograssBarCTU
        loadingProgressBar .visibility = ProgressBar.VISIBLE

        cardProgressBar = binding.cardloadingPrograssBarCTU
        cardProgressBar .visibility = CardView.VISIBLE

        buttonUpdate = binding.updatebtnUtc
        buttonUpdate .visibility =Button.INVISIBLE



        val transactionaId = intent.getStringExtra("TRANSACTION_ID")
        val costomarId = intent.getStringExtra("CUSTOMER_ID")
        T_Model= transactionModel()

        binding.updatebtnUtc.setOnClickListener {

            T_Model.T_got=binding.gotUtc.text.toString()
            T_Model.T_give=binding.gaveUtc.text.toString()
            T_Model.T_billNo=binding.billnoUtc.text.toString()
            T_Model.T_detail=binding.detailUtc.text.toString()
            // Convert the DateTime string to Long and set it in newData
            val dateTimeString = binding.datetimeUtc.text.toString()
            val pattern = "yyyy-MM-dd hh:mm a"
            val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())

            try {
                val date = dateFormat.parse(dateTimeString)
                val mfgDt = date?.time ?: 0L
                T_Model.mfgDt = mfgDt
            } catch (e: ParseException) {
                // Handle date parsing exception, e.g., show an error message
                Toast.makeText(this, "Error parsing date: ${e.message}", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // Exit the click listener to prevent saving invalid data
            }

            if ( transactionaId!= null) {
                T_Model.T_id=transactionaId
            }

            // Access Firestore instance
            val db = FirebaseFirestore.getInstance()
            val userId = FirebaseAuth.getInstance().currentUser!!.uid

            if (transactionaId != null) {
                if (costomarId != null) {
                    db.collection(USER_DATA)
                        .document(CUSTOMERS)
                        .collection(userId)
                        .document(costomarId)
                        .collection(CUSTOMERS)
                        .document(transactionaId)
                        .set(T_Model)
                        .addOnSuccessListener { documentSnapshot ->

                            Toast.makeText(this, "update customer", Toast.LENGTH_SHORT).show()

                        }

                        .addOnFailureListener { exception ->
                            // Handle any errorsIN
                            Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                    finish()
                }

        }



        }

        binding.cancellbtnUtc.setOnClickListener {
            Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show()
            finish()
        }

        // show data
        // Access Firestore instance
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

// Assuming 'users' is the collection name
//val userRef = db.collection(USER_DATA).document(LOGINDETAILS).collection(userId)
        if (costomarId != null) {
            if (transactionaId != null) {
                db.collection(USER_DATA)
                    .document(CUSTOMERS)
                    .collection(userId)
                    .document(costomarId)
                    .collection(CUSTOMERS)
                    .document(transactionaId)
                    .get()
                    .addOnSuccessListener { documentSnapshot ->
                        buttonUpdate .visibility = Button.VISIBLE

                        loadingProgressBar .visibility = ProgressBar.INVISIBLE
                        cardProgressBar .visibility = CardView.INVISIBLE
                        if (documentSnapshot.exists()) {
                            val user = documentSnapshot.toObject(transactionModel::class.java)
                            if (user != null) {
                                // Assuming you want to display information from the document
                                // Update UI
                                binding.gotUtc.setText( user.T_got)
                                binding.gaveUtc.setText (user.T_give)
                                binding.billnoUtc.setText ( user.T_billNo)
                                binding.detailUtc.setText ( user.T_detail)
                                // Convert milliseconds to formatted date and time string
                                val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
                                val formattedDateTime = dateFormat.format(Date(user.mfgDt))
                                binding.datetimeUtc.setText(formattedDateTime)

                            } else {
                                // Handle the case where the user conversion is null
                                Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // Handle the case where the document does not exist
                            Toast.makeText(this, "Document does not exist", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { exception ->
                        buttonUpdate .visibility = Button.VISIBLE


                        loadingProgressBar .visibility = ProgressBar.INVISIBLE
                        cardProgressBar .visibility = CardView.INVISIBLE
                        // Handle any errors
                      //  Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
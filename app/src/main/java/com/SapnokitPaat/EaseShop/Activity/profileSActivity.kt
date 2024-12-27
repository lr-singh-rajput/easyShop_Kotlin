package com.SapnokitPaat.EaseShop.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.SapnokitPaat.EaseShop.Model.supplierModel
import com.SapnokitPaat.EaseShop.databinding.ActivityProfileSactivityBinding
import com.SapnokitPaat.EaseShop.utils.SUPPLIERS
import com.SapnokitPaat.EaseShop.utils.USER_DATA
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class profileSActivity : AppCompatActivity() {
    private val binding:ActivityProfileSactivityBinding by lazy {
        ActivityProfileSactivityBinding.inflate(layoutInflater)
    }
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var buttonUpdate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //restart activity
        binding.PSrestartbtn.setOnClickListener {
            val intent=intent
            finish()
            startActivity(intent)
        }

        loadingProgressBar = binding.loadingPrograssBarSPT
        loadingProgressBar .visibility = ProgressBar.VISIBLE


        buttonUpdate = binding.deletebtnSd
        buttonUpdate .visibility =Button.INVISIBLE

        // Retrieve the ID passed from MainActivity
        val supplierId = intent.getStringExtra("SUPPLIER_ID")

        binding.backbtnsp.setOnClickListener {

            finish()

        }


        // Access Firestore instance
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        /* / Configure Firestore settings for offline persistence
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings = settings
*/
// Assuming 'users' is the collection name
//val userRef = db.collection(USER_DATA).document(LOGINDETAILS).collection(userId)



        binding.updatebtnSp.setOnClickListener {
            var intent = Intent(this, updatePSActivity::class.java)
            //   intent.putExtra("TRANSACTION_ID", transactionModel.T_id)
            intent.putExtra("SUPPLIER_ID", supplierId)
            // intent.putExtra("CUSTOMER_NAME", costomarname)
            startActivity(intent)
        }


        binding.deletebtnSd.setOnClickListener {


            var dialog = AlertDialog.Builder(this)
            dialog.setTitle("delete")
            dialog.setMessage("This will delete the supplier and transaction ")

            dialog.setPositiveButton("Yas") {
                    dialogInterface, which ->
                if (supplierId != null) {
                    db.collection(USER_DATA)
                        .document(SUPPLIERS)
                        .collection(userId)
                        .document(supplierId)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "Supplier deleted successfully", Toast.LENGTH_SHORT).show()
                            // Optionally, you can update UI or perform any other actions after deletion




                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "Failed to delete customer: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                    setResult(RESULT_OK)
                    finish()

                } else {
                    Toast.makeText(this, "No customer ID available", Toast.LENGTH_SHORT).show()
                }

            }
            dialog.setNegativeButton("No"){
                    dialogInterface,which ->
                Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show()
            }
            var alertDialog:AlertDialog= dialog.create()
            alertDialog.setCancelable(false)
            alertDialog.show()


        }

        setupRecyclerview()


    }

    override fun onStart() {
        super.onStart()
        setupRecyclerview()
    }

    private fun setupRecyclerview() {
        val supplierId = intent.getStringExtra("SUPPLIER_ID")
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        if (supplierId != null) {
            db.collection(USER_DATA)
                .document(SUPPLIERS)
                .collection(userId)
                .document(supplierId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    buttonUpdate .visibility = Button.VISIBLE
                    loadingProgressBar .visibility = ProgressBar.INVISIBLE

                    if (documentSnapshot.exists()) {
                        val user = documentSnapshot.toObject(supplierModel::class.java)
                        if (user != null) {
                            // Assuming you want to display information from the document
                            // Update UI
                            binding.shownameSp.text = user.S_name
                            // binding.shownumberSp.text = user.S_number.toString()
                            val number= user.S_number.toString()
                            val code= user.S_county
                            binding.shownumberSp.text = "$code$number"

                            binding.showareaSp.text = user.S_area
                            binding.showpinSp.text = user.S_pincode
                            binding.showcitySp.text = user.S_city
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

                    // Handle any errors
                    Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
        else{
            Toast.makeText(this, "not Id Id", Toast.LENGTH_SHORT).show()
        }
    }
}
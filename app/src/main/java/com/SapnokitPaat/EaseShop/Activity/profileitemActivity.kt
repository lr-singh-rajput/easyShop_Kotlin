package com.SapnokitPaat.EaseShop.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.SapnokitPaat.EaseShop.Model.itemModel
import com.SapnokitPaat.EaseShop.R
import com.SapnokitPaat.EaseShop.databinding.ActivityProfileitemBinding
import com.SapnokitPaat.EaseShop.utils.ITEMS
import com.SapnokitPaat.EaseShop.utils.USER_DATA
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class profileitemActivity : AppCompatActivity() {
    private val binding:ActivityProfileitemBinding by lazy {
        ActivityProfileitemBinding.inflate(layoutInflater)
    }
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var buttonUpdate: Button
    private lateinit var add: Button
    private lateinit var remove: Button
    private lateinit var addededittext:EditText
    private lateinit var removeededittext:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //restart activity
        binding.PIrestartbtn.setOnClickListener {
            val intent=intent
            finish()
            startActivity(intent)
        }

        loadingProgressBar = binding.loadingPrograssBarIPT
        loadingProgressBar .visibility = ProgressBar.VISIBLE


        buttonUpdate = binding.deleteIp
        buttonUpdate .visibility = Button.INVISIBLE

        add = binding.addbtn
        add .visibility = Button.INVISIBLE

        remove = binding.removebtn
        remove .visibility = Button.INVISIBLE

        addededittext = binding.addstockedittext
        addededittext.visibility=EditText.INVISIBLE

        removeededittext = binding.removeitemedittext
        removeededittext.visibility=EditText.INVISIBLE


        val itemId = intent.getStringExtra("ITEM_ID")




        binding.updateIp.setOnClickListener {
            var intent = Intent(this, updateItemActivity::class.java)
            intent.putExtra("ITEM_ID", itemId)
            startActivity(intent)
        }

        binding.backitemIp.setOnClickListener {
            finish()
        }

        binding.addstockbtn.setOnClickListener {
            addededittext.visibility=EditText.VISIBLE
            add .visibility = Button.VISIBLE
            increaseStock()
        }
        binding.removeitembtn.setOnClickListener {
            removeededittext.visibility=EditText.VISIBLE
            remove .visibility = Button.VISIBLE
            decreaseStock()
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



        binding.deleteIp.setOnClickListener {
            var dialog = AlertDialog.Builder(this)
            dialog.setTitle("delete")
            dialog.setMessage("This will delete the item ")

            dialog.setPositiveButton("Yas"){
                dialogInterface,which ->

                if (itemId != null) {
                    db.collection(USER_DATA)
                        .document(ITEMS)
                        .collection(userId)
                        .document(itemId)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "item deleted successfully", Toast.LENGTH_SHORT).show()

                            finish()
                            // Optionally, you can update UI or perform any other actions after deletion
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "Failed to delete customer: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
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
        val itemId = intent.getStringExtra("ITEM_ID")
        // Access Firestore instance
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        if (itemId != null) {
            db.collection(USER_DATA)
                .document(ITEMS)
                .collection(userId)
                .document(itemId)
                // Assuming 'users' is the name of your collection
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    buttonUpdate .visibility = Button.VISIBLE
                    loadingProgressBar .visibility = ProgressBar.INVISIBLE

                    val user = documentSnapshot.toObject(itemModel::class.java)
                    if (user != null) {
                        // Assuming you want to display information from the first document found
                        // Update UI
                        binding.shownameIp.text = user.itemname
                        binding.showbuyIp.text = user.parchassprice.toString()
                        binding.showsellIp.text = user.shellprice.toString()
                        binding.showstockIp.text = user.stock.toString()

                        // Load image using Picasso
                        //Picasso.get().load(item.imageitem).into(holder.binding.showImage)
                        // Inside your activity or fragment where you have an ImageView reference
                        Picasso.get()
                            .load(user.imageitem)
                            .placeholder(R.drawable.addimage)
                            .error(R.drawable.errorimage)
                            .into(binding.showimageIp, object : Callback {
                                override fun onSuccess() {
                                    // Image loaded successfully
                                }

                                override fun onError(e: Exception?) {
                                    // Error occurred while loading image
                                    Log.e("Picasso", "Error loading image: ${e?.localizedMessage}")
                                }
                            })
                        //
                        // binding.showid.text = user.id


                        //  break // Remove this if you want to handle all documents in the collection
                    } else {
                        // Handle the case where the user conversion is null
                        Toast.makeText(this, "not load data", Toast.LENGTH_SHORT).show()
                    }

                }
                .addOnFailureListener { exception ->
                    buttonUpdate .visibility = Button.VISIBLE
                    loadingProgressBar .visibility = ProgressBar.INVISIBLE

                    // Handle any errors
                }
        }
    }

    private fun decreaseStock() {
        binding.removebtn.setOnClickListener {
        val stock = binding.showstockIp.text.toString()
        val add = binding.removeitemedittext.text.toString()
        if (stock.isNotEmpty() && add.isNotEmpty()) {
            val Stocks = stock.toInt()
            val Adds = add.toInt()

            val newStock = Stocks - Adds
            binding.editTextText3.text = " $newStock"


            val db = FirebaseFirestore.getInstance()
            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            val itemId = intent.getStringExtra("ITEM_ID")
            if (itemId != null) {
                db.collection(USER_DATA)
                    .document(ITEMS)
                    .collection(userId)
                    .document(itemId)
                    .update("stock", newStock)
                    .addOnSuccessListener {
                        Toast.makeText(this, "successfully", Toast.LENGTH_SHORT).show()
                        removeededittext.visibility=EditText.INVISIBLE
                        remove .visibility = Button.INVISIBLE

                        // Optionally, you can update UI or perform any other actions after deletion
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(
                            this,
                            "Failed : ${exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                val intent=intent
                finish()
                startActivity(intent)
                }


            }else{
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun increaseStock() {
        binding.addbtn.setOnClickListener {
        val stock = binding.showstockIp.text.toString()
        val add = binding.addstockedittext.text.toString()
        if (stock.isNotEmpty() && add.isNotEmpty()) {
            val Stocks = stock.toInt()
            val Adds = add.toInt()

            val newStock = Stocks + Adds

            binding.editTextText3.text = " $newStock"
            //update stock

            val db = FirebaseFirestore.getInstance()
            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            val itemId = intent.getStringExtra("ITEM_ID")
            if (itemId != null) {
                db.collection(USER_DATA)
                    .document(ITEMS)
                    .collection(userId)
                    .document(itemId)
                    .update("stock", newStock)
                    .addOnSuccessListener {

                        Toast.makeText(this, "successfully", Toast.LENGTH_SHORT).show()
                        addededittext.visibility=EditText.INVISIBLE
                       // add.visibility = Button.INVISIBLE


                        // Optionally, you can update UI or perform any other actions after deletion
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(
                            this,
                            "Failed : ${exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                val intent=intent
                finish()
                startActivity(intent)
            }

            }
        }
    }
}
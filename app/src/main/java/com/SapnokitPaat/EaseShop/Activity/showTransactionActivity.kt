package com.SapnokitPaat.EaseShop.Activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.SapnokitPaat.EaseShop.Adaptor.transactionAdaptor
import com.SapnokitPaat.EaseShop.Model.supplierModel
import com.SapnokitPaat.EaseShop.Model.transactionModel
import com.SapnokitPaat.EaseShop.Model.userModel
import com.SapnokitPaat.EaseShop.R
import com.SapnokitPaat.EaseShop.databinding.ActivityShowTransactionBinding
import com.SapnokitPaat.EaseShop.utils.LOGINDETAILS
import com.SapnokitPaat.EaseShop.utils.SUPPLIERS
import com.SapnokitPaat.EaseShop.utils.USER_DATA
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreSettings

@Suppress("DEPRECATION")
class showTransactionActivity : AppCompatActivity() {
    private val binding:ActivityShowTransactionBinding by lazy {
        ActivityShowTransactionBinding.inflate(layoutInflater)
    }

    private var db = FirebaseFirestore.getInstance()
    private lateinit var loadingProgressBar: ProgressBar

    //  DETAIL ACTIVITY AND THIS ACTIVITY FINISH THEN USER CLICK DELETE BUTTON
    private  val REQUEST_CODE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadingProgressBar = binding.loadingPrograssBarST


        // Retrieve the ID passed from MainActivity
        val supplierId = intent.getStringExtra("SUPPLIER_ID")
        val supplierName = intent.getStringExtra("SUPPLIER_NAME")
        // Retrieve the number from the intent using getLongExtra()
        val supplierNumber = intent.getLongExtra("SUPPLIER_NUMBER", 0L)



        //binding.showidS.text="ID: $supplierId"
        //binding.shownameS.text="$supplierName"


        binding.backT.setOnClickListener {
            finish()
        }

        binding.showsum.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                updateFirestore("s_money",s.toString())
            }

        })

        //restart activity
        binding.STRestartbtn.setOnClickListener {
            val intent=intent
            finish()
            startActivity(intent)
        }
        binding.updateandseleteS.setOnClickListener {

            val intent = Intent(this, profileSActivity::class.java)
            intent.putExtra("SUPPLIER_ID", supplierId) // Yahi ID aage bhej rahe hain
            startActivityForResult(intent, REQUEST_CODE)
        }

        binding.addTransaction.setOnClickListener {

            val intent = Intent(this, addTransactionActivity::class.java)
            intent.putExtra("SUPPLIER_ID", supplierId) // Yahi ID aage bhej rahe hain
            startActivity(intent)
        }

        binding.sendsmsS.setOnClickListener {

                val name = binding.shownameS.text.toString()
                val manny = binding.showsum.text.toString()
                val number = binding.textView62.text.toString()
                val onername = binding.nameoner.text.toString()
                val shopname = binding.shopname.text.toString()
                if (manny.isNotEmpty() && name.isNotEmpty() && number.isNotEmpty() ){
                    sendSMS(number,manny,name,shopname)

                }else{
                    Toast.makeText(this, "please enter all value then try again", Toast.LENGTH_SHORT).show()
                }

        }
        binding.sendWhatsappS.setOnClickListener {
            if(isNetworkAvailable()){
                val name = binding.shownameS.text.toString()
                val manny = binding.showsum.text.toString()
                val number = binding.textView62.text.toString()
                val onername = binding.nameoner.text.toString()
                val shopname = binding.shopname.text.toString()
                if (manny.isNotEmpty() && name.isNotEmpty() && number.isNotEmpty() ){
                    whatsappsendReminder(number,manny,name,shopname)

                }else{
                    Toast.makeText(this, "please enter all value then try again", Toast.LENGTH_SHORT).show()
                }
            }else{
                // Show alert to check internet connection if user is offline
                // Function to show warning dialog
                val view = layoutInflater.inflate(R.layout.dialog_box_internet_off_whatsapp,null)

                val builder =  AlertDialog.Builder(this)
                builder.setView(view)

                val alertDialog = builder.create()
                //set the background to transparent
                alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                alertDialog.show()
            }

        }


        /*  / Initialize userList as a mutable list
        T_List = mutableListOf()
        adapter = transactionAdaptor(T_List)
        binding.rvshowT.adapter = adapter


        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        db.collection(USER_DATA).document(SUPPLIERS).collection(userId).document(supplierId!!)
            .collection(SUPPLIERS)

//         db.collection(SUPPLIERS).document(supplierId!!).collection(userId)
             .orderBy("mfgDt",Query.Direction.DESCENDING)
             .get()

     //   db.collection(USER_DATA).document(ITEMS).collection(userId).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val item = document.toObject(transactionModel::class.java)
                    T_List.add(item)
                }
                adapter.notifyDataSetChanged()



            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to retrieve items", Toast.LENGTH_SHORT).show()

            }


        // Now set the adapter to the RecyclerView
        binding.rvshowT.adapter = adapter

         */
        // show busnessdetail
        showBusnessDtail()

        //show suplier data name
        shownamesupplier()

        // show rv transaction
        setupC_RecyclerView()

    }


    private fun updateFirestore(field: String, value: Any) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val supplierId = intent.getStringExtra("SUPPLIER_ID")
      //  val costomarId = intent.getStringExtra("CUSTOMER_ID")

        db.collection(USER_DATA)
            .document(SUPPLIERS)
            .collection(userId)
            .document(supplierId!!)
            .update(field,value)
            .addOnSuccessListener {
                // Toast.makeText(this, "update successfuly", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->


                Toast.makeText(this, "Error not update total Amaunt ", Toast.LENGTH_LONG).show()
            }
    }
    private fun sendSMS(number: String, manny: String, name: String, shopname: String){
        val message = "Dear Supplier $name ,\n  ShopName $shopname,\n     your payment of $manny is pending "

        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("smsto:$number")
        intent.putExtra("sms_body", message)

        try {
            startActivity(intent)
        }catch (e: Exception){
            Toast.makeText(this, "Faild to send sms", Toast.LENGTH_SHORT).show()
        }


        //send sms from this app direct number
        /*
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(number,null,message,null,null)
            Toast.makeText(this, "sms sent successfully", Toast.LENGTH_SHORT).show()
        }catch (e: Exception){
            Toast.makeText(this, "sms sending failed", Toast.LENGTH_SHORT).show()

        }
        */
    }

    private fun whatsappsendReminder(number: String, manny: String, name: String, shopname: String) {
        val massage = " Dear supplier $name ji ,\n ShopName $shopname ,\n      your payment of $manny is pending "
        val uri = Uri.parse("https://wa.me/$number?text=${Uri.encode(massage)}")
        val intent = Intent(Intent.ACTION_VIEW, uri)

        try {
            startActivity(intent)
        }catch (e:Exception){
            Toast.makeText(this, "whatsapp error check your whatsapp app", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(android.content.Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnectedOrConnecting
        }
    }

    override fun onStart() {
        super.onStart()
        showBusnessDtail()
        shownamesupplier()
        setupC_RecyclerView()


    }
    private fun showBusnessDtail() {
        // start show business detail code

        // Access Firestore instance
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid


        // Assuming 'users' is the collection name
        val userRef = db.collection(USER_DATA)
            .document(LOGINDETAILS)
            .collection("user")
            .document(userId)

        // Assuming 'users' is the name of your collection
        userRef.get()
            .addOnSuccessListener { querySnapshot ->


                val user = querySnapshot.toObject(userModel::class.java)
                if (user != null) {
                    // Assuming you want to display information from the first document found
                    // Update UI
                    binding.nameoner.text = user.U_name
                    binding.shopname.text = user.U_shopName
                    //binding.usernumberB.text = user.U_number

                } else {
                    // Handle the case where the user conversion is null
                }

            }
            .addOnFailureListener { exception ->

                // Handle any errors
            }

        //end show business detail code

    }

    // show supplier name
    private fun shownamesupplier() {
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

                    loadingProgressBar .visibility = ProgressBar.INVISIBLE

                    if (documentSnapshot.exists()) {
                        val user = documentSnapshot.toObject(supplierModel::class.java)
                        if (user != null) {
                            // Assuming you want to display information from the document
                            // Update UI
                            binding.shownameS.text = user.S_name
                            val number = user.S_number
                            binding.textView62.text="+91$number"

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

                    loadingProgressBar .visibility = ProgressBar.INVISIBLE

                    // Handle any errors
                    Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
        else{
            Toast.makeText(this, "not Id Id", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupC_RecyclerView() {
        loadingProgressBar .visibility = ProgressBar.VISIBLE

        val supplierId = intent.getStringExtra("SUPPLIER_ID")
        val supplierName = intent.getStringExtra("SUPPLIER_NAME")
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        // Configure Firestore settings for offline persistence

        val settings = FirebaseFirestoreSettings.Builder()
           .setPersistenceEnabled(true)
           .build()
        db.firestoreSettings = settings


        db.collection(USER_DATA)
            .document(SUPPLIERS)
            .collection(userId)
            .document(supplierId!!)
            .collection(SUPPLIERS)

            .orderBy("mfgDt", Query.Direction.DESCENDING)

            .get()
            .addOnSuccessListener { result ->
                val supplierId = intent.getStringExtra("SUPPLIER_ID")
                val supplierName = intent.getStringExtra("SUPPLIER_NAME")
                val userId = FirebaseAuth.getInstance().currentUser!!.uid
                loadingProgressBar .visibility = ProgressBar.INVISIBLE

                val item = result.toObjects(transactionModel::class.java)
                binding.rvshowT.layoutManager = LinearLayoutManager(this)
                val adapter = transactionAdaptor(item) { transactionModel ->
                    // Handle click event
                    val intent = Intent(this@showTransactionActivity, EntryDetailSActivity::class.java)
                    // intent.putExtra("CUSTOMER_NAME", itemModel.itemname)
                    intent.putExtra("TRANSACTION_ID", transactionModel.T_id)
                    intent.putExtra("SUPPLIER_ID", supplierId)
                    intent.putExtra("SUPPLIER_NAME", supplierName)
                    startActivity(intent)

                }
                binding.rvshowT.adapter = adapter
                // Calculate total T_got and T_give
                var totalGot = 0
                var totalGive = 0
                for (transaction in item) {
                    totalGot += transaction.T_got.toIntOrNull() ?: 0
                    totalGive += transaction.T_give.toIntOrNull() ?: 0
                }
                // Calculate difference between total T_got and total T_give
                val totalDifference =totalGive -  totalGot

                // Set total to TextView
                binding.showsum.text = "$totalDifference"
                // Set color according to positive or negative value
                binding.showsum.setTextColor(
                    ContextCompat.getColor(
                        this,
                        if (totalDifference >= 0) android.R.color.holo_green_dark else android.R.color.holo_red_dark
                    )
                )
            }
            .addOnFailureListener { e ->
                loadingProgressBar .visibility = ProgressBar.INVISIBLE

                if (e is FirebaseFirestoreException && e.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                    // Handle permission denied error
                 //   Toast.makeText(this, "Permission denied. Check Firestore rules.", Toast.LENGTH_LONG).show()
                } else {
                    // Handle other errors
                    Toast.makeText(this, "Error loading customers: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }

    }



    // CODE FINISH BOTH ACTIVITY CLICK DELETE BUTOTN

    override fun onActivityResult(requestCode: Int, resultCode:Int,data:Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==REQUEST_CODE && resultCode == RESULT_OK){
            finish()
        }
    }

}
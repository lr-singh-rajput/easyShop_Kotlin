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
import com.SapnokitPaat.EaseShop.Model.customerModel
import com.SapnokitPaat.EaseShop.Model.transactionModel
import com.SapnokitPaat.EaseShop.Model.userModel
import com.SapnokitPaat.EaseShop.R
import com.SapnokitPaat.EaseShop.databinding.ActivityShowcustomarTransactionBinding
import com.SapnokitPaat.EaseShop.utils.CUSTOMERS
import com.SapnokitPaat.EaseShop.utils.LOGINDETAILS
import com.SapnokitPaat.EaseShop.utils.USER_DATA
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreSettings

class showcustomarTransactionActivity : AppCompatActivity() {
    private val binding: ActivityShowcustomarTransactionBinding by lazy {
        ActivityShowcustomarTransactionBinding.inflate(layoutInflater)
    }


    private var db = FirebaseFirestore.getInstance()
    private lateinit var loadingProgressBar: ProgressBar

    //  DETAIL ACTIVITY AND THIS ACTIVITY FINISH THEN USER CLICK DELETE BUTTON
    private  val REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadingProgressBar = binding.loadingPrograssBarCT

        // Retrieve the ID passed from MainActivity
        // val costomarId = intent.getStringExtra("CUSTOMER_ID")
        val costomarname = intent.getStringExtra("CUSTOMER_NAME")
        val costomarId = intent.getStringExtra("CUSTOMER_ID")
        // Retrieve the number from the intent using getLongExtra()
        val customerNumber = intent.getLongExtra("NUMBER", 0L)

        //binding.shownameC.text="ID: $costomarId"
       // binding.shownameC.text = "$costomarname"


        //restart activity
        binding.CTRestartbtn.setOnClickListener {
            val intent = intent
            finish()
            startActivity(intent)
        }
        binding.updateanddeletebtnC.setOnClickListener {

            var intent = Intent(this, profileCActivity::class.java)
           // intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
            intent.putExtra("CUSTOMER_ID", costomarId) // Yahi ID aage bhej rahe hain
            startActivityForResult(intent, REQUEST_CODE)

        }







        binding.backCT.setOnClickListener {
            finish()
        }

        binding.sumtotalc.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
               updateFirestore("c_moneyP",s.toString())
            }

        })


        binding.addcustomerTransactionbtn.setOnClickListener {

            val intent = Intent(this, addcustomarTransactionActivity::class.java)
            intent.putExtra("CUSTOMER_ID", costomarId) // Yahi ID aage bhej rahe hain
            startActivity(intent)
        }

        /* / Initialize userList as a mutable list
        T_List = mutableListOf()
        adapter = transactionAdaptor(T_List)
        binding.rvshowCT.adapter = adapter


        val userId = FirebaseAuth.getInstance().currentUser!!.uid

         db.collection(USER_DATA).document(CUSTOMERS).collection(userId).document(costomarId!!)
            .collection(CUSTOMERS)




            .orderBy("mfgDt", Query.Direction.DESCENDING)
            .get()


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
        binding.rvshowCT.adapter = adapter

*/
        // show busness setails
        showBusnessDtail()
        //show customar name
        showname()
        // setup rv transaction
        setupC_RecyclerView()



        binding.sendSMS.setOnClickListener {
            if (isNetworkAvailable()){
                val manny= binding.sumtotalc.text.toString()
                val number = binding.textView55.text.toString()
                val onername = binding.nameonerc.text.toString()
                val shopname = binding.shopnamec.text.toString()

                val name = binding.shownameC.text.toString()
                if (manny.isNotEmpty() && name.isNotEmpty()) {
                    sendSMS(number, manny, name,onername,shopname)

                } else {
                    Toast.makeText(this, "please enter all value then try again", Toast.LENGTH_SHORT)
                        .show()
                }
            }else{
                //  Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()

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

        binding.sendWhatsapp.setOnClickListener{


            if (isNetworkAvailable()){
                val manny= binding.sumtotalc.text.toString()
                val number = binding.textView55.text.toString()

                val name = binding.shownameC.text.toString()
                val onername = binding.nameonerc.text.toString()
                val shopname = binding.shopnamec.text.toString()
                if (manny.isNotEmpty() && name.isNotEmpty()) {
                    whatsappsendReminder(number, manny, name,onername,shopname)

                } else {
                    Toast.makeText(this, "please enter all value then try again", Toast.LENGTH_SHORT)
                        .show()
                }
            }else{
              //  Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()

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


    }

    private fun updateFirestore(field: String, value: Any) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val costomarId = intent.getStringExtra("CUSTOMER_ID")

        db.collection(USER_DATA)
            .document(CUSTOMERS)
            .collection(userId)
            .document(costomarId!!)
            .update(field,value)
            .addOnSuccessListener {
               // Toast.makeText(this, "update successfuly", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->


                Toast.makeText(this, "Error not update total Amaunt ", Toast.LENGTH_LONG).show()
            }
    }


    private fun sendSMS(number: String, manny: String, name: String, onername: String, shopname: String,){

        val message = "Dear customer $name  ,\n ShopName $shopname ,\n      your payment of $manny is pending "
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("smsto:$number")
        intent.putExtra("sms_body", message)

        try {
            startActivity(intent)
        }catch (e: Exception){
            Toast.makeText(this, "Faild to send sms", Toast.LENGTH_SHORT).show()
        }

        /* send  sms direct this app click button then
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(number,null,message,null,null)
            Toast.makeText(this, "sms sent successfully", Toast.LENGTH_SHORT).show()
        }catch (e: Exception){
            Toast.makeText(this, "sms sending failed", Toast.LENGTH_SHORT).show()

        }
        */
    }

    private fun whatsappsendReminder(
        number: String,
        manny: String,
        name: String,
        onername: String,
        shopname: String
    ) {

        val massage = "Dear customer $name ji ,\n ShopName $shopname, \n       your payment of $manny is pending "
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
        showname()
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
                    binding.nameonerc.text = user.U_name
                    binding.shopnamec.text = user.U_shopName
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

//costomar name show
    private fun showname() {
        // Access Firestore instance
        val costomarId = intent.getStringExtra("CUSTOMER_ID")
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        /*
                // Configure Firestore settings for offline persistence
                val settings = FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .build()
                db.firestoreSettings = settings

                */

// Assuming 'users' is the collection name
//val userRef = db.collection(USER_DATA).document(LOGINDETAILS).collection(userId)
        if (costomarId != null) {
            db.collection(USER_DATA)
                .document(CUSTOMERS)
                .collection(userId)
                .document(costomarId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    // buttonUpdate .visibility = Button.VISIBLE
                    loadingProgressBar .visibility = ProgressBar.INVISIBLE

                    if (documentSnapshot.exists()) {
                        val user = documentSnapshot.toObject(customerModel::class.java)
                        if (user != null) {
                            // Assuming you want to display information from the document
                            // Update UI
                            binding.shownameC.text = user.C_name
                        //    binding..text = user.C_country

                         val number= user.C_number

                            binding.textView55.text = "+91$number"

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
                    //     buttonUpdate .visibility = Button.VISIBLE
                    loadingProgressBar .visibility = ProgressBar.INVISIBLE

                    // Handle any errors
                    Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }

    }

    // trangection data show
    private fun setupC_RecyclerView() {
        loadingProgressBar.visibility = ProgressBar.VISIBLE
        val costomarname = intent.getStringExtra("CUSTOMER_NAME")
        val costomarId = intent.getStringExtra("CUSTOMER_ID")
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        // Configure Firestore settings for offline persistence
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings = settings

        db.collection(USER_DATA)
            .document(CUSTOMERS)
            .collection(userId)
            .document(costomarId!!)
            .collection(CUSTOMERS)
            // .orderBy("itemname")
            .orderBy("mfgDt", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                val costomarname = intent.getStringExtra("CUSTOMER_NAME")
                val costomarId = intent.getStringExtra("CUSTOMER_ID")
                val userId = FirebaseAuth.getInstance().currentUser!!.uid
                loadingProgressBar.visibility = ProgressBar.INVISIBLE

                val item = result.toObjects(transactionModel::class.java)
                binding.rvshowCT.layoutManager = LinearLayoutManager(this)
                val adapter = transactionAdaptor(item) { transactionModel ->
                    // Handle click event
                    val intent = Intent(
                        this@showcustomarTransactionActivity,
                        EntryDetailsCActivity::class.java
                    )
                    intent.putExtra("TRANSACTION_ID", transactionModel.T_id)
                    intent.putExtra("CUSTOMER_ID", costomarId)
                    intent.putExtra("CUSTOMER_NAME", costomarname)
                    startActivity(intent)
                }
                binding.rvshowCT.adapter = adapter

                // Calculate total T_got and T_give
                var totalGot = 0
                var totalGive = 0
                for (transaction in item) {
                    totalGive += transaction.T_give.toIntOrNull() ?: 0
                    totalGot += transaction.T_got.toIntOrNull() ?: 0
                }
                // Calculate difference between total T_got and total T_give
                val totalDifference =   totalGive - totalGot
                // Set total to TextView
                binding.sumtotalc.text = "$totalDifference"
                // Set color according to positive or negative value
                binding.sumtotalc.setTextColor(
                    ContextCompat.getColor(
                        this,
                        if (totalDifference >= 0) android.R.color.holo_green_dark else android.R.color.holo_red_dark
                    )
                )

            }
            .addOnFailureListener { e ->
                loadingProgressBar.visibility = ProgressBar.INVISIBLE
                if (e is FirebaseFirestoreException && e.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                    // Handle permission denied error
                    Toast.makeText(
                        this,
                        "Permission denied. Check Firestore rules.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    // Handle other errors
                    Toast.makeText(
                        this,
                        "Error loading customers: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
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


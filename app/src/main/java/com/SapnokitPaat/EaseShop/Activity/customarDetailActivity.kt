package com.SapnokitPaat.EaseShop.Activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.SapnokitPaat.EaseShop.Adaptor.customerAdaptor
import com.SapnokitPaat.EaseShop.Model.customerModel
import com.SapnokitPaat.EaseShop.R
import com.SapnokitPaat.EaseShop.databinding.ActivityCustomarDetailBinding
import com.SapnokitPaat.EaseShop.utils.CUSTOMERS
import com.SapnokitPaat.EaseShop.utils.USER_DATA
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Source

class customarDetailActivity : AppCompatActivity() {
    private val binding : ActivityCustomarDetailBinding by lazy {
        ActivityCustomarDetailBinding.inflate(layoutInflater)
    }

    private var db = FirebaseFirestore.getInstance()
    private lateinit var loadingProgressBar: ProgressBar

    private lateinit var searchView: SearchView
    private lateinit var adaptor: customerAdaptor
    private  var customerList: List<customerModel> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadingProgressBar=binding.loadingPrograssBar

        //restart activity
        binding.CRestartbtn.setOnClickListener {
            val intent=intent
            finish()
            startActivity(intent)
        }

        binding.addCustomarbtn.setOnClickListener {
            var intent = Intent(this, addCustomerActivity::class.java)
            startActivity(intent)
        }
        binding.closebtn.setOnClickListener {
            finish()
        }


        setupC_RecyclerView()


        // Handle item click

        adaptor = customerAdaptor(emptyList()){ customerModel ->
            // Here you handle the click event
            val intent = Intent(this@customarDetailActivity, showcustomarTransactionActivity::class.java)
            intent.putExtra("CUSTOMER_ID", customerModel.C_id)
            intent.putExtra("CUSTOMER_NAME", customerModel.C_name)


            // Check if C_number is not null before putting it into the intent extras
            customerModel.C_number?.let { number ->
                intent.putExtra("NUMBER", number)

            }
            //intent.putExtra("CUSTOMER_NUMBER", customerModel.C_number)
            startActivity(intent)

        }
        binding.RvCustomar.layoutManager = LinearLayoutManager(this)
        binding.RvCustomar.adapter = adaptor


        // Set up search functionality

        searchView = binding.searchTextC

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    filter(newText)
                }
                return true
            }
        })

    }



    private fun filter(query: String) {
        val filteredList = customerList.filter { customerModel ->
            customerModel.C_name.contains(query, ignoreCase = true )
        }
        adaptor.setData(filteredList)
    }


    override fun onStart() {
        super.onStart()
        setupC_RecyclerView()

    }

    private fun setupC_RecyclerView() {
        loadingProgressBar .visibility = ProgressBar.VISIBLE
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // Configure Firestore settings for offline persistence
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings = settings

        db.collection(USER_DATA)
            .document(CUSTOMERS)
            .collection(userId)
            .orderBy("c_name")
            .get(getSource())
            .addOnSuccessListener { result ->
                handleResult(result)
            }
            .addOnFailureListener { e ->
                loadingProgressBar .visibility = ProgressBar.INVISIBLE

                Toast.makeText(this, "Error loading suppliers: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            }
       //loadingProgressBar .visibility = ProgressBar.INVISIBLE

    }
    private fun getSource(): Source{
        return if (isNetworkAvailable()){
            Source.DEFAULT
        }else{
            Source.CACHE
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(android.content.Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val network =connectivityManager.activeNetwork ?:return false
            val capabilities=connectivityManager.getNetworkCapabilities(network) ?:return false
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }else{
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnectedOrConnecting
        }
    }
    private fun handleResult(result: QuerySnapshot) {
        loadingProgressBar .visibility = ProgressBar.INVISIBLE




        if (result.isEmpty && getSource() == Source.CACHE){
            //show error
            val view = layoutInflater.inflate(R.layout.dealog_warning_datanot_show,null)

            val builder =  AlertDialog.Builder(this)
            builder.setView(view)

            val alertDialog = builder.create()
            //set the background to transparent
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.show()
            return
        }
        if (result.isEmpty){
            if (isNetworkAvailable()){
                // Toast.makeText(this, "Add new data, please", Toast.LENGTH_LONG).show()
                val view = layoutInflater.inflate(R.layout.dialog_adddata_box,null)

                val builder =  AlertDialog.Builder(this)
                builder.setView(view)

                val alertDialog = builder.create()
                //set the background to transparent
                alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                alertDialog.show()

            }

            else {
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()

                // Show alert to check internet connection if user is offline
                // Function to show warning dialog
                val view = layoutInflater.inflate(R.layout.dealog_warning_datanot_show,null)

                val builder =  AlertDialog.Builder(this)
                builder.setView(view)

                val alertDialog = builder.create()
                //set the background to transparent
                alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                alertDialog.show()


            }

            return
        }


            loadingProgressBar .visibility = ProgressBar.INVISIBLE
            val costomar = result.toObjects(customerModel::class.java)
            customerList = costomar
            adaptor.setData(customerList)
            calculateTotalPositiveandnagativeMoney(costomar)

        /*
            binding.RvCustomar.layoutManager = LinearLayoutManager(this)
            binding.RvCustomar.adapter = customerAdaptor(costomar) { customerModel ->


                // Here you handle the click event
                val intent = Intent(this@customarDetailActivity, showcustomarTransactionActivity::class.java)
                intent.putExtra("CUSTOMER_ID", customerModel.C_id)
                intent.putExtra("CUSTOMER_NAME", customerModel.C_name)
                startActivity(intent)
            }
        */

        }

    private fun calculateTotalPositiveandnagativeMoney(customerList: List<customerModel>) {

        val totalPositiveMoney = customerList.filter {
            try {
                it.C_moneyP.toInt() > 0
            }catch (e: NumberFormatException){
                false
            }

        }.sumOf {
            try {
                it.C_moneyP.toInt()
            }catch (e:NumberFormatException ){
                0
            }
        }
        val totalNagativeMoney = customerList.filter {
            try {
                it.C_moneyP.toInt() < 0
            }catch (e:NumberFormatException ){
                false
            }
        }.sumOf {
            try {
                it.C_moneyP.toInt()
            }catch (e:NumberFormatException ){
                0
            }
        }

        binding.totalCGive.text = "₹$totalPositiveMoney"
        binding.totalCGet.text = "₹$totalNagativeMoney"

    }


}


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
import com.SapnokitPaat.EaseShop.Adaptor.supplierAdaptor
import com.SapnokitPaat.EaseShop.Model.supplierModel
import com.SapnokitPaat.EaseShop.R
import com.SapnokitPaat.EaseShop.databinding.ActivityMainBinding

import com.SapnokitPaat.EaseShop.utils.SUPPLIERS
import com.SapnokitPaat.EaseShop.utils.USER_DATA

import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Source

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private var db = FirebaseFirestore.getInstance()
    private lateinit var loadingProgressBar: ProgressBar

    private lateinit var searchView: SearchView
    private lateinit var adapter: supplierAdaptor
    private var supplierList: List<supplierModel> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        //enable offline persistence
        db.firestoreSettings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()



        loadingProgressBar = binding.loadingPrograssBarS

        searchView = binding.searchText

        adapter = supplierAdaptor(emptyList()) { supplierModel ->
            // Handle item click


            // Here you handle the click event
            val intent = Intent(this@MainActivity, showTransactionActivity::class.java)
            intent.putExtra("SUPPLIER_ID", supplierModel.S_id)
            intent.putExtra("SUPPLIER_NAME", supplierModel.S_name)
            intent.putExtra("SUPPLIER_NUMBER", supplierModel.S_number)

            startActivity(intent)
        }


        binding.Rvsuppliershow.layoutManager = LinearLayoutManager(this)
        binding.Rvsuppliershow.adapter = adapter

        // Set up search functionality
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

        //restart activity
        binding.SRestartbtn.setOnClickListener {
            val intent = intent
            finish()
            startActivity(intent)
        }

        binding.addSupplierbtn.setOnClickListener {
            val intent = Intent(this, addsupplierActivity::class.java)
            startActivity(intent)
        }
        binding.closeMbtn.setOnClickListener {
            finish()
        }
        /*
        //card view ki size badae
        val cardView = binding.searchCv
        cardView.viewTreeObserver.addOnGlobalLayoutListener {
            val layoutParams =  cardView.layoutParams
            layoutParams.width = dpToPx(370)
            cardView.layoutParams = layoutParams

        }
*/

        setupS_RecyclerView()


    }
    //fun size badane ka
    /*
    fun dpToPx(dp: Int): Int{
        return (dp * resources.displayMetrics.density).toInt()
    }
    */

    private fun filter(query: String) {
        val filteredList = supplierList.filter { supplierModel ->
            supplierModel.S_name.contains(query, ignoreCase = true)
        }
        adapter.setData(filteredList)
    }


    override fun onStart() {
        super.onStart()
        setupS_RecyclerView()
    }

    private fun setupS_RecyclerView() {
        loadingProgressBar.visibility = ProgressBar.VISIBLE
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // Configure Firestore settings for offline persistence

        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings = settings



        db.collection(USER_DATA)
            .document(SUPPLIERS)
            .collection(userId)
            // .orderBy("S_name")
            .orderBy("s_name")
            //.limit(5)
            .get(getSource())
            .addOnSuccessListener { result ->

                handleResult(result)

            }
            .addOnFailureListener { e ->


                Toast.makeText(
                    this,
                    "Error loading suppliers: ${e.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun getSource(): Source {
        return if (isNetworkAvailable()) {
            Source.DEFAULT
        } else {
            Source.CACHE
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

    private fun handleResult(result: QuerySnapshot) {
        loadingProgressBar.visibility = ProgressBar.INVISIBLE


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


            loadingProgressBar.visibility = ProgressBar.INVISIBLE
            val sup = result.toObjects(supplierModel::class.java)
            supplierList = sup
            adapter.setData(supplierList)

        // calculate money
            calculateTotalPositiveandnagativeMoney(supplierList)
         /*

            val suppliers = result.toObjects(supplierModel::class.java)
            binding.Rvsuppliershow.layoutManager = LinearLayoutManager(this)
            binding.Rvsuppliershow.adapter = supplierAdaptor(suppliers) { supplierModel ->
                // Here you handle the click event
                val intent = Intent(this@MainActivity, showTransactionActivity::class.java)
                intent.putExtra("SUPPLIER_ID", supplierModel.S_id)
                intent.putExtra("SUPPLIER_NAME", supplierModel.S_name)

                startActivity(intent)
            }

        */

        /*
        if (source == Source.
            DEFAULT && result.isEmpty) {
            Toast.makeText(this, "add new data", Toast.LENGTH_SHORT).show()

        } else if (source == Source.CACHE && !isNetworkAvailable()){
            // Toast.makeText(this, "please check internet conection", Toast.LENGTH_LONG).show()
            // Function to show warning dialog
            val view = layoutInflater.inflate(R.layout.dealog_warning_datanot_show,null)

            val builder =  AlertDialog.Builder(this)
            builder.setView(view)

            val alertDialog = builder.create()
            //set the background to transparent
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.show()
        }

        else {

            loadingProgressBar.visibility = ProgressBar.INVISIBLE

            val suppliers = result.toObjects(supplierModel::class.java)
            binding.Rvsuppliershow.layoutManager = LinearLayoutManager(this)
            binding.Rvsuppliershow.adapter = supplierAdaptor(suppliers) { supplierModel ->
                // Here you handle the click event
                val intent = Intent(this@MainActivity, showTransactionActivity::class.java)
                intent.putExtra("SUPPLIER_ID", supplierModel.S_id)
                intent.putExtra("SUPPLIER_NAME", supplierModel.S_name)

                startActivity(intent)
            }
        }
        */


    }

    private fun calculateTotalPositiveandnagativeMoney(supplierList: List<supplierModel>) {
        val totalPositiveMoney = supplierList.filter {
            try {
                it.S_money.toInt() > 0
            }catch (e: NumberFormatException){
                false
            }
        } .sumOf {
            try {
                it.S_money.toInt()
            }catch (e: NumberFormatException){
                0
            }
        }
        val totalNagativeMoney = supplierList.filter {
            try {
                it.S_money.toInt() < 0
            }catch (e: NumberFormatException){
                false
            }
        } .sumOf {
            try {
                it.S_money.toInt()
            }catch (e: NumberFormatException){
                0
            }
        }
        binding.totalSGiveMain.text ="₹$totalPositiveMoney"
        binding.totalSGetMain.text ="₹$totalNagativeMoney"
    }
}
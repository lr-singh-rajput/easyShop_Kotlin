package com.SapnokitPaat.EaseShop.ActivityBills

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface

import android.graphics.pdf.PdfDocument

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.SapnokitPaat.EaseShop.Adaptor.billAdaptor
import com.SapnokitPaat.EaseShop.Model.BillModel
import com.SapnokitPaat.EaseShop.Model.billCutomerModel
import com.SapnokitPaat.EaseShop.Model.userModel
import com.SapnokitPaat.EaseShop.R
import com.SapnokitPaat.EaseShop.databinding.ActivityShowbillBinding
import com.SapnokitPaat.EaseShop.utils.BILL
import com.SapnokitPaat.EaseShop.utils.LOGINDETAILS
import com.SapnokitPaat.EaseShop.utils.USER_DATA
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.io.FileOutputStream
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class showbillActivity : AppCompatActivity() {
    private val binding:ActivityShowbillBinding by lazy {
        ActivityShowbillBinding .inflate(layoutInflater)
    }

    private lateinit var  adapter :billAdaptor
    private lateinit var pdf: Button
    private lateinit var Edit: ImageView
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var cardProgressBar: CardView

    private lateinit var addbtnD: Button
    private lateinit var editD: EditText

   // private lateinit var editDt: TextView
   // private lateinit var editSD: TextView
 //   private lateinit var editNTD: TextView
 //   private lateinit var editNTDS: TextView





    //  DETAIL ACTIVITY AND THIS ACTIVITY FINISH THEN USER CLICK DELETE BUTTON
    private  val REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


/*

        // actionbar color change
       // supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFCD3838")))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.white2)
        }
        // actionbar color change code End
*/

        loadingProgressBar = binding.progressBarBIll
        loadingProgressBar .visibility = ProgressBar.VISIBLE

        cardProgressBar = binding.cardprogressBarBILL
        cardProgressBar .visibility = CardView.VISIBLE


        pdf = binding.pdfBills
        pdf .visibility = Button.INVISIBLE

       Edit = binding.updateBtnBill
        Edit .visibility = ImageView.VISIBLE

        // descount handel visiblety

        addbtnD = binding.discountaddBtn
        addbtnD .visibility = Button.INVISIBLE

        editD = binding.enterDiscountMoney
        editD.visibility=EditText.INVISIBLE

        //
/*
        editDt = binding.textView67
        editDt.visibility=TextView.INVISIBLE

        editSD = binding.showDiscount
        editSD.visibility=TextView.INVISIBLE

        editNTD = binding.textView72
        editNTD.visibility=TextView.INVISIBLE

        editNTDS = binding.newtotalmoneybill
        editNTDS.visibility=TextView.INVISIBLE

*/
        val costomarnameB = intent.getStringExtra("CUSTOMER_NAME")
        val costomarIdB = intent.getStringExtra("CUSTOMER_ID")
        val costomarNoB = intent.getStringExtra("CUSTOMER_NO")

       // binding.showidB.text="ID: $costomarIdB"
       // binding.custNameB.text="$costomarnameB"
      //  binding.custNumberB.text="$costomarNoB"



        // Automatically detect current date and time

        val  mfgDt =
            System.currentTimeMillis()
        // Convert milliseconds to formatted date and time string
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDateTime = dateFormat.format(Date(mfgDt))
        binding.showdatatimeBill.text = formattedDateTime


        binding.showfielddiscount.setOnClickListener{
            editD.visibility=EditText.VISIBLE
            addbtnD .visibility = Button.VISIBLE
        }



        binding.discountaddBtn.setOnClickListener {
       //     Toast.makeText(this, "This Discount is not saved in The DATABASE ", Toast.LENGTH_LONG).show()
            val totalmoneystr = binding.showtotalmonny.text.toString()
            val descountstr = binding.enterDiscountMoney.text.toString()
                binding.showDiscount.text = "$descountstr"
            if (totalmoneystr.isNotEmpty() && descountstr.isNotEmpty()){
                val total = totalmoneystr.toDouble()
                val des = descountstr.toDouble()
                val newtotalmoneybill = total - des
                binding.newtotalmoneybill.text = " $newtotalmoneybill"
            }else{
                Toast.makeText(this, "error show disc", Toast.LENGTH_SHORT).show()
            }
            // save new total money add discount
            savetotalinserver()

        }

        binding.updateBtnBill.setOnClickListener {
            var intent = Intent(this, profileBillActivity::class.java)
            intent.putExtra("CUSTOMER_ID",costomarIdB ) // Yahi ID aage bhej rahe hain
            startActivityForResult(intent, REQUEST_CODE)
        }

        //GENAREATE PDF CODE





        //recyclerview setup
        adapter = billAdaptor(emptyList())
        binding.showbillrv.layoutManager = LinearLayoutManager(this)

        binding.pdfBills.setOnClickListener {
            generatePDF()
        }

        // show busness details
        showBusnessDtail()



      //  fetchDataBill()
        fetchDataBill()

        // show customer setails
        showCustomarDtail()

   // show discount and new total money
        setupdiscount()
        // Find your button
        val screenshotButton: Button = findViewById(R.id.screenshotButton)
      // val shareButton:Button = findViewById(R.id.shareButton)



        screenshotButton.setOnClickListener {
            pdf .visibility = Button.INVISIBLE
            Edit .visibility = ImageView.INVISIBLE
           // share .visibility = Button.INVISIBLE
            // Hide the action bar temporarily
            supportActionBar?.hide()

            /*
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE_WRITE_EXTERNAL_STORAGE
                )
            } else {
                takeAndSaveScreenshot()
            }

             */
            //screnshot code
          //  takeScreenshot()

        }


    }
// update  total new value and descount in firestore
    private fun savetotalinserver() {
    val newtotalsave = binding.newtotalmoneybill.text.toString()
    val Descount = binding.showDiscount.text.toString()
    if (newtotalsave .isEmpty()){
        Toast.makeText(this, "Pleasse fill the all field", Toast.LENGTH_SHORT).show()
        }else{
       // val newtotalsave = binding.newtotalmoneybill.text.toString()
        val costomarIdB = intent.getStringExtra("CUSTOMER_ID")
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        db.collection(USER_DATA)
            .document(BILL)
            .collection(userId)
            .document(costomarIdB!!)
            .update("totalwithDisnt",newtotalsave,"disnt" ,Descount)
            .addOnSuccessListener {
                Toast.makeText(this, "save Descount", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "faild to save Discount", Toast.LENGTH_SHORT).show()
            }
        editD.visibility=EditText.INVISIBLE
        addbtnD .visibility = Button.INVISIBLE
        }
    }


    private fun generatePDF() {

     //   val fileName= "bill_${System.currentTimeMillis()}.pdf"
     //   val file = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),fileName)

     //   val writer = PdfWriter(file)

        // PROPARTI
        //paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        //paint.color = Color.BLACK
        //paint.textSize = 40f   //-- text size jis text ki size badani hai us ke upar likhe fir jo defolt hai agle text ke uper vo text size likhe
        //yPosition += 20f
        //xPosition += 20f
        /*
        //use position
        yPosition += 20f
        canvas.drawText("Round-off", 10f, yPosition, paint)
        canvas.drawText("0.40", 450f, yPosition, paint)
        */
        /*
        // Table Header ak sath x position dena or bhi propaeti

        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 12f
        val yStart = 250f
        val xPositions = listOf(10f, 50f, 250f, 350f, 400f, 450f)
        val headers = listOf("#", "Item Details", "Price/Unit", "Qty", "Rate", "Total")
        for (i in headers.indices) {
            canvas.drawText(headers[i], xPositions[i], yStart, paint)
        }
        // handal rv
         // Item Details (Example data, replace with your adapter data)
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    var yPosition = yStart + 20f
    val items = adapter.B_list
    for ((index, item) in items.withIndex()) {
        canvas.drawText((index + 1).toString(), xPositions[0], yPosition, paint)
        canvas.drawText(item.itemName, xPositions[1], yPosition, paint)
        canvas.drawText("${item.rate}/${item.unit}", xPositions[2], yPosition, paint)
        canvas.drawText(item.quantity.toString(), xPositions[3], yPosition, paint)
        canvas.drawText(item.rate.toString(), xPositions[4], yPosition, paint)
        canvas.drawText(item.amount.toString(), xPositions[5], yPosition, paint)
        yPosition += 20f
    }
        */


        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595 ,842 ,1). create()
        val page = document.startPage(pageInfo)

        val canvas = page.canvas
        val paint = Paint()
        paint.textSize = 18f

        //  content here using canvas object
        //busness detail

        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textSize = 29f // Set text size to 29
        //paint.textAlign = Paint.Align.CENTER
        canvas.drawText(binding.businessNname.text.toString() ,51f , 25f, paint)
        //canvas.drawText("GANPATI KRASHI SEWA KENDRA  ", 121f, 25f, paint)


        paint.textSize = 18f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Name -", 90f, 65f, paint)
        canvas.drawText(binding.usernameB.text.toString() ,180f , 65f, paint)

        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("No. -", 340f, 65f, paint)
        canvas.drawText(binding.usernumberB.text.toString() ,380f , 65f, paint)

        // Invoice Details
        paint.textSize = 14f

        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("INVOICE", 490f, 17f, paint)
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText(binding.showdatatimeBill.text.toString(), 480f, 33f, paint)


        // customer details
        paint.textSize = 18f

        canvas.drawText("customer Name -", 50f, 100f, paint)
        canvas.drawText(binding.custNameB.text.toString() ,180f , 100f, paint)

        canvas.drawText("Number -", 350f, 100f, paint)
        canvas.drawText(binding.custNumberB.text.toString() ,450f , 100f, paint)



        //item details rv
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("No", 15f, 140f, paint)
        canvas.drawText("Name", 50f, 140f, paint)
        canvas.drawText("rate/unit", 220f, 140f, paint)
        canvas.drawText("Qnt", 335f, 140f, paint)
        canvas.drawText("gst", 390f, 140f, paint)
        canvas.drawText("Amt", 445f, 140f, paint)
        canvas.drawText("total", 525f, 140f, paint)

        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        val items = adapter.B_list
        var yPosition = 165f
        paint.textSize = 14f

        for ((index, item) in items.withIndex()){
            canvas.drawText((index + 1).toString(), 10f, yPosition, paint)
            canvas.drawText(item.itemName, 50f, yPosition, paint)
            canvas.drawText(item.rate.toString() + "/" + item.unit, 220f, yPosition, paint)
            canvas.drawText(item.quantity.toString(), 335f, yPosition, paint)

            val gstPercentage = item.gst
            if(gstPercentage ==0){
                paint.textSize = 11f
                canvas.drawText("inGST", 386f, yPosition, paint)
            }else{
                paint.textSize = 14f
                canvas.drawText(item.gst.toString(), 390f, yPosition, paint)
            }
            paint.textSize = 14f
            canvas.drawText(item.amount.toString(), 440f, yPosition, paint)
            canvas.drawText(item.amountwithgst.toString(), 525f, yPosition, paint)
            yPosition += 20f
        }



        //total

        paint.textSize = 19f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)

        canvas.drawText("total = ₹", 80f, 755f, paint)
        canvas.drawText(binding.showtotalmonny.text.toString() ,157f , 755f, paint)

        //Descount

        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText(" Discount = ₹", 70f, 780f, paint)
        canvas.drawText(binding.showDiscount.text.toString(), 180f, 780f, paint)
        canvas.drawText(" new Total = ₹", 70f, 805f, paint)
        canvas.drawText(binding.newtotalmoneybill.text.toString(), 184f, 805f, paint)

    // stamp
        canvas.drawText(" Signature / Stamp  ", 400f, 795f, paint)

        // Footer
        paint.textSize = 12f
        paint.color = Color.BLUE
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText("~  DIGITALLY INVOICE CREATED BY EASESHOP APP  ~", 297.5f, 825f, paint)

        document.finishPage(page)



        //write the document
        val directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
       // val file = File(directoryPath)
        if (!directoryPath.exists()){
            directoryPath.mkdirs()
        }
        val filePath = File(directoryPath,"bill_${System.currentTimeMillis()}.pdf")
        document.writeTo(FileOutputStream(filePath))

        document.close()
        Toast.makeText(this, "pdf generate successfully", Toast.LENGTH_SHORT).show()

        // Open PDF in external viewer
        openPDFInExternalViewer(filePath)
        /*
// Start PdfViewerActivity
        val intent = Intent(this, pdfBillShowActivity::class.java)
        intent.putExtra("filePath", filePath.absolutePath)
        startActivity(intent)
*/
    }

    private fun openPDFInExternalViewer(pdfFile: File) {
    val uri = FileProvider.getUriForFile(this,"${applicationContext.packageName}.provider",pdfFile)

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri,"application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        }
        val chooser = Intent.createChooser(intent,"Open PDF with")
        startActivity(chooser)
    }

    /*
        private fun creatr_screenshot() {
            val pdfDocument = android.graphics.pdf.PdfDocument()
            val pageInfo = android.graphics.pdf.PdfDocument.PageInfo.Builder(595,842,1).create()
            val page = pdfDocument.startPage(pageInfo)
    
            binding.showbillrv.draw(page.canvas)
            pdfDocument.finishPage(page)
    
            val derectory= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!derectory.exists()){
                derectory.mkdirs()
            }
    
            val file =  File(derectory,"Bill.pdf")
    
            try {
                val outputStream = FileOutputStream(file)
    
                pdfDocument.writeTo(outputStream)
                outputStream.close()
                pdfDocument.close()
    
                Toast.makeText(this, "pdf is created at ${file.absolutePath}", Toast.LENGTH_SHORT).show()
            }catch (e: IOException){
                Toast.makeText(this, "Error creating pdf: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    
        }
        */




    /*
    private fun takeScreenshot() {
        val rootView: View = window.decorView.rootView
        rootView.isDrawingCacheEnabled = true
        val bitmap: Bitmap = Bitmap.createBitmap(rootView.drawingCache)
        rootView.isDrawingCacheEnabled = false

        // Save the screenshot
        val uri = saveScreenshot(bitmap)

        // Display the screenshot
        //uri?.let {
          //  imageView.setImageURI(uri)
        //}

        // Share the screenshot
        shareScreenshot(uri)
        pdf .visibility = Button.VISIBLE

        Edit .visibility = ImageView.VISIBLE
    }
    private fun saveScreenshot(bitmap: Bitmap): Uri? {
        val resolver: ContentResolver = applicationContext.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "${System.currentTimeMillis()}.png")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES+ File.separator+"DataEntryBook")
        }

        val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        imageUri?.let {
            try {
                resolver.openOutputStream(it)?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    Toast.makeText(
                        this,
                        "bill saved to Pictures directory",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    "Error saving bill: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return imageUri
    }

    private fun shareScreenshot(uri: Uri?) {
        uri?.let {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_STREAM, it)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(shareIntent, "Share Screenshot"))
        }

    }
    */



     */
    override fun onStart() {
        super.onStart()
        showBusnessDtail()
        showCustomarDtail()
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
             //   loadingProgressBar .visibility = ProgressBar.INVISIBLE

              //  cardProgressBar .visibility = CardView.INVISIBLE

                val user = querySnapshot.toObject(userModel::class.java)
                if (user != null) {
                    // Assuming you want to display information from the first document found
                    // Update UI
                    binding.usernameB.text = user.U_name
                    binding.businessNname.text = user.U_shopName
                    //binding.usernumberB.text = user.U_number
                    val number= user.U_number.toString()
                    val code= user.U_country
                    binding.usernumberB.text = "$code$number"
                } else {
                    // Handle the case where the user conversion is null
                }

            }
            .addOnFailureListener { exception ->
              //  loadingProgressBar .visibility = ProgressBar.INVISIBLE

              //  cardProgressBar .visibility = CardView.INVISIBLE
                // Handle any errors
            }

        //end show business detail code

    }



    // show customar details
    private fun showCustomarDtail() {

            val costomarIdBIL = intent.getStringExtra("CUSTOMER_ID")
            // Access Firestore instance
            val db = FirebaseFirestore.getInstance()
            val userId = FirebaseAuth.getInstance().currentUser!!.uid

            /*
     // Configure Firestore settings for offline persistence
     val settings = FirebaseFirestoreSettings.Builder()
          .setPersistenceEnabled(true)
          .build()
         db.firestoreSettings = settings
    */
            // val firestore = Firebase.firestore

            //firestore.firestoreSettings = firestoreSettings {
            //  isPersistenceEnabled = true
            //}

// Assuming 'users' is the collection name
//val userRef = db.collection(USER_DATA).document(LOGINDETAILS).collection(userId)
            if (costomarIdBIL != null) {
                db.collection(USER_DATA)
                    .document(BILL)
                    .collection(userId)
                    .document(costomarIdBIL)
                    .get()
                    .addOnSuccessListener { documentSnapshot ->

                      //  loadingProgressBar .visibility = ProgressBar.INVISIBLE


                        if (documentSnapshot.exists()) {
                            val user = documentSnapshot.toObject(billCutomerModel::class.java)
                            if (user != null) {
                                // Assuming you want to display information from the document
                                // Update UI
                                binding.custNameB.text = user.C_name
                                binding.custNumberB.text= user.C_number.toString()
                                // Convert milliseconds to formatted date and time string
                                val dateFormat = SimpleDateFormat("dd/MM/yyyy ", Locale.getDefault())
                                val formattedDateTime = dateFormat.format(Date(user.date))
                                binding.showdatatimeBill.text = formattedDateTime
                                binding.showtotalvaluehide.text =user.TotalwithDisnt
                                binding.showDiscount.text =user.disnt





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

                     //   loadingProgressBar .visibility = ProgressBar.INVISIBLE

                        // Handle any errors
                        Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            }




    }



    private fun fetchDataBill() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val costomarIdB = intent.getStringExtra("CUSTOMER_ID")

        val db = FirebaseFirestore.getInstance()

        // Configure Firestore settings for offline persistence
       /*
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings = settings

        */
        db.collection(USER_DATA)
            .document(BILL)
             .collection(userId)
             .document(costomarIdB!!)
            .collection(BILL)
           .orderBy("amountwithgst")
          //  .limit(10)
            .get()
            .addOnSuccessListener {result ->


            val items=mutableListOf<BillModel>()
            for   (document in result){
                  val item = document.toObject(BillModel::class.java)
                items.add(item)
                }
                // Update adapter with the fetched data
                adapter = billAdaptor(items)
                binding.showbillrv.adapter = adapter

                //Calculate total amaunt
                val totalAmount = adapter.calculateTotalAmount()
                // Show warning dialog with the total amount
               // binding.showtotalmonny.text= "$totalAmount "
                val total= totalAmount
                val formattedValue = DecimalFormat("#").format(total)
                binding.showtotalmonny.text = "$formattedValue"

                //binding.newtotalmoneybill.text = "₹$formattedValue "


                // setup if else for total amount show data
                val totalstr = formattedValue

                Handler(Looper.getMainLooper()).postDelayed({

                    var newshowtotalstr = binding.showtotalvaluehide.text.toString()
                        // Ensure both values are loaded from the server before proceeding
                    if(newshowtotalstr.isNullOrEmpty()){
                        newshowtotalstr  = "0"
                    }



                    if (totalstr.isNotEmpty()) {
                        try {


                            val newshow = newshowtotalstr.toDouble()
                            val totalstr2 = totalstr.toInt()

                            if (newshow <=1){
                                binding.newtotalmoneybill.text ="$totalstr"
                            }else{
                                binding.newtotalmoneybill.text ="$newshowtotalstr"
                            }
                        } catch (e: NumberFormatException) {
                            // Handle the error if the strings cannot be converted to integers
                            e.printStackTrace()
                            // Optionally, set a default value or show an error message
                            binding.newtotalmoneybill.text = "Invalid number"
                        }
                    }else{


                        // Optionally, handle the case where data is not yet loaded
                        binding.newtotalmoneybill.text = "Loading..."

                    }
                    loadingProgressBar .visibility = ProgressBar.INVISIBLE

                    cardProgressBar .visibility = CardView.INVISIBLE
                    pdf .visibility = Button.VISIBLE

                },2300)










            }
            .addOnFailureListener { exception ->
               // loadingProgressBar .visibility = ProgressBar.INVISIBLE

            //    cardProgressBar .visibility = CardView.INVISIBLE
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    private fun setupdiscount() {

    }


    // CODE FINISH BOTH ACTIVITY CLICK DELETE BUTOTN

    override fun onActivityResult(requestCode: Int, resultCode:Int,data:Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==REQUEST_CODE && resultCode == RESULT_OK){
            finish()
        }
    }

}
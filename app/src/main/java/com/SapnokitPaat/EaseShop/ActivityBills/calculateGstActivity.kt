package com.SapnokitPaat.EaseShop.ActivityBills

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.SapnokitPaat.EaseShop.databinding.ActivityCalculateGstBinding

class calculateGstActivity : AppCompatActivity() {
    private val binding : ActivityCalculateGstBinding by lazy {
        ActivityCalculateGstBinding .inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //set default country code
        binding.quntitygstCalc.setText("1").toString()


        binding.backCalc.setOnClickListener {
            finish()
        }

        binding

        binding.removegst.setOnClickListener {
            val rateStr = binding.RategstCalc.text.toString()
            val quantityStr = binding.quntitygstCalc.text.toString()
            val gstStr= binding.gstgstCalc.text.toString()
            if (rateStr.isNotEmpty() && quantityStr.isNotEmpty() && gstStr.isNotEmpty()) {
                val rate = rateStr.toDouble()
                val quantity = quantityStr.toDouble()
                val amount = rate * quantity
                val gst = gstStr.toDouble()
                val removegstrate  = rate - (rate*100 /(100+gst))
                val remove= rate - removegstrate

                val gstAmount = amount - (amount*100 /(100+gst))

                val netvalue= amount

                //binding.RategstCalc.setText ("${String.format("%.2f",remove)}")

                binding.AmountgstCalc.text = "₹ ${ String.format("%.2f",remove)} one item "
                binding.withgstAmountgstCalc.text = "₹ ${ String.format("%.2f",gstAmount)}"

                binding.allwithgstAmountbillA.text = "₹ ${ String.format("%.2f",netvalue)}"



            }else if(rateStr.isNotEmpty() && quantityStr.isNotEmpty()) {
                val rate = rateStr.toDouble()
                val quantity = quantityStr.toInt()
                val amount = rate * quantity
                binding.AmountgstCalc.text = "₹ $amount"
                binding.allwithgstAmountbillA.text = "₹ $amount"

            }
            else{
                Toast.makeText(this, "all field required", Toast.LENGTH_SHORT).show()
            }
        }
        binding.calculatgstcalc.setOnClickListener {
            val rateStr = binding.RategstCalc.text.toString()
            val quantityStr = binding.quntitygstCalc.text.toString()
            val gstStr= binding.gstgstCalc.text.toString()
           // val allgstStr= binding.gstbillA.text.toString()

            if (rateStr.isNotEmpty() && quantityStr.isNotEmpty() && gstStr.isNotEmpty()) {
                val rate = rateStr.toDouble()
                val quantity = quantityStr.toDouble()
                val amount = rate * quantity
                val gst = gstStr.toDouble()
                val withGstAmount = amount * gst / 100
                val allwithGstAmount = amount + withGstAmount
                binding.AmountgstCalc.text = "₹ $amount"
                binding.withgstAmountgstCalc.text = "₹ $withGstAmount"
                binding.allwithgstAmountbillA.text = "₹ $allwithGstAmount"

            } else if(rateStr.isNotEmpty() && quantityStr.isNotEmpty()) {
                val rate = rateStr.toDouble()
                val quantity = quantityStr.toInt()
                val amount = rate * quantity
                binding.AmountgstCalc.text = "₹ $amount"
                binding.allwithgstAmountbillA.text = "₹ $amount"

            }else{
                //binding.AmountgstCalc.text = "Invalid input"
                //binding.withgstAmountgstCalc.text = "Invalid"
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
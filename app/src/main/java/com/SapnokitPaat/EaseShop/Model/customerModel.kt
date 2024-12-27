package com.SapnokitPaat.EaseShop.Model

data class customerModel(
    var C_id :String,
    var C_name:String,
    var C_country:String,
    var C_number:String,
    var C_moneyP:String,

    var C_area:String,
    var C_pincode:String,
    var C_city:String,
    //var C_gstNo:String
)
{

    constructor() : this("","","","","","","","")
}


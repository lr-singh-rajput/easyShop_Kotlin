package com.SapnokitPaat.EaseShop.Model

data class userModel(
    var U_id:String,
    var U_name:String,
    var U_shopName:String,
    var U_country:String,

    var U_number:String,
    var email:String,
    var password:String
)
{
    constructor() : this("","","","","","","")
}


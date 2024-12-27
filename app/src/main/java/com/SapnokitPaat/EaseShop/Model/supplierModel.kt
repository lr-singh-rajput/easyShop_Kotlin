package com.SapnokitPaat.EaseShop.Model

data class supplierModel(
    var S_id :String,
    var S_name:String,
    var S_county:String,
    var S_number:String,
    var S_money:String,
    var S_area:String,
    var S_pincode:String,
    var S_city:String
)
{
    constructor() : this("","","","","","","","")
}

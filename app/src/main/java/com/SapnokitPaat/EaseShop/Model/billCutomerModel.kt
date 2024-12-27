package com.SapnokitPaat.EaseShop.Model

data class billCutomerModel(
    var C_id :String,
    var C_name:String,
    var C_country:String,
    var C_number:String,
    var TotalwithDisnt:String,
    var disnt:String,
    var date:Long=0,

    //var C_gstNo:String
)
{

    constructor() : this("","","","","","",0)
}



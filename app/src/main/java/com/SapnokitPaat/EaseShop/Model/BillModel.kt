package com.SapnokitPaat.EaseShop.Model

data class BillModel(
    var id:String,
    var itemName:String,
    var detail:String,
    var unit:String,
    var rate: Double ,
    var quantity: Int ,
    var gst: Int ,
    var descount: Int,
    var amount: Double ,
    var totalgst: Double ,
    var date:Long,
    var amountwithgst :Double,

)
{

    constructor() : this("","","","",0.0,0,0,0,0.0,0.0,0,0.0)
}

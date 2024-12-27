package com.SapnokitPaat.EaseShop.Model

data class transactionModel(
    var T_id :String,
    var T_got:String,
    var T_give:String,
    var T_detail:String,
    var T_billNo:String,
    var mfgDt:Long=0,

){
    constructor() : this("","","","","",0,)
}


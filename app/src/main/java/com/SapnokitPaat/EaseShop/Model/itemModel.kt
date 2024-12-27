package com.SapnokitPaat.EaseShop.Model

class itemModel{
    var id :String?=null
    var itemname:String?=null
    var shellprice:Int?=null
    var offerprice:Int?=null
    var parchassprice:Int?=null
    var stock:Int?=null
    var unit:String?=null
    var imageitem:String?=null

    constructor()

    constructor(
        id: String,
        itemname: String?,
        shellprice: Int?,
        parchassprice: Int?,
        stock: Int?,
        unit:String?,
        offerprice:Int?,
        imageitem: String?
    ) {
        this.id = id
        this.itemname = itemname
        this.shellprice = shellprice
        this.parchassprice = parchassprice
        this.stock = stock
        this.unit = unit
        this.offerprice = offerprice
        this.imageitem = imageitem
    }

    constructor(
        id: String,
        itemname: String?,
        shellprice: Int?,
        parchassprice: Int?,
        stock: Int?

        ) {
        this.id = id
        this.itemname = itemname
        this.shellprice = shellprice
        this.parchassprice = parchassprice
        this.stock = stock
        this.unit = unit

    }

    constructor(
        id: String?,
        itemname: String?,
        shellprice: Int?,
        parchassprice: Int?,
        unit: String?,
        imageitem: String?
    ) {
        this.id = id
        this.itemname = itemname
        this.shellprice = shellprice
        this.parchassprice = parchassprice
        this.unit = unit
        this.imageitem = imageitem
    }

    constructor(
        id: String?,
        itemname: String?,
        shellprice: Int?,
        parchassprice: Int?,
        stock: Int?,
        unit: String?
    ) {
        this.id = id
        this.itemname = itemname
        this.shellprice = shellprice
        this.parchassprice = parchassprice
        this.stock = stock
        this.unit = unit
    }


}

//data class itemModel(
  //  var id :String,
  //  var itemname:String,
 //   var shellprice:Int=0,
   // var offerprice:Int=0,
   // var parchassprice:Int=0,
  // var stock:String,
  //   var imageitem:String?=null

    //)
//{



    //constructor() : this("","",0,0 ,"","")
//}
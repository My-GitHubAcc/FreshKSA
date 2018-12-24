package com.nibrasco.freshksa.Model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference

import java.util.ArrayList

class User {
    var name: String? = null
    var phone: String? = null
    var password: String? = null
    var cart: String? = null
    private var Orders: MutableList<String>? = null

    var orders: MutableList<String>?
        get() = Orders
        set(orders) {
            Orders = orders
        }


    fun AddOrder(id: String?) {
        id?.let{
            Orders!!.add(it)
        }
    }

    constructor() {}

    constructor(name: String, password: String) {
        this.name = name
        phone = "+212..."
        cart = "0"
        Orders = ArrayList()
        this.password = password
    }

    constructor(userSnap: DataSnapshot) {
        phone = userSnap.key
        name = userSnap.child("Name").getValue<String>(String::class.java!!)
        password = userSnap.child("Password").getValue<String>(String::class.java!!)
        cart = userSnap.child("Cart").getValue<String>(String::class.java!!)
        Orders = ArrayList()
        if (userSnap.hasChild("Orders")) {
            val ordersSnap = userSnap.child("Orders")
            if (!ordersSnap.hasChild("0") && ordersSnap.childrenCount >= 1) {
                for (orderSnap in ordersSnap.children) {
                    AddOrder(orderSnap.key)
                }
            }
        }

    }

    fun MapToDbRef(userRef: DatabaseReference) {
        userRef.child("Name").setValue(name)
        userRef.child("Password").setValue(password)
        userRef.child("Cart").setValue(cart)

        val ordersRef = userRef.child("Orders")
        if (Orders!!.size == 0) {
            ordersRef.child("0").setValue("")
        } else {
            for (item in Orders!!) {
                ordersRef.child(item).setValue("")
            }
        }
    }
}

package com.nibrasco.freshksa.Model

import android.provider.ContactsContract
import com.google.firebase.database.DatabaseReference

class Delivery {
    //private String CartId;
    //public String getCartId() {
    //    return CartId;
    //}
    //
    //public void setCartId(String cartId) {
    //    CartId = cartId;
    //}

    var address: String? = null
    var account: String? = null
    var userName: String? = null

    fun Delivery() {
        //CartId = Session.getInstance().User().getCart();
        address = Session.Cart!!.address
        account = Session.Cart!!.bankAccount
        userName = Session.User!!.name
    }

    fun MapToDbRef(dRef: DatabaseReference) {
        dRef.child("Address").setValue(address)
        dRef.child("Account").setValue(account)
        dRef.child("UserName").setValue(userName)
    }
}

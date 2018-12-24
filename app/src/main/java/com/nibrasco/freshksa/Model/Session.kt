package com.nibrasco.freshksa.Model

object Session {
    private var user : User? = null
    private var cart : Cart? = null
    private var item : Cart.Item? = null

        var User : User?
            get() = user
            set(user) { this.user = user }

        var Item : Cart.Item?
            get() = item
            set(_item) { item = _item }

        var Cart : Cart?
            get() = cart
            set(_cart) { cart = _cart }
}

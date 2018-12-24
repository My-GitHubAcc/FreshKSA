package com.nibrasco.freshksa.Model

import android.content.res.Resources
import com.nibrasco.freshksa.R

class ItemCategory(r: Resources, category: Cart.eCategory) {
    var name: String? = null
    var image_drawable: Int = 0

    init {
        when (category) {
            Cart.eCategory.Goat -> name = r.getString(R.string.recyclerItemGoat)
            Cart.eCategory.Sheep -> name = r.getString(R.string.recyclerItemSheep)
            Cart.eCategory.Camel -> name = r.getString(R.string.recyclerItemCamel)
            Cart.eCategory.GroundMeat -> name = r.getString(R.string.recyclerItemGroundMeat)
            Cart.eCategory.HalfSheep -> name = r.getString(R.string.recyclerItemHalfSheep)
        }
        image_drawable = category.Value
    }
}

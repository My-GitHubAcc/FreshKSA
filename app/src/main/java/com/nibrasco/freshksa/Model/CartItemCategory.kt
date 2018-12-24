package com.nibrasco.freshksa.Model

class CartItemCategory(item: Cart.Item) {
    var category: String? = null
    var image_drawable: Int = 0
    var packaging: String? = null
    var slicing: String? = null
    var weight: String? = null
    var intestine: String? = null
    var quantity: String? = null

    init {
        image_drawable = item.Category.Value
        category = Cart.Lists.GetCategoryName(item.Category.At())
        packaging = Cart.Lists.GetPackagingName(item.Packaging.Value)
        slicing = Cart.Lists.GetSlicingName(item.Slicing.Value)
        weight = Cart.Lists.GetWeightName(item.Category, item.Weight)
        intestine = if (item.Intestine) "نعم" else "لا"
        quantity = item.Quantity.toString()
    }
}

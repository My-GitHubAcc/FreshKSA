package com.nibrasco.freshksa.Model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.nibrasco.freshksa.R

import kotlin.collections.ArrayList

class Cart {

    var timeOfDelivery: eTime? = null
        private set
    var address: String? = null
    var bankAccount: String? = null
    private var Items: MutableList<Item>? = null

    object Lists {

        private var Weights: ArrayList<Weight>? = null
        private var Slicings: ArrayList<Slicing> = GetSlicings()
        private var Categories: ArrayList<Category> = GetCategories()
        private var Packages: ArrayList<Packaging> = GetPackages()

        class Weight(internal var Price: Float?, internal var Name: String)
        class Slicing(internal var Name: String)
        class Category(internal var Name: String)
        class Packaging(internal var Price: Float?, internal var Name: String)

        fun GetWeightName(Category: eCategory, index: Int): String {
            Weights = GetWeights(Category)
            return if (index >= 0 && index < Weights!!.size) Weights!![index].Name else "NaN"
        }

        fun GetWeightPrice(Category: eCategory, index: Int): Float {
            Weights = GetWeights(Category)
            return if (index >= 0 && index < Weights!!.size) Weights!![index].Price!! else -1f
        }

        fun GetWeights(Category: eCategory): ArrayList<Weight> {
            Weights = ArrayList()
            when (Category) {
                Cart.eCategory.HalfSheep -> Weights!!.add(Weight(660.0f, "نصف نعيمي"))
                Cart.eCategory.Sheep -> {
                    Weights!!.add(Weight(950.0f, "لباني 9-13 كيلو"))
                    Weights!!.add(Weight(1030.0f, "صغير 12-15 كيلو"))
                    Weights!!.add(Weight(1120.0f, "وسط 15-17 كيلو"))
                    Weights!!.add(Weight(1210.0f, "جدع وسط 17-20 كيلو"))
                    Weights!!.add(Weight(1290.0f, "جدع طيب 20-25 كيلو"))
                    Weights!!.add(Weight(1370.0f, "جدع ناهي 25-30 كيلو"))
                }
                Cart.eCategory.Goat -> {
                    Weights!!.add(Weight(850.0f, "صغير"))
                    Weights!!.add(Weight(920.0f, "وسط"))
                    Weights!!.add(Weight(990.0f, "كبير"))
                }
                Cart.eCategory.Camel -> {
                    Weights!!.add(Weight(400.0f, "5 كيلو"))
                    Weights!!.add(Weight(600.0f, "10 كيلو"))
                    Weights!!.add(Weight(800.0f, "15 كيلو"))
                    Weights!!.add(Weight(950.0f, "20 كيلو"))
                }
                Cart.eCategory.GroundMeat -> {
                    Weights!!.add(Weight(400.0f, "مفروم 5 كيلو"))
                    Weights!!.add(Weight(430.0f, "مفروم 6 كيلو"))
                }
                else -> {
                    return Weights!!
                }
            }
            Weights?.let{
                return it
            }
        }

        fun GetWeightNames(Category: eCategory): ArrayList<String> {
            val list = ArrayList<String>()
            Weights = GetWeights(Category)
            for (w in Weights!!) {
                list.add(w.Name)
            }
            return list
        }

        fun GetSlicingNames(): ArrayList<String> {
            val list = ArrayList<String>()
            for (s in Slicings) {
                list.add(s.Name)
            }
            return list
        }

        fun GetSlicingName(index: Int): String {
            return GetSlicingNames()[index]
        }

        fun GetSlicings(): ArrayList<Slicing> {
            Slicings = ArrayList()

            Slicings.add(Slicing("ثلاجة"))
            Slicings.add(Slicing("أنصاف"))
            Slicings.add(Slicing("أرباع"))
            Slicings.add(Slicing("كامل"))

            return Slicings
        }

        fun GetCategoryNames(): ArrayList<String> {
            val list = ArrayList<String>()
            for (s in Categories) {
                list.add(s.Name)
            }
            return list
        }

        fun GetCategoryName(index: Int): String {
            return GetCategoryNames()[index]
        }

        fun GetCategories(): ArrayList<Category> {
            Categories = ArrayList()

            Categories.add(Category("نعيمي"))
            Categories.add(Category("عجل بلدي"))
            Categories.add(Category("تيس عارضي"))
            Categories.add(Category("حاشي بلدي"))
            Categories.add(Category("نصف نعيمي"))

            return Categories
        }

        fun GetPackagingNames(): ArrayList<String> {
            val list = ArrayList<String>()
            for (p in Packages) {
                list.add(p.Name)
            }
            return list
        }

        fun GetPackagingName(index: Int): String {
            return GetPackagingNames()[index]
        }

        fun GetPackages(): ArrayList<Packaging> {
            Packages = ArrayList()

            Packages.add(Packaging(0.0f, "بدون"))
            Packages.add(Packaging(30.0f, "كيس"))
            Packages.add(Packaging(40.0f, "أطباق"))

            return Packages
        }

        fun GetPackagingPrice(index: Int): Float {
            return if (index >= 0 && index < Packages.size) Packages[index].Price!!
                    else -1f
        }
    }

    enum class eTime private constructor(private val value: Int) {
        Noon(0),
        AfterNoon(1),
        Evening(2);

        var Value = value
            get() = value

        companion object {
            fun Get(value: Int): eTime {
                for (t in values()) {
                    if (t.value == value) return t
                }
                return Noon
            }
        }
    }

    enum class eSlicing private constructor(private val value: Int) {
        Fridge(0),
        Halfs(1),
        Quarters(2),
        Whole(3);

        var Value = value
            get() = value

        companion object {
            fun Get(value: Int): eSlicing {
                for (t in values()) {
                    if (t.value == value) return t
                }
                return Fridge
            }
        }
    }

    enum class ePackaging private constructor(private val value: Int) {
        None(0),
        Bags(1),
        Plates(2);

        var Value = value
            get() = value

        companion object {
            fun Get(value: Int): ePackaging {
                for (t in values()) {
                    if (t.value == value) return t
                }
                return None
            }
        }
    }

    enum class eCategory private constructor(private val value: Int) {

        None(-1),
        Sheep(R.drawable.naeme),
        //Lamb(R.drawable.hree),
        Goat(R.drawable.tais),
        GroundMeat(R.drawable.ajl),
        Camel(R.drawable.hashe),
        HalfSheep(R.drawable.half_sheep);

        var Value = value
                get() = value

        fun At(): Int {
            when (this) {
                Sheep -> return 0
                Goat -> return 1
                GroundMeat -> return 2
                Camel -> return 3
                HalfSheep -> return 4
            }
            return -1
        }

        companion object {
            fun Get(value: Int): eCategory {
                for (t in values()) {
                    if (t.value == value) return t
                }
                return Sheep
            }
        }
    }

    class Item {

        var Category : eCategory
            get() = Category
            set(category){ Category = category }

        var Quantity : Int
            get() = Quantity
            set(quantity){Quantity = quantity}

        var Intestine : Boolean
            get() = Intestine
            set(intestine) { Intestine = intestine}

        var Notes: String?
            get() = Notes
            set(notes) { Notes = notes}

        var Slicing : eSlicing
            get() = Slicing
            set(slicing){ Slicing = slicing }

        var Packaging : ePackaging
            get() = Packaging
            set(packaging) { Packaging = packaging}

        var Weight : Int
            get() = Weight
            set(weight) { Weight = weight }

        var Total: Float
            get() = (Lists.GetWeightPrice(Category, Weight) + Lists.GetPackagingPrice(Packaging.Value)) * Quantity
            set(_total) {
                Total = _total
            }

        val DefaultPrice: Float
            get() {
                when (Category) {
                    Cart.eCategory.None -> return 0.0f
                    Cart.eCategory.Sheep -> return 1030.0f
                    Cart.eCategory.HalfSheep -> return 660.0f
                    Cart.eCategory.Goat -> return 990.0f
                    Cart.eCategory.GroundMeat, Cart.eCategory.Camel -> return 400.0f
                    else -> return 0.0f
                }
            }

        fun setCategory(category: Int) {
            this.Category = eCategory.Get(category)
        }

        fun setSlicing(slicing: Int) {
            this.Slicing = eSlicing.Get(slicing)
        }

        fun setPackaging(packaging: Int) {
            this.Packaging = ePackaging.Get(packaging)
        }

        constructor() {

        }

        constructor(itemSnap: DataSnapshot) {
            Category = eCategory.Get(Integer.parseInt(itemSnap.key!!))
            Notes = itemSnap.child("Notes").getValue<String>(String::class.java)
            Total = java.lang.Float.parseFloat(itemSnap.child("Total").value!!.toString())
            Intestine = Integer.parseInt(itemSnap.child("Intestine").value!!.toString()) != 0
            Packaging = ePackaging.Get(Integer.parseInt(itemSnap.child("Packaging").value!!.toString()))
            Quantity = Integer.parseInt(itemSnap.child("Quantity").value!!.toString())
            Slicing = eSlicing.Get(Integer.parseInt(itemSnap.child("Slicing").value!!.toString()))
            Weight = Integer.parseInt(itemSnap.child("Weight").value!!.toString())
        }

        fun MapToDbRef(itemsRef: DatabaseReference) {
            val itemRef = itemsRef.child(Integer.toString(Category.Value))
            itemRef.child("Intestine").setValue(if (Intestine) 1 else 0)
            itemRef.child("Notes").setValue(Notes)
            itemRef.child("Packaging").setValue(Packaging.Value)
            itemRef.child("Quantity").setValue(Quantity)
            itemRef.child("Slicing").setValue(Slicing.Value)
            itemRef.child("Total").setValue(Total)
            itemRef.child("Weight").setValue(Weight)
        }

        fun ToString(): String {
            return ("\nCategory : " + Category.Value
                    + "\nQuantity : " + Quantity
                    + "\nPackaging : " + Packaging.Value
                    + "\nSlicing : " + Slicing.Value
                    + "\nWeight : " + Lists.GetWeightName(Category, Weight)
                    + "\nNotes : " + Notes
                    + "\nTotal : " + Total)
        }

    }

    fun setTimeOfDelivery(TimeOfDelivery: Int) {
        this.timeOfDelivery = eTime.Get(TimeOfDelivery)
    }

    fun GetCount(): Int? {
        return Items!!.size
    }

    fun GetItem(index: Int): Item {
        return Items!![index]
    }

    fun AddItem(item: Item) {
        Items!!.add(item)
    }

    fun RemoveItem(index: Int) {
        Items!!.removeAt(index)
    }

    fun RemoveItem(item: Item) {
        Items!!.remove(item)
    }

    fun ModifyItem(index: Int, newItem: Item) {
        Items!![index] = newItem
    }

    fun Items(): List<Item>? {
        return Items
    }

    constructor() {
        address = ""
        timeOfDelivery = eTime.Noon
        bankAccount = "0"
        Items = ArrayList()
    }

    constructor(address: String) {
        this.address = address
        timeOfDelivery = eTime.Noon
        bankAccount = "0"
        Items = ArrayList()
    }

    /*
    public Cart(String address, ArrayList<Item> items) {
        Address = address;
        Items = items;
    }
    */
    constructor(cart: DataSnapshot) {
        address = if (cart.hasChild("Address")) cart.child("Address").getValue<String>(String::class.java!!) else ""
        timeOfDelivery = if (cart.hasChild("TimeOfDelivery")) eTime.Get(Integer.parseInt(cart.child("TimeOfDelivery").value!!.toString())) else eTime.Noon
        //BankAccount = cart.child("BankAccount").getValue().toString();
        Items = ArrayList()
        if (cart.child("Items").exists() || cart.child("Items").hasChildren()) {
            val itemsSnap = cart.child("Items")

            for (itemSnap in itemsSnap.children) {
                if (itemSnap.hasChildren())
                    Items!!.add(Item(itemSnap))
            }
        }
    }

    fun MapToDbRef(cartRef: DatabaseReference) {
        cartRef.child("Address").setValue(address)
        cartRef.child("TimeOfDelivery").setValue(timeOfDelivery!!.Value)
        cartRef.child("BankAccount").setValue(bankAccount)
        if (Items!!.size > 0) {
            val itemsRef = cartRef.child("Items")
            for (item in Items!!) {
                item.MapToDbRef(itemsRef)
            }
        }

    }

    fun Total() : Float {
        var total = 0.0f
        if (Items!!.size > 0) {
            for (i in Items!!) {
                total += i.Total
            }
            return total
        }
        return 0.0f
    }

    fun ToString(): String {
        var obj = "Address = " + address + "\nItems(Count = " + Items!!.size + "):\n"
        for (item in Items!!) {
            obj += item.ToString()
        }
        return obj
    }

}

package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.cupcake.R
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val PRICE_PER_CUPCAKE = 2.00
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00

class OrderViewModel: ViewModel()
{
    private val _quantity = MutableLiveData<Int>()
    private val _flavor = MutableLiveData<String>()
    private val _date = MutableLiveData<String>()
    private val _price = MutableLiveData<Double>()


    val quantity: LiveData<Int> = _quantity
    val flavor: LiveData<String> = _flavor
    val date: LiveData<String> = _date
    val price: LiveData<String> = Transformations.map(_price) {NumberFormat.getCurrencyInstance().format(it)}
    val dateOptions = getPickupOptions()
    var isSpecialFlavor = false

    init {
        resetOrder()
    }

    fun hasNoFlavorSet(): Boolean
    {
        return _flavor.value.isNullOrEmpty()
    }

    private fun getPickupOptions(): List<String>
    {
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()
        var repeatTimes = 4

        if(isSpecialFlavor)
            repeatTimes--

        repeat(repeatTimes)
        {
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }

        return options
    }

    fun resetOrder()
    {
        _quantity.value = 0
        _flavor.value = ""
        _date.value = dateOptions[0]
        _price.value = 0.0
        isSpecialFlavor = false
    }

    private fun updatePrice()
    {
        var calculatedPrice = (quantity.value ?: 0) * PRICE_PER_CUPCAKE

        if(dateOptions[0] == _date.value)
        {
            calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
        }

        _price.value = calculatedPrice
    }

    fun setQuantity(numberCupcakes: Int)
    {
        _quantity.value = numberCupcakes
        updatePrice()
    }

    fun setFlavor(desiredFlavor: String)
    {
        _flavor.value = desiredFlavor
    }

    fun setDate(pickupDate: String)
    {
        _date.value = pickupDate
        updatePrice()
    }
}
package com.pbrandwijk.app.models

/**
  * Class to represent products in the web shop.
  *
  * @param id The unique identifier of the product
  * @param description A description of what the product is
  * @param price The price of a single item
  * @param stock The number of items in stock
  */
class Product (val id: String, val description: String, var price: Double, var stock: Int) {
  override def toString = id + ", " + description + ", " + price + ", " + stock
}

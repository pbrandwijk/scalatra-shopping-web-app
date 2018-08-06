package com.pbrandwijk.app.models

import scala.collection.mutable.HashMap

/**
  * Class to represent orders in the web shop.
  *
  * @param email The uniquely identifying email address of the user
  * @param address The shipping address of thu user
  * @param totalPrice The total price of the order
  * @param items The collection of product codes and their quantities in the order
  */
class Order (val email: String, val address: String, val totalPrice: Double, var items: HashMap[String,Int]) {
  override def toString = email + ", " + address + ", " + totalPrice + ", " + items
}

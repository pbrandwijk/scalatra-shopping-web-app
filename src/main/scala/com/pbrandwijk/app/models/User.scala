package com.pbrandwijk.app.models

import scala.collection.mutable.HashMap

/**
  * Class to represent users in the web shop.
  *
  * @param email The unique email address of the user
  * @param name The name of the user
  * @param bankAccount The bank account number of the user
  * @param cart The collection of product codes and their quantities on the user's shopping cart
  */
class User (val email: String, val name: String, val bankAccount: String, var cart: HashMap[String,Int]) {
  override def toString = name + ", " + email + ", " + bankAccount + ", " + cart

  // shopping cart is stored as a mutable HashMap
  cart = new HashMap[String, Int]

  /**
    * Add an item and quantity to the cart of the user. If the item was already on the cart, the quantity will
    * be overwritten.
    *
    * It is assumed that it is previously checked that a product with the given id exists in the model.
    *
    * @param id The uniquely identifying id of the product
    * @param quantity The quantity of the product to be ordered
    */
  def addItemToCart(id: String, quantity: Int): Unit = {
    cart += (id -> quantity)
  }
}

package com.pbrandwijk.app.models

import scala.collection.mutable.HashMap

/**
  * Class to represent users in the web shop.
  *
  * @param email The unique email address of the user
  * @param name The name of the user
  * @param bankAccount The bank account number of the user
  * @param chart The collection of product codes and their quantities on the user's shopping chart
  */
class User (val email: String, val name: String, val bankAccount: String, var chart: HashMap[String,Int]) {
  override def toString = name + ", " + email + ", " + bankAccount + ", " + chart

  // shopping chart is stored as a mutable HashMap
  chart = new HashMap[String, Int]

  /**
    * Add an item and quantity to the chart of the user. If the item was already on the chart, the quantity will
    * be overwritten.
    *
    * It is assumed that it is previously checked that a product with the given id exists in the model.
    *
    * @param id The uniquely identifying id of the product
    * @param quantity The quantity of the product to be ordered
    */
  def addItemToChart(id: String, quantity: Int): Unit = {
    chart += (id -> quantity)
  }
}

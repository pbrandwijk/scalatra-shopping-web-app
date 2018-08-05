package com.pbrandwijk.app

import com.pbrandwijk.app.models._
import org.slf4j.LoggerFactory

import scala.collection.mutable._

/**
  * Object that contains the model of the data in the application.
  */
object Model {

  // create logger for class
  val logger =  LoggerFactory.getLogger(getClass)

  // order numbers are assigned automatically, starting at 1 and incrementing with every order
  var orderReference = 1
  // products are stored in a mutable HashMap
  var products = new HashMap[String, Product]
  // users are stored in a mutable HashMap
  var users = new HashMap[String, User]
  // orders are stored in a mutable HashMap
  var orders = new HashMap[Int, Order]

}

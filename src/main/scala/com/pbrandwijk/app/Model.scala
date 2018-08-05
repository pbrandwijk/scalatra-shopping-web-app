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

  /**
    * Add a product to the model.
    *
    * If the id is already present, then overwrite the existing product.
    *
    * @param product The product to add
    * @return Either the product if added successfully, or an error message if not
    */
  def addProduct(product: Product): Either[String, Product] = {

    if ((products get product.id) == None) {
      products += (product.id -> product)
      logger.info("Added new product: " + product)
    }
    else {
      products -= product.id
      products += (product.id -> product)
      logger.info("Updated product: " + product)
    }
    logger.debug("All products: " + products)
    return Right(product)
  }

  /**
    * Add a user to the model.
    *
    * If the email address is already present, then do nothing.
    *
    * @param user The user to add
    * @return Either the user if added successfully, or an error message if not
    */
  def addUser(user: User): Either[String, User] = {

    if ((users get user.email) == None) {
      users += (user.email -> user)
      logger.info("Added new user: " + user)
      return Right(user)
    }
    else {
      return Left("User already present")
    }
  }

}

package com.pbrandwijk.app.servlets

import com.pbrandwijk.app.{Model, Util}
import com.pbrandwijk.app.models.User
import net.liftweb.json.Serialization.write
import net.liftweb.json._
import org.scalatra._
import org.slf4j.LoggerFactory

class UserServlet extends ScalatraServlet {

  // create logger for class
  val logger =  LoggerFactory.getLogger(getClass)

  /**
    * Get all the users currently in the model as JSON.
    */
  get( "/" ) {
    // create a JSON string from the users, then print it
    implicit val formats = DefaultFormats
    val jsonString = write(Model.users)
    response.getWriter.print(jsonString)
  }

  /**
    * Get all the orders currently in the model as JSON.
    */
  get( "/orders" ) {
    // create a JSON string from the users, then print it
    implicit val formats = DefaultFormats
    val jsonString = write(Model.orders)
    response.getWriter.print(jsonString)
  }

  /**
    * Request to add user to the model. Respond with user in body if successful.
    *
    * If request body cannot be parsed into JSON, respond with error message in header.
    * If JSON cannot be mapped to user model, respond with error message in header.
    * If user cannot be added by model, respond with error message in header.
    *
    * Expects an incoming JSON string like this:
    * { "email": "johndoe@example.com", "name": "John Doe", "bankAccount": "9876543210" }
    */
  post("/addUser") {

    // get the POST request data
    val jsonString = request.body

    // needed for Lift-JSON
    implicit val formats = DefaultFormats

    // convert the JSON string to a JValue object and check for errors
    Util.parseJsonString(jsonString) match {
      case Some(jValue) => {

        // deserialize jValue into User object
        var user: User = null
        try {
          user = jValue.extract[User]
        } catch {case e: MappingException => logger.warn("Mapping of JSON failed: " + e.getMessage)}

        // Only proceed if mapping succeeded
        if (user != null) {

          // add the user to the model
          val result = Model.addUser(user)

          // check if adding user was successful
          result match {
            case Left(msg) => {
              // notify in header that adding user failed
              response.addHeader("ACK", "Adding user failed: " + msg)
            }
            case Right(u) => {
              // confirm that user was added in the response header
              response.addHeader("ACK", "User added")

              // give user back as JSON in the response body
              write(u)
            }
          }
        }
        else {
          // notify in header that JSON cannot be mapped to user model
          response.addHeader("ACK", "JSON cannot be mapped to user model")
        }
      }
      case None => {
        // notify in header that request body could not be parsed into JSON
        response.addHeader("ACK", "Request body could not be parsed into JSON")
      }
    }
  }

  /**
    * Request to add item to user's chart. Respond with user in body if successful.
    *
    * If request body cannot be parsed into JSON, respond with error message in header.
    * If JSON cannot be mapped to item model, respond with error message in header.
    * If given quantity cannot be parsed, respond with error message in header.
    * If item cannot be added to chart by model, respond with error message in header.
    *
    * Expects an incoming JSON string like this:
    * { "email": "johndoe@example.com", "id": "book001", "quantity": "1" }
    */
  post("/addItemToUserChart") {

    // get the POST request data
    val jsonString = request.body

    // needed for Lift-JSON
    implicit val formats = DefaultFormats

    // convert the JSON string to a JValue object and check for errors
    Util.parseJsonString(jsonString) match {
      case Some(jValue) => {

        // deserialize the request json into a holder object
        case class AddItemModel(email: String, id: String, quantity: String)
        var iModel: AddItemModel = null
        try {
          iModel = jValue.extract[AddItemModel]
        } catch { case e: MappingException => logger.warn("Mapping of JSON failed: " + e.getMessage) }

        // Only proceed if mapping succeeded
        if (iModel != null) {

          // check if quantity can be parsed as Int
          Util.toInt(iModel.quantity) match {
            case Some(q) => {
              // update the user shopping chart in the model
              val result = Model.addItemToUserChart(iModel.email, iModel.id, q)

              // check if adding item to chart was successful
              result match {
                case Left(msg) => {
                  // notify in header that adding item to chart failed
                  response.addHeader("ACK", "Adding item to chart failed: " + msg)
                }
                case Right(u) => {
                  // confirm that item was added to chart in the response header
                  response.addHeader("ACK", "Item added to chart")

                  // give user back as JSON in the response body
                  write(u)
                }
              }
            }
            case None => {
              // notify in header that quantity cannot be parsed as integer
              response.addHeader("ACK", "Value for quantity cannot be parsed as an integer")
            }
          }
        }
        else {
          // notify in header that JSON cannot be mapped to item model
          response.addHeader("ACK", "JSON cannot be mapped to item model")
        }
      }
      case None => {
        // notify in header that request body could not be parsed into JSON
        response.addHeader("ACK", "Request body could not be parsed into JSON")
      }
    }
  }

  /**
    * Request to check out chart and create an order. Respond with order number and total price in body if successful.
    *
    * If request body cannot be parsed into JSON, respond with error message in header.
    * If JSON cannot be mapped to checkout model, respond with error message in header.
    * If the order cannot be created, do nothing and respond with error message in header.
    *
    * Expects an incoming JSON string like this:
    * { "email": "johndoe@example.com", "address": "Mainstreet 1, Johnsville" }
    */
  post("/checkout") {

    // get the POST request data
    val jsonString = request.body

    // needed for Lift-JSON
    implicit val formats = DefaultFormats

    // convert the JSON string to a JValue object and check for errors
    Util.parseJsonString(jsonString) match {
      case Some(jValue) => {

        // deserialize the data of the request into a model object
        case class CheckoutModel(email: String, address: String)
        var cModel: CheckoutModel = null
        try {
          cModel = jValue.extract[CheckoutModel]
        } catch { case e: MappingException => logger.warn("Mapping of JSON failed: " + e.getMessage) }

        // Only proceed if mapping succeeded
        if (cModel != null) {
          // place the order
          val result = Model.addOrder(cModel.email, cModel.address)

          // check if placing order was successful
          result match {
            case Left(msg) => {
              // notify in response header that checkout failed
              response.addHeader("ACK", "Checkout failed: " + msg)
            }
            case Right((orderNumber, total)) => {
              // confirm that checkout succeeded in the response header
              response.addHeader("ACK", "Checkout succeeded")
              // return the order number and total price as JSON
              "{ \"orderNumber\": " + orderNumber + ", \"total\": " + total + "}"
            }
          }
        }
        else {
          // notify in header that JSON cannot be mapped to checkout model
          response.addHeader("ACK", "JSON cannot be mapped to checkout model")
        }
      }
      case None => {
        // notify in header that request body could not be parsed into JSON
        response.addHeader("ACK", "Request body could not be parsed into JSON")
      }
    }
  }

}

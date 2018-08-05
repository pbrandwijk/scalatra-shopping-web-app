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

}

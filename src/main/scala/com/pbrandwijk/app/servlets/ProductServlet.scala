package com.pbrandwijk.app.servlets

import com.pbrandwijk.app.models._
import com.pbrandwijk.app.Model
import com.pbrandwijk.app.Util
import net.liftweb.json.Serialization.write
import net.liftweb.json._
import org.scalatra._
import org.slf4j.LoggerFactory

class ProductServlet extends ScalatraServlet {

  // create logger for class
  val logger =  LoggerFactory.getLogger(getClass)

  /**
    * Get all the products currently in the model.
    */
  get( "/" ) {
    // create a JSON string from the products, then print it
    implicit val formats = DefaultFormats
    val jsonString = write(Model.products)
    response.getWriter.print(jsonString)
  }

  /**
    * Request to add a product to the model. Respond with product in body if successful.
    *
    * If request body cannot be parsed into JSON, respond with error message in header.
    * If JSON cannot be mapped to product model, respond with error message in header.
    * If given price or stock cannot be parsed, respond with error message in header.
    * If product cannot be added by model, respond with error message in header.
    *
    * Expects an incoming JSON string like this:
    * { "id": "book001", "description": "The Da Vinci Code", "price": "9.95", "stock": "10" }
    * or (price is a double, stock is an integer)
    * { "id": "book001", "description": "The Da Vinci Code", "price": 9.95, "stock": 10 }
    */
  post("/addProduct") {

    // get the POST request data
    val jsonString = request.body

    // needed for Lift-JSON
    implicit val formats = DefaultFormats

    // convert the JSON string to a JValue object and check for errors
    Util.parseJsonString(jsonString) match {
      case Some(jValue) => {

        // deserialize the request json into a holder object
        case class ProductModel(id: String, description: String, price: String, stock: String)
        var pModel : ProductModel = null
        try {
          pModel = jValue.extract[ProductModel]
        } catch { case e: MappingException => logger.warn("Mapping of JSON failed: " + e.getMessage) }

        // Only proceed if mapping succeeded
        if (pModel != null) {

          // check if price and stock can be parsed correctly
          (Util.toDouble(pModel.price), Util.toInt(pModel.stock)) match {
            case (Some(p), Some(s)) => {

              // use product model object to create product object
              val product = new Product(pModel.id, pModel.description, p, s)
              logger.info("Parsed product: " + product)

              // add the product to the model
              val result = Model.addProduct(product)

              // check if adding product was successful
              result match {
                case Left(msg) => {
                  // notify in header that adding product failed
                  response.addHeader("ACK", "Adding product failed: " + msg)
                }
                case Right(p) => {
                  // confirm that product was added in the response header
                  response.addHeader("ACK", "Product added")

                  // give product back as JSON in the response body
                  write(p)
                }
              }
            }
            case _ => {
              // notify in header that parsing of product price and stock failed
              response.addHeader("ACK", "Parsing of product price and stock failed")
            }
          }
        }
        else {
          // notify in header that JSON cannot be mapped to product model
          response.addHeader("ACK", "JSON cannot be mapped to product model")
        }
      }
      case None => {
        // notify in header that request body could not be parsed into JSON
        response.addHeader("ACK", "Request body could not be parsed into JSON")
      }
    }

  }

}

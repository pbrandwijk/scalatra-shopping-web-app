package com.pbrandwijk.app.servlets

import org.scalatra._

class ProductServlet extends ScalatraServlet {

  get("/") {
    views.html.hello()
  }

}

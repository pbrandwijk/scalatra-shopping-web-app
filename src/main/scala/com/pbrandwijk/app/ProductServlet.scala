package com.pbrandwijk.app

import org.scalatra._

class ProductServlet extends ScalatraServlet {

  get("/") {
    views.html.hello()
  }

}

package com.example.app.controllers

import org.scalatra._
import org.scalatra.scalate.ScalateSupport

class RootController extends ScalatraServlet with ScalateSupport {
  get("/") {
    contentType = "text/html"

    ssp("/WEB-INF/templates/views/index.ssp")
  }

}

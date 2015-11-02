package com.example.app.controllers

import com.example.app.models.Product
import com.mongodb.casbah.MongoDB
import org.scalatra._
import org.json4s.DefaultFormats
import org.scalatra.json._

class ProductsController extends ScalatraServlet with JacksonJsonSupport {
  protected implicit lazy val jsonFormats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  get("/") {
    Product.all()
  }

  get("/:id") {
    implicit val id = params("id")
    Product.find getOrElse notFound
  }

  def notFound(implicit id: String) {
    halt(404, body = Map("error" -> "Can't find product with id ".concat(id)))
  }

  post("/") {
    val product = Product.fromRequest(params)

    product.save match {
      case true => halt(201, body = Map("success" -> "Product created with success"))
      case _ => halt(422, body = Map("error" -> "Invalid data"))
    }
  }

  delete("/:id") {
    implicit val id = params("id")

    Product.find match {
      case Some(product) => destroy(product)
      case _ => notFound
    }
  }

  put("/:id") {
    implicit val id = params("id")

    Product.find match {
      case Some(product) => update(product)
      case _ => notFound
    }
  }

  private def update(product: Product) {
    if (product.update(params)) {
      val message = "Product with id " + product.id + " updated"
      halt(200, body = Map("success" -> message, "product" -> product))
    } else {
      halt(422, body = Map("error" -> "Invalid data"))
    }
  }

  private def destroy(product: Product) {
    product.delete
    val message = "Product with id " + product.id + " removed"
    halt(200, body = Map("success" -> message))
  }
}

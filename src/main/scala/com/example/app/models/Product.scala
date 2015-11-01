package com.example.app.models

import scala.util.Random

object Product {
  val rand = new Random(100)

  case class Product(name: String, price: Long = 30, id: Long = rand.nextLong()) {}

  val all = List(
    Product("foo"),
    Product("bar", 50),
    Product("foobar", 10)
  )

  def find(implicit  id: Long): Option[Product] = {
    all.find(_.id == id)
  }

}

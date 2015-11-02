package com.example.app.models

import javax.servlet.http.HttpServletRequest

import org.scalatra.Params
import com.mongodb._
import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.MongoDBObject
import org.bson.types.ObjectId

case class Product(name: String, price: Int = 30) {
  protected var id = ""

  def this(name: String, price: Int, id: String) {
    this(name, price)
    this.id = id
  }

  def save: Boolean = {
    if (this.valid) {
      Product.save(this).getField("ok") match {
        case x: Object => x == 1
        case _ => false
      }
    } else {
      false
    }
  }

  def toMongo = {
    id match {
      case "" => MongoDBObject("name" -> name, "price" -> price)
      case _ => MongoDBObject("name" -> name, "price" -> price, "_id" -> id)
    }
  }

  private def valid: Boolean = {
    !this.name.isEmpty
  }
}

object Product {
  private val db = MongoClient("localhost")
  private val collection = db("pismo")("products")

  def fromRequest(data: Params): Product = {
    val name = data.get("product[name]") match {
      case x: Some[String] => x.head
      case _ => ""
    }

    data.get("product[price]") match {
      case x: Some[String] => Product(name, x.head.toInt)
      case _ => Product(name)
    }
  }

  def save(product: Product): WriteResult = {
    this.collection.insert(product.toMongo)
  }

  def find(implicit id: String): Option[Product] = {
    val query = MongoDBObject("_id" -> new ObjectId(id))
    this.collection.findOne(query).map(toProduct)
  }

  def all(limit: Int = 10): List[Product] = {
    this.collection.find().limit(limit).map(toProduct).toList
  }

  private def toProduct(data: DBObject): Product = {
    new Product(
      data.get("name").toString,
      data.get("price").toString.toInt,
      data.get("_id").toString
    )
  }
}

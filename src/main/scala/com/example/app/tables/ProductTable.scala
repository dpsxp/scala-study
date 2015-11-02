package com.example.app.tables

import com.example.app.models.Product
import org.scalatra.Params
import com.mongodb._
import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.MongoDBObject
import org.bson.types.ObjectId

class ProductTable(host: String = "localhost", collectionName: String = "test") {
  private lazy val db = MongoClient("localhost")
  private lazy val collection = db(collectionName)("products")

  def delete(id: String): WriteResult = {
    val query = MongoDBObject("_id" -> new ObjectId(id))
    collection.remove(query)
  }

  def fromRequest(data: Params): Product = {
    val name = data.get("product[name]") match {
      case Some(name) => name
      case _ => ""
    }

    data.get("product[price]") match {
      case Some(price) => Product(name, price.toInt)
      case _ => Product(name)
    }
  }

  def save(product: Product): WriteResult = {
    this.collection.insert(product.toMongo)
  }

  def find(id: String): Option[Product] = {
    try {
      val query = MongoDBObject("_id" -> new ObjectId(id))
      this.collection.findOne(query).map(toProduct)
    } catch {
      case e: IllegalArgumentException => {
        // TODO perform some log here
        None
      }
      case _: Throwable => None
    }
  }

  def update(product: Product): WriteResult = {
    val query = MongoDBObject("_id" -> new ObjectId(product.id))
    val update = product.toMongo
    this.collection.update(query, update, false)
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

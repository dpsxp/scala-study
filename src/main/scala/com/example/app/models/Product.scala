package com.example.app.models

import com.mongodb.DBObject
import org.scalatra.Params
import com.mongodb.casbah.commons.MongoDBObject
import com.example.app.tables.DBTable

case class Product(var name: String = "", var price: Int = 30) extends MongoDocument {
  def this(name: String, price: Int, id: String) {
    this(name, price)
    this.id = id
  }

  def save: Boolean = {
    if (this.valid) {
      Product.save(this).getN match {
        case 1 => true
        case _ => false
      }
    } else {
      false
    }
  }

  def update(data: Map[String, String]): Boolean = {
    (data.get("product[name]"), data.get("product[price]")) match {
      case (None, None) => return false
      case (Some(_name), Some(_price)) => {
        this.name = _name
        this.price = _price.toInt
      }
      case (_, Some(_price)) => this.price = _price.toInt
      case (Some(_name), _) => this.name = _name
    }

    Product.update(this).getN == 1
  }

  def delete() {
    Product.delete(this.id)
  }

  def valid: Boolean = {
    !this.name.isEmpty
  }
}

object Product extends DBTable[Product](databaseName = "pismo", collectionName = "products") {
  override def toMongo(data: Product): DBObject = {
    MongoDBObject("name" -> data.name, "price" -> data.price)
  }

  override def toModel(data: DBObject): Product = {
    new Product(
      data.get("name").toString,
      data.get("price").toString.toInt,
      data.get("_id").toString
    )
  }

  override def fromRequest(data: Params): Product = {
    val name = data.get("product[name]") match {
      case Some(_name) => _name
      case _ => ""
    }

    data.get("product[price]") match {
      case Some(price) => Product(name, price.toInt)
      case _ => Product(name)
    }
  }
}

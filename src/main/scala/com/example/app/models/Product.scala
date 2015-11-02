package com.example.app.models

import com.mongodb.casbah.commons.MongoDBObject
import com.example.app.tables.ProductTable

case class Product(var name: String = "", var price: Int = 30) {
  var id = ""
  private val table = new ProductTable(collectionName = "pismo")

  def this(name: String, price: Int, id: String) {
    this(name, price)
    this.id = id
  }

  def save: Boolean = {
    if (this.valid) {
      table.save(this).getN match {
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

    table.update(this).getN == 1
  }

  def delete() {
    table.delete(this.id)
  }

  def valid: Boolean = {
    !this.name.isEmpty
  }

  def toMongo = {
    MongoDBObject("name" -> name, "price" -> price)
  }
}



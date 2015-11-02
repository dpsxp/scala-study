import com.example.app.models.Product
import org.scalatest._
import org.scalatra.test.scalatest._

class ProductSpec extends ScalatraSuite with FunSpecLike {
  describe("#save") {
    it("saves the current product in the database") (pending)

    it("saves only if the product is valid") {
      val product = new Product()
      product.save should equal(false)
    }
  }

  describe("#update") {
    it("updates the current product in the database") (pending)
  }

  describe("#valid") {
    it("returns true when the name is not empty") {
      val product = Product()
      product.valid should equal(false)
      product.name = "Tester"
      product.valid should equal(true)
    }
  }

  describe("#toMongo") {
    it("returns a MongoDBObject with the name and price values") {
      val product = new Product("test", 50)
      val result = product.toMongo

      result.get("name") should equal("test")
      result.get("price") should equal(50)
    }
  }

  describe("#delete") {
    it("deletes the current product from the database") (pending)
  }
}

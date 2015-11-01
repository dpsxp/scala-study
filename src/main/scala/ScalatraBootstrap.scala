import com.example.app.controllers.ProductsController
import com.mongodb.casbah.MongoClient
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    val mongo = MongoClient("localhost")
    val collection = mongo("pismo")
    context.mount(new ProductsController(collection), "/products")
  }
}

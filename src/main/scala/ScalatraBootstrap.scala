import com.example.app.controllers._
import com.example.app.models.Product
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new RootController, "/*")
    context.mount(new ProductsController(Product), "/products")
  }
}

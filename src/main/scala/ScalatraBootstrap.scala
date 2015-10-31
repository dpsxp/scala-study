import com.example.app._
import com.example.app.controllers.ProductsController
import org.scalatra._
import javax.servlet.ServletContext

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new ProductsController, "/products")
  }
}

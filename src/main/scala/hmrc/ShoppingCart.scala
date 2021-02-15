package hmrc

import hmrc.domain.Item
import cats.Monoid

object domain {
  sealed trait Item {
    def price: BigDecimal
  }

  object Item {
    case class Apple(price: BigDecimal)  extends Item
    case class Orange(price: BigDecimal) extends Item
  }
}

object PriceMonoid extends Monoid[BigDecimal] {
  override def empty: BigDecimal                                 = BigDecimal(0)
  override def combine(x: BigDecimal, y: BigDecimal): BigDecimal = x + y
}

object ShoppingCart {
  def checkout(items: List[Item]): String = {
    val totalCost = items.foldRight(PriceMonoid.empty) {
      case (item, priceAcc) => PriceMonoid.combine(item.price, priceAcc)
    }
    totalCost.print
  }
}

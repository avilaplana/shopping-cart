package hmrc

import hmrc.domain.Item

object domain {
  sealed trait Item {
    def price: BigDecimal
  }

  object Item {
    case class Apple(price: BigDecimal)  extends Item
    case class Orange(price: BigDecimal) extends Item
  }
}

object ShoppingCart {
  def checkout(items: List[Item]): String =
    items
      .map(_.price)
      .sum
      .print
}

package hmrc

import cats.Monoid
import hmrc.domain.Item.Apple
import hmrc.domain.{Item, Offer}

object domain {
  sealed trait Item {
    def price: BigDecimal
  }

  object Item {
    case class Apple(price: BigDecimal)  extends Item
    case class Orange(price: BigDecimal) extends Item
  }

  case class Offer(buy: Int, pay: Int) {
    def applyTo(items: List[Item]): List[Item] = {
      val sizeToPay = (items.size / buy) * pay + items.size % buy
      items.take(sizeToPay)
    }
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

object ShoppingCartWithOffers {

  val appleOffer  = Offer(buy = 2, pay = 1)
  val orangeOffer = Offer(buy = 3, pay = 2)

  def checkout(items: List[Item]): String = {
    val (apples, oranges) = items.partition(_.isInstanceOf[Apple])
    val applesToPay       = appleOffer.applyTo(apples)
    val orangesToPay      = orangeOffer.applyTo(oranges)

    val totalCost = (applesToPay ++ orangesToPay).foldRight(PriceMonoid.empty) {
      case (item, priceAcc) => PriceMonoid.combine(item.price, priceAcc)
    }
    totalCost.print
  }
}

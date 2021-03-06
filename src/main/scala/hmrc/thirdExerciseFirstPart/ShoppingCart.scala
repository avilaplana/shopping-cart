package hmrc.thirdExerciseFirstPart

import cats.Monoid
import hmrc.BigDecimalPrinter
import hmrc.thirdExerciseFirstPart.domain.Item._
import hmrc.thirdExerciseFirstPart.domain.{Basket, Item, Offer}

object domain {
  sealed trait Item {
    def price: BigDecimal
  }

  object Item {
    case object Apple extends Item {
      val price = BigDecimal(0.60)
    }
    case object Orange extends Item {
      val price = BigDecimal(0.25)
    }
    case object Banana extends Item {
      val price = BigDecimal(0.20)
    }
  }

  sealed trait Offer {
    def applyTo(items: List[Item]): List[Item]
  }

  object Offer {
    case object Buy2Pay1 extends Offer {
      override def applyTo(items: List[Item]): List[Item] = {
        val sizeToPay = (items.size / 2) * 1 + items.size % 2
        items.take(sizeToPay)
      }
    }

    case object Buy3Pay2 extends Offer {
      override def applyTo(items: List[Item]): List[Item] = {
        val sizeToPay = (items.size / 3) * 2 + items.size % 3
        items.take(sizeToPay)
      }
    }
  }
  case class Basket(apples: List[Apple.type], oranges: List[Orange.type], bananas: List[Banana.type])

  object Basket {
    def apply(items: List[Item]): Basket =
      items.foldLeft(BasketMonoid.empty) { (agg, item) =>
        val tempBasket = item match {
          case apple: Apple.type   => Basket(apples = List(apple), oranges = List.empty, bananas = List.empty)
          case orange: Orange.type => Basket(apples = List.empty, oranges = List(orange), bananas = List.empty)
          case banana: Banana.type => Basket(apples = List.empty, oranges = List.empty, bananas = List(banana))
        }
        BasketMonoid.combine(agg, tempBasket)
      }
  }
}

object PriceMonoid extends Monoid[BigDecimal] {
  override def empty: BigDecimal                                 = BigDecimal(0)
  override def combine(x: BigDecimal, y: BigDecimal): BigDecimal = x + y
}

object BasketMonoid extends Monoid[Basket] {
  override def empty: Basket = Basket(List.empty, List.empty, List.empty)
  override def combine(x: Basket, y: Basket): Basket = Basket(
    apples = x.apples ++ y.apples,
    oranges = x.oranges ++ y.oranges,
    bananas = x.bananas ++ y.bananas
  )
}

object ShoppingCartWithOffers {

  val appleOffer  = Offer.Buy2Pay1
  val orangeOffer = Offer.Buy3Pay2
  val bananaOffer = Offer.Buy2Pay1

  def checkout(items: List[Item]): String = {
    val basket       = Basket(items)
    val applesToPay  = appleOffer.applyTo(basket.apples)
    val orangesToPay = orangeOffer.applyTo(basket.oranges)
    val bananasToPay = bananaOffer.applyTo(basket.bananas)

    val totalCost = (applesToPay ++ orangesToPay ++ bananasToPay).foldRight(PriceMonoid.empty) {
      case (item, priceAcc) => PriceMonoid.combine(item.price, priceAcc)
    }
    totalCost.print
  }
}

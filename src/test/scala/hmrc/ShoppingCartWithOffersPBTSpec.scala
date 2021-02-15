package hmrc

import hmrc.domain.Item
import hmrc.domain.Item.{Apple, Orange}
import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.ScalacheckShapeless._
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class ShoppingCartWithOffersPBTSpec extends AnyFreeSpec with Matchers with ScalaCheckDrivenPropertyChecks {

  private val apple  = Apple(BigDecimal(0.60))
  private val orange = Orange(BigDecimal(0.25))

  implicit val arbItem: Arbitrary[Item] = Arbitrary(Gen.oneOf(Seq(apple, orange)))

  s"checkout should calculate the total cost when the list of items is provided" in {
    forAll { items: List[Item] => ShoppingCartWithOffers.checkout(items) shouldBe calculateCost(items) }
  }

  private def calculateCost(items: List[Item]) = {
    val oranges = items.filter(_.isInstanceOf[Orange]).size
    val apples  = items.filter(_.isInstanceOf[Apple]).size

    val applesPrice  = (apples / 2 + apples         % 2) * apple.price
    val orangesPrice = ((oranges / 3) * 2 + oranges % 3) * orange.price
    (applesPrice + orangesPrice).print
  }
}

package hmrc.thirdExerciseSecondPart

import hmrc.BigDecimalPrinter
import hmrc.thirdExerciseSecondPart.domain.Item
import hmrc.thirdExerciseSecondPart.domain.Item.{Apple, Banana, Orange}
import org.scalacheck.ScalacheckShapeless._
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class ShoppingCartWithOffersPBTSpec extends AnyFreeSpec with Matchers with ScalaCheckDrivenPropertyChecks {

  implicit val arbItem: Arbitrary[Item] = Arbitrary(Gen.oneOf(Seq(Apple, Orange, Banana)))

  s"checkout should calculate the total cost when the list of items is provided" in {
    forAll { items: List[Item] => ShoppingCartWithOffers.checkout(items) shouldBe calculateCost(items) }
  }

  private def calculateCost(items: List[Item]) = {
    val oranges = items.filter(_.isInstanceOf[Orange.type]).size
    val apples  = items.filter(_.isInstanceOf[Apple.type]).size
    val bananas = items.filter(_.isInstanceOf[Banana.type]).size

    val applesPrice  = (apples / 2 + apples         % 2) * Apple.price
    val orangesPrice = ((oranges / 3) * 2 + oranges % 3) * Orange.price
    val bananasPrice = (bananas / 2 + bananas       % 2) * Banana.price

    val bananasAndApplePrice = if (applesPrice > bananasPrice) applesPrice else bananasPrice
    (orangesPrice + bananasAndApplePrice).print
  }
}

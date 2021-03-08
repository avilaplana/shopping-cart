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
    forAll(minSuccessful(100)) { items: List[Item] =>
      ShoppingCartWithOffers.checkout(items) shouldBe calculateCost(items)
    }
  }

  private def calculateCost(items: List[Item]) = {
    val oranges = items.filter(_.isInstanceOf[Orange.type])
    val apples  = items.filter(_.isInstanceOf[Apple.type])
    val bananas = items.filter(_.isInstanceOf[Banana.type])

    val applesPrice  = (apples.size / 2 + apples.size         % 2) * Apple.price
    val orangesPrice = ((oranges.size / 3) * 2 + oranges.size % 3) * Orange.price
    val bananasPrice = (bananas.size / 2 + bananas.size       % 2) * Banana.price

    val comboAppleAndBanana =
      if (apples.size > 0 && bananas.size > 0)
        if (Banana.price <= Apple.price) applesPrice
        else bananasPrice
      else bananasPrice + applesPrice

    (orangesPrice + comboAppleAndBanana).print
  }
}

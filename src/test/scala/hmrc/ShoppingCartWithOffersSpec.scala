package hmrc

import hmrc.domain.Item.{Apple, Orange}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class ShoppingCartWithOffersSpec extends AnyFreeSpec with Matchers with TableDrivenPropertyChecks {

  private val apple  = Apple(BigDecimal(0.60))
  private val orange = Orange(BigDecimal(0.25))

  private val testCases =
    Table(
      ("items", "totalCost"),
      (List(), "£0.00"),
      (List(apple), "£0.60"),
      (List(orange), "£0.25"),
      (List(apple, orange), "£0.85"),
      (List(apple, apple, orange, apple), "£1.45"),
      (List(orange, orange, orange, apple), "£1.10")
    )

  forAll(testCases) { (items, totalCost) =>
    s"checkout should calculate the total cost ${totalCost} when the list of items is $items" in {
      ShoppingCartWithOffers.checkout(items) shouldBe totalCost
    }
  }
}

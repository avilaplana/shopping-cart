package hmrc

import hmrc.domain2.Item.{Apple, Banana, Orange}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class ShoppingCartWithOffers2Spec extends AnyFreeSpec with Matchers with TableDrivenPropertyChecks {

  private val testCases =
    Table(
      ("items", "totalCost"),
      (List(), "£0.00"),
      (List(Apple), "£0.60"),
      (List(Orange), "£0.25"),
      (List(Banana), "£0.20"),
      (List(Apple, Orange, Banana), "£1.05"),
      (List(Apple, Apple, Orange, Apple, Banana, Banana), "£1.65"),
      (List(Orange, Orange, Orange, Apple), "£1.10")
    )

  forAll(testCases) { (items, totalCost) =>
    s"checkout should calculate the total cost ${totalCost} when the list of items is $items" in {
      ShoppingCartWithOffers2.checkout(items) shouldBe totalCost
    }
  }
}

package hmrc.thirdExerciseSecondPart

import hmrc.thirdExerciseSecondPart.domain.Item.{Apple, Banana, Orange}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks

class ShoppingCartWithOffersSpec extends AnyFreeSpec with Matchers with TableDrivenPropertyChecks {

  private val testCases =
    Table(
      ("items", "totalCost"),
      (List(), "£0.00"),
      (List(Apple), "£0.60"),
      (List(Orange), "£0.25"),
      (List(Banana), "£0.20"),
      (List(Banana, Apple), "£0.60"),
      (List(Banana, Banana, Apple), "£0.60"),
      (List(Banana, Banana, Banana, Banana, Banana, Banana, Banana, Apple), "£0.60"),
      (List(Apple, Orange, Banana), "£0.85"),
      (List(Apple, Apple, Orange, Apple, Banana), "£1.45"),
      (List(Apple, Apple, Orange, Apple, Banana, Banana), "£1.45"),
      (List(Orange, Orange, Orange, Apple), "£1.10")
    )

  forAll(testCases) { (items, totalCost) =>
    s"checkout should calculate the total cost ${totalCost} when the list of items is $items" in {
      ShoppingCartWithOffers.checkout(items) shouldBe totalCost
    }
  }
}

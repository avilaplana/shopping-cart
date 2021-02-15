package hmrc

import hmrc.domain.Item
import org.scalacheck.ScalacheckShapeless._
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

class ShoppingCartPBTSpec extends AnyFreeSpec with Matchers with ScalaCheckDrivenPropertyChecks {

  implicit val arbBigDecimal = Arbitrary(for {
    value <- Gen.chooseNum(0, 100000)
  } yield BigDecimal(value, 2))

  s"checkout should calculate the total cost when the list of items is provided" in {
    forAll { items: List[Item] => ShoppingCart.checkout(items) shouldBe calculateCost(items) }
  }
  private def calculateCost(items: List[Item]) = items.map(_.price).sum.print
}

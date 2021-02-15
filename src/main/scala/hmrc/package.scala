import scala.math.BigDecimal.RoundingMode

package object hmrc {
  implicit class BigDecimalPrinter(bd: BigDecimal) {
    def print: String = s"£${bd.setScale(2, RoundingMode.HALF_UP)}"
  }
}

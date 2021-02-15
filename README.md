
# Commits

**Fist problem - TDD and quick solution**

Based on the example:

```
For example: [ Apple, Apple, Orange, Apple ] => £2.05
```


The API is the following:

```
def checkout(items: List[Item]): String
```

With the following model:

```
 sealed trait Item {
    def price: BigDecimal
  }

  object Item {
    case class Apple(price: BigDecimal)  extends Item
    case class Orange(price: BigDecimal) extends Item
  }
```  

This solution is based on TDD where the logic to calculate the total price is simple:


```
items.map(_.price).sum
```

For testing I decided to use `TableDrivenPropertyChecks` because I think is an easy way to show all the use cases in a single table.

**First problem - Add monoid type class to calculate total cost**

I decided to refactor the prod code using a `cats Monoid type class` in order to encapsulate the behaviour related with the addition of money

```
object PriceMonoid extends Monoid[BigDecimal] {
  override def empty: BigDecimal                                 = BigDecimal(0)
  override def combine(x: BigDecimal, y: BigDecimal): BigDecimal = x + y
}

```

The method now looks more robust and elegant:

```
    def checkout(items: List[Item]): String = {
    val totalCost = items.foldRight(PriceMonoid.empty) {
      case (item, priceAcc) => PriceMonoid.combine(item.price, priceAcc)
    }
    totalCost.print
  }
 ```

**First problem - Add Property Based Testing**

In this commit I added another test to show how it would be the use of `Property Based Testing` using `scalacheck` and `shapeless`.
I have been using it for a while in unit tests, specially when I want to check the boundaries of the data model. I think is useful but because is less descriptive than normal unit tests, the code end up being very generic and normally forces you to develop logic to validate the results. i.e: 

```
    private def calculateCost(items: List[Item]) = items.map(_.price).sum.print

```

I have seen people not very happy with PBT, specially people that consider tests like documentation. 

**Second problem - TDD and solution**

To face this solution and to avoid mess with the current code, I have decided to create another object `ShoppingCartWithOffers`.

I have modelled the offer with a new type in the domain:

```
case class Offer(buy: Int, pay: Int) {
    def applyTo(items: List[Item]): List[Item] = {
      val sizeToPay = (items.size / buy) * pay + items.size % buy
      items.take(sizeToPay)
    }
  }
```  

The idea was to represent `offer buy 2 pay 1` as Offer(2,1)

For testing I decided to use `TableDrivenPropertyChecks`

**Second problem - Add Property Based Testing**

In this commit I added another test to show how it would be the use of `Property Based Testing` using `scalacheck` and `shapeless`. I think is good in this case because the test can generate more complex combinations of items.
The negative side of this approach is because the test is generic I needed to create the following method to validate the results.

```
private def calculateCost(items: List[Item]) = {
    val oranges = items.filter(_.isInstanceOf[Orange]).size
    val apples  = items.filter(_.isInstanceOf[Apple]).size

    val applesPrice  = (apples / 2 + apples         % 2) * apple.price
    val orangesPrice = ((oranges / 3) * 2 + oranges % 3) * orange.price
    (applesPrice + orangesPrice).print
  }
```

# Some notes

My intention was modelling the solution based on the statement, introducing domain concepts like Items, Orange, Apple and Offer. 

Another valid alternative to have modelled `Apple` and `Orange` could have been as:

```
case object Apple extends Item {
      val price = BigDecimal(0.60)
    }
case object Orange extends Item {
      val price = BigDecimal(0.25)
    }
```

In the second solution, for simplicity I hardcoded the offers in the object `ShoppingCartWithOffers` as:

```
val appleOffer  = Offer(buy = 2, pay = 1)
val orangeOffer = Offer(buy = 3, pay = 2)
```

Ideally this could be injected in the method or in the constructor.

Regarding tests, I have proposed both approaches, usual unit tests and PBT to show my skills. In reality I would use the one that me and my pair would consider better for the use case.

I have kept all the production code in the same file (ShoppingCart) for readability purpose. 

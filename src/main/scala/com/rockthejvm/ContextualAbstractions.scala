package com.rockthejvm

object ContextualAbstractions extends App {

  /*
    1 - context parameters/arguments
  */

  val aList = List(2,1,3,4)
  val anOrderedList = aList.sorted // (contextual arg - ordering automatically injected by compiler)

  // Ordering
  given descendingOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _) // (a,b) => a > b

  // analogous to an implicit val

  trait Combinator[A] { // monoid
    def combine(x: A, y: A): A
  }

  def combineAll[A](list: List[A])(using combinator: Combinator[A]): A =
    list.reduce(combinator.combine) // (a,b) => combinator.combine(a,b)

  given intCombinator: Combinator[Int] = (x: Int, y: Int) => x + y

  val theSum = combineAll(aList)

  /*
    Given places
    - local scope
    - imported scope (import yourpackage.given)
    - companions of all the types involved in the call
      - companion of List
      - companion of Int
  */

  // context bounds
  def combineAll_v2[A](list: List[A])(using Combinator[A]):A = ???
  def combineAll_v3[A : Combinator](list: List[A]):A = ???

  /*
    where context args are useful
    - type classes
    - dependency injection
    - context-dependent functionality
    - type-level programming
  * */

  /*
    2 - extension methods
  * */

  case class Person(name: String) {
    def greet(): String = s"Hi, my name is $name, I love Scala!"
  }

  extension (string: String)
    def greet(): String = new Person(string).greet()

  val danielsGreeting = "Daniel".greet() // "type enrichment" fka pimping

  // POWER
  extension [A] (list: List[A])
    def combineAllValues(using combinator: Combinator[A]): A =
      list.reduce(combinator.combine)

  val theSum_v2 = aList.combineAllValues

  println(anOrderedList)
  println(theSum)
  println(theSum_v2)
}

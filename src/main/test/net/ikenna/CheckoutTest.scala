package net.ikenna

import org.scalatest.FunSuite

class CheckoutTest extends FunSuite {


  test("Scan item A") {
    val total = new Checkout().scan(ItemA).getTotal()
    expectResult(50)(total)
  }

  test("Scan item B") {
    val total = new Checkout().scan(ItemB).getTotal()
    expectResult(30)(total)
  }

  test("Scan item C") {
    val total = new Checkout().scan(ItemC).getTotal()
    expectResult(20)(total)
  }

  test("Scan item D") {
    val total = new Checkout().scan(ItemD).getTotal()
    expectResult(15)(total)
  }

  test("Scan items A, B, C, D") {
    val total = new Checkout()
      .scan(ItemA)
      .scan(ItemB)
      .scan(ItemC)
      .scan(ItemD)
      .getTotal()
    expectResult(115)(total)
  }

  test("Scan 3 item A") {
    val total = new Checkout()
      .addMultiBuy(new MultiBuy(ItemA, 3, 130))
      .scan(ItemA)
      .scan(ItemA)
      .scan(ItemA)
      .getTotal()
    expectResult(130)(total)
  }

  test("Scan 3 item A and 2 Item B") {
    val total = new Checkout()
      .addMultiBuy(new MultiBuy(ItemA, 3, 130))
      .addMultiBuy(new MultiBuy(ItemB, 2, 45))
      .scan(ItemA)
      .scan(ItemA)
      .scan(ItemA)
      .scan(ItemA)
      .scan(ItemB)
      .scan(ItemB)
      .getTotal()
    expectResult(225)(total)
  }

  test("Find number of multibuys discounts to be applied"){
    val items =  Vector(ItemA, ItemA, ItemB, ItemA)
    val result = new Discounter(items, new MultiBuy(ItemA, 3, 130)).itemsEligibleForThisMultibuy()
    assert(result.size === 3)
  }

  test("Find number of multibuys that apply"){
    val items:Seq[Item] =  Vector(ItemA, ItemA, ItemB, ItemA, ItemA, ItemA, ItemA, ItemA)
    val result = new Discounter(items, new MultiBuy(ItemA, 3, 130)).numberOfMultibuysThatApply()
    assert(result === 2)
  }

  test("Sum discount for each multibuy"){
    val items:Seq[Item] =  Vector(ItemA, ItemA, ItemB, ItemA, ItemA, ItemA, ItemA)
    val result = new Discounter(items, new MultiBuy(ItemA, 3, 130)).totalDiscountForMultibuy()
    assert(result === 40)
  }

  test("Multibuy discount"){
    val result =  new MultiBuy(ItemA, 3, 130).discount
    expectResult(result)(20)
  }

  test("Should add multibuy"){
    val checkout = new Checkout()
      .addMultiBuy(new MultiBuy(ItemA, 3, 130))
      .addMultiBuy(new MultiBuy(ItemA, 3, 130))

    assert(checkout.multiBuys.size === 2)
  }

}


class Discounter(items: Seq[Item], multibuy: MultiBuy){

   def itemsEligibleForThisMultibuy():Seq[Item]={
    items.filter(item => item == multibuy.item)
  }

  def numberOfMultibuysThatApply():Int = {
    itemsEligibleForThisMultibuy.grouped(multibuy.count).count(group => group.size == multibuy.count)
  }

  def totalDiscountForMultibuy():Int={
    numberOfMultibuysThatApply() * multibuy.discount
  }
}

class Total(val totalValue: Int = 0) {

  def add(itemCost: Int): Total = {
    new Total(totalValue + itemCost)
  }
}

class Checkout(val items: Seq[Item], val multiBuys:Seq[MultiBuy]) {
  def  this() = this(Vector[Item](), Vector[MultiBuy]())

  def addMultiBuy(multiBuy: MultiBuy):Checkout = new Checkout(items, multiBuys :+ multiBuy)

  def scan(item: Item): Checkout = {
    new Checkout(items :+ item, multiBuys)
  }

  def getTotal(): Int = {
    val costOfItems = items.foldLeft(0)((acc:Int, item) => acc + item.price)
    val totalDiscount = multiBuys.map(multiBuy => new Discounter(items, multiBuy).totalDiscountForMultibuy()).sum
    costOfItems - totalDiscount
  }
}

trait Item {
  val sku: String
  val price: Int
}


object ItemA extends Item {
  val sku: String = "A"
  val price: Int = 50
}

object ItemB extends Item {
  val sku: String = "B"
  val price: Int = 30
}

object ItemC extends Item {
  val sku: String = "C"
  val price: Int = 20
}


object ItemD extends Item {
  val sku: String = "D"
  val price: Int = 15
}

class MultiBuy(val item: Item, val count: Int, val specialPrice: Int) {
  val discount:Int = item.price * count - specialPrice
}
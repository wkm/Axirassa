package test.scala

import org.junit.Test
import org.junit.Assert._

object TestScala extends App {
  override def main(args: Array[String]) {
    println("Hello World")
  }
}

class TestScala {
  @Test
  def test() {
    assertEquals("hi", "hi")
  }
}
package net.zgmfx.bfscala

import org.scalatest.FunSuite

class AstSuite extends FunSuite {
  test("translate") {
    val code = List('+', '-', '>', '<', '.', ',', List('+'))
    assert(AstHelper.translate(code) ===
      List(Inc(1), Dec(1), Fwd(1), Back(1), Write, Read, Block(List(Inc(1)))))
  }

  test("translate invalid code") {
    intercept[IllegalArgumentException] {
      val code = List('*')
      val tsd = AstHelper.translate(code)
    }
  }

  test("translate recursive") {
    val code = List(List('+', List('.'), '-'), '+')
    assert(AstHelper.translate(code) ===
      List(Block(List(Inc(1), Block(List(Write)), Dec(1))), Inc(1)))
  }

  test("optimize") {
    val code = List('+', '+', '+', List('.'), '-', '-', '>', '>', '>')
    val ast = AstHelper.optimize(AstHelper.translate(code))
    assert(ast === List(Inc(3), Block(List(Write)), Dec(2), Fwd(3)))
  }

  test("optimize recursive") {
    val code = List('>', List('+', '+', '+', '-'))
    val ast = AstHelper.optimize(AstHelper.translate(code))
    assert(ast === List(Fwd(1), Block(List(Inc(3), Dec(1)))))
  }

}
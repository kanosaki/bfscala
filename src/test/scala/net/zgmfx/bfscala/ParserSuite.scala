package net.zgmfx.bfscala

import org.scalatest.FunSuite

class ParserSuite extends FunSuite {
  test("Instructions") {
    val result = BfParser.parse("+-><.,")
    assert(result === List('+', '-', '>', '<', '.', ','))
  }

  test("Blocks") {
    val result = BfParser.parse("+[--]")
    assert(result === List('+', List('-', '-')))
  }

  test("Block rec") {
    val result = BfParser.parse("[[+]]")
    assert(result === List(List(List('+'))))
  }

  test("Code") {
    val result = BfParser.parse("+-[><[++]-].")
    assert(result === List('+', '-', List('>', '<', List('+', '+'), '-'), '.'))
  }

}
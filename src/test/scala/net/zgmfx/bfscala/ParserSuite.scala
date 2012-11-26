package net.zgmfx.bfscala

import org.scalatest.FunSuite

class ParserSuite extends FunSuite {
  test("Instructions") {
    val result = BfParser.parse("+-><., ")
    assert(result === List(Inc(1), Dec(1), Fwd(1), Back(1), Write, Read))
  }

  test("Blocks") {
    val result = BfParser.parse("+[--]", false)
    assert(result === List(Inc(1), Block(List(Dec(1), Dec(1)))))
  }

  test("Block rec") {
    val result = BfParser.parse("[[+]]", false)
    assert(result === List(Block(List(Block(List(Inc(1)))))))
  }

  test("Code") {
    val result = BfParser.parse("+-[><[++]-].", false)
    assert(result === List(Inc(1), Dec(1), Block(List(Fwd(1), Back(1), Block(List(Inc(1), Inc(1))), Dec(1))), Write))
  }
}
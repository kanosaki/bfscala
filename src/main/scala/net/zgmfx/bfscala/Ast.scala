package net.zgmfx.bfscala

import scala.collection.mutable.LinkedList
import scala.collection.mutable.Buffer
import scala.collection.mutable.ArrayBuffer

abstract class Inst
case class Inc(n: Int) extends Inst
case class Dec(n: Int) extends Inst
case class Fwd(n: Int) extends Inst
case class Back(n: Int) extends Inst
case object Write extends Inst
case object Read extends Inst
case class Block(code: List[Inst]) extends Inst

class Ast(insts: List[Inst]) {
  def code = insts
}

object AstHelper {
  def optimize(code: List[Inst]): List[Inst] = optimizeCode(code, Nil)

  private def optimizeCode(code: List[Inst], result: List[Inst]): List[Inst] = {
    code match {
      case Nil                          => result.reverse
      case Inc(x) :: Inc(y) :: remain   => optimizeCode(Inc(x + y) :: remain, result)
      case Dec(x) :: Dec(y) :: remain   => optimizeCode(Dec(x + y) :: remain, result)
      case Fwd(x) :: Fwd(y) :: remain   => optimizeCode(Fwd(x + y) :: remain, result)
      case Back(x) :: Back(y) :: remain => optimizeCode(Back(x + y) :: remain, result)
      case Block(x) :: remain           => optimizeCode(remain, optimizeBlock(x) :: result)
      case x :: remain                  => optimizeCode(remain, x :: result)
    }
  }

  private def optimizeBlock(code: List[Inst]): Block = {
    Block(optimizeCode(code, Nil))
  }

  def translate(code: List[Any]): List[Inst] = {
    for (inst <- code) yield inst match {
      case '+'            => Inc(1)
      case '-'            => Dec(1)
      case '>'            => Fwd(1)
      case '<'            => Back(1)
      case '.'            => Write
      case ','            => Read
      case block: List[_] => Block(translate(block))
      case _              => throw new IllegalArgumentException
    }
  }
}

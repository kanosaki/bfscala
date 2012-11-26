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

class Ast(rawCode: List[Any]) {
  lazy val translated: List[Inst] = translate(rawCode)
  lazy val code = optimize(translated, Nil)

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

  def optimize(code: List[Inst], result: List[Inst]): List[Inst] = {
    code match {
      case Nil                          => result.reverse
      case Inc(x) :: Inc(y) :: remain   => optimize(Inc(x + y) :: remain, result)
      case Dec(x) :: Dec(y) :: remain   => optimize(Dec(x + y) :: remain, result)
      case Fwd(x) :: Fwd(y) :: remain   => optimize(Fwd(x + y) :: remain, result)
      case Back(x) :: Back(y) :: remain => optimize(Back(x + y) :: remain, result)
      case Block(x) :: remain           => optimize(remain, optimizeBlock(x) :: result)
      case x :: remain                  => optimize(remain, x :: result)
    }
  }

  def optimizeBlock(code: List[Inst]): Block = {
    Block(optimize(code, Nil))
  }
}
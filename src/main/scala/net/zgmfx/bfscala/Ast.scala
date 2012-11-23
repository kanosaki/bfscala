package net.zgmfx.bfscala

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
  lazy val code = optimize(translated)

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

  def optimize(code: List[Inst]): List[Inst] = {
    code match {
      case Inc(x) :: Inc(y) :: remain   => optimize(Inc(x + y) :: remain)
      case Dec(x) :: Dec(y) :: remain   => optimize(Dec(x + y) :: remain)
      case Fwd(x) :: Fwd(y) :: remain   => optimize(Fwd(x + y) :: remain)
      case Back(x) :: Back(y) :: remain => optimize(Back(x + y) :: remain)
      case Block(x) :: remain           => Block(optimize(x)) :: optimize(remain)
      case x :: remain                  => x :: optimize(remain)
      case Nil                          => Nil
    }
  }
}
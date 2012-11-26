package net.zgmfx.bfscala

import scala.util.parsing.combinator.JavaTokenParsers

object BfParser extends JavaTokenParsers {
  override type Elem = Char
  val pInsts = elem('+') ^^^ Inc(1) |
    elem('-') ^^^ Dec(1) |
    elem('<') ^^^ Back(1) |
    elem('>') ^^^ Fwd(1) |
    elem('.') ^^^ Write |
    elem(',') ^^^ Read

  val pBlock: Parser[Inst] = ('[' ~> pCode <~ ']') ^^ ((i: List[Inst]) => Block(i))
  val pCode: Parser[List[Inst]] = rep(pInsts | pBlock)

  def parse(text: String, reduction: Boolean = true) = {
    val parsed = parseAll(pCode, text.replaceAll("\\s", "")).get
    if (reduction)
      AstHelper.optimize(parsed)
    else
      parsed
  }
}
package net.zgmfx.bfscala

import scala.util.parsing.combinator.JavaTokenParsers

object BfParser extends JavaTokenParsers {
  val insts = Seq('+', '-', '<', '>', '.', ',')
  override type Elem = Char
  val pInsts = insts.map(elem(_)).reduce(_ | _);
  // val baseInst = elem('+') | elem('-') | elem('<') | elem('>') | elem('.') | elem(',')

  val pBlock: Parser[List[Any]] = '[' ~> pCode <~ ']'
  val pCode: Parser[List[Any]] = rep(pInsts | pBlock)

  def parse(text: String) = {
    parseAll(pCode, text.replaceAll("\\s", "")).get
  }
}
package net.zgmfx.bfscala

import scala.util.parsing.combinator.JavaTokenParsers

object BfParser extends JavaTokenParsers {
  override type Elem = Char
  val plus = elem("PLUS", _ == '+')
  val minus = elem("MINUS", _ == '-')
  val forward = elem("FORWARD", _ == '>')
  val back = elem("BACK", _ == '<')
  val output = elem("OUTPUT", _ == '.')
  val input = elem("INPUT", _ == ',')
  val baseInst = plus | minus | forward | back | output | input
  val block : Parser[List[Any]] = '[' ~> rep(baseInst | block) <~ ']'
  val code : Parser[List[Any]] = rep(block|baseInst)
  
  def parse(text: String) = {
    parseAll(code, text).get
  }
}
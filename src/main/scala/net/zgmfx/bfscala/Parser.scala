package net.zgmfx.bfscala

import scala.util.parsing.combinator.JavaTokenParsers

object BfParser extends JavaTokenParsers {
  override type Elem = Char
  val baseInst = elem('+') | elem('-') | elem ('<') | elem('>') | elem('.') | elem(',') 

  val block: Parser[List[Any]] = '[' ~> code <~ ']'
  val code: Parser[List[Any]] = rep(baseInst | block)

  def parse(text: String) = {
    parseAll(code, text.replaceAll("\\s", "")).get
  }
}
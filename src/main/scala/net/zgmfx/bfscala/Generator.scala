package net.zgmfx.bfscala

abstract class Generator {
  def compile(ast: Ast, outPath: String)
}
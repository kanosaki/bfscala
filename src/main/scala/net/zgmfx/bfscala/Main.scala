package net.zgmfx.bfscala

import scala.io.Source
import org.apache.commons.io.FileUtils
import java.io.File
import org.apache.commons.io.IOUtils

object Main {
  def main(args: Array[String]) {
    //val interp = new Interpreter
    val src =
      if (args.length == 1)
        FileUtils.readFileToString(new File(args(0)))
      else
        IOUtils.toString(System.in)
    //interp.run(src, true)
    val rawcode = BfParser.parse(src)
    val code = new Ast(rawcode)
    val gen = new CGenerator
    gen.compile(code)
  }
}
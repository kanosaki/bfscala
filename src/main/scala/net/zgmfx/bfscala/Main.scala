package net.zgmfx.bfscala

import scala.io.Source
import org.apache.commons.io.FileUtils
import java.io.File
import org.apache.commons.io.IOUtils

case class Config(input: Option[String] = None,
                  output: Option[String] = None,
                  compile: Option[String] = None,
                  keep: Boolean = false)

object Main {
  def main(args: Array[String]) {
    val parser = new scopt.immutable.OptionParser[Config]("bfscala", "1.0") {
      def options = Seq(
        opt("c", "compile", "Compile Type") { (v, c) => c.copy(compile = Some(v)) },
        opt("o", "output", "Output file path") { (v, c) => c.copy(output = Some(v)) },
        flag("k", "keep", "Keep temp file") { _.copy(keep = true) },
        argOpt("", "input") { (v, c) => c.copy(input = Some(v)) })
    }
    parser.parse(args, Config()) map { cfg =>
      val src = cfg.input match {
        case Some(path) => FileUtils.readFileToString(new File(path))
        case None       => IOUtils.toString(System.in)
      }
      cfg.compile match {
        case Some("c")  => compileC(src, cfg.output, cfg.keep)
        case Some("js") => compileJS(src, cfg.output)
        case None       => interpret(src)
      }
    }
  }

  def compileC(srccode: String, output: Option[String], keepTemp: Boolean) {
    compile(srccode, output.getOrElse("a.out"), new CGenerator(keepTemp))
  }

  def compileJS(srccode: String, output: Option[String]) {
    compile(srccode, output.getOrElse("output.js"), new JSGenerator)
  }

  def compile(srccode: String, output: String, gen: Generator) {
    val ast = new Ast(BfParser.parse(srccode))
    gen.compile(ast, output)
  }

  def interpret(src: String) {
    val interp = new Interpreter
    interp.run(src, true)
  }
}
package net.zgmfx.bfscala

import org.scalatest.FunSuite

class BinaryStreamsSuite extends FunSuite {
  test("input") {
    val strs = new BinaryStreams
    strs.addInput("Foo")
    assert(strs.input() === 'F'.toInt)
    assert(strs.input() === 'o'.toInt)
    assert(strs.input() === 'o'.toInt)
  }
  test("output") {
    val strs = new BinaryStreams
    strs.output('B'.toInt)
    strs.output('a'.toInt)
    strs.output('r'.toInt)
    assert(strs.toString === "Bar")
  }
}

class InterpreterSuite extends FunSuite {
  test("Input output") {
    val streams = new BinaryStreams
    streams.addInput("A")
    val code = BfParser.parse(",++.")
    val interp = new Interpreter(read = streams.input, write = streams.output)
    interp.runRaw(code)
    assert(streams.toString === "C")
  }
  test("simple instructions") {
    val code = BfParser.parse("++>+++>++++--<--<--")
    val interp = new Interpreter
    interp.runRaw(code)
    val targetMemArea = interp.memory.slice(0, 3)
    assert(targetMemArea === Array(0, 1, 2))
  }
  test("Void input") {
    intercept[IllegalStateException] {
      val streams = new BinaryStreams
      val code = BfParser.parse(",")
      val interp = new Interpreter(read = streams.input, write = streams.output)
      interp.runRaw(code)
    }
  }
  test("Block skip") {
    val code = BfParser.parse("[>]+>+")
    val interp = new Interpreter
    interp.runRaw(code)
    val targetMemArea = interp.memory.slice(0, 4)
    assert(targetMemArea === Array(1, 1, 0, 0))
  }

  test("Block") {
    val code = BfParser.parse("+++[>+>++>+++<<<-]>")
    val interp = new Interpreter
    interp.runRaw(code)
    val targetMemArea = interp.memory.slice(0, 5)
    assert(targetMemArea === Array(0, 3, 6, 9, 0))
  }
  test("Hello wrold! - raw") {
    val streams = new BinaryStreams
    val code = BfParser.parse("+++++++++[>++\n++++++>++++++++++\t+>+  ++++<<<-]>.>++.+++++++..+++.>-.------------.<++++++++.--------.+++.------.--------.>+.")
    val interp = new Interpreter(memsize = 2, read = streams.input, write = streams.output)
    interp.runRaw(code)
    assert(streams.toString === "Hello, world!")
  }
  test("Memory expanding") {
    val code = BfParser.parse("+>>>+")
    val streams = new BinaryStreams
    val interp = new Interpreter(memsize = 2, read = streams.input, write = streams.output)
    assert(interp.memory.length === 2)
    interp.runRaw(code)
    assert(interp.memory.length === 4)
    val targetMemArea = interp.memory.slice(0, 4)
    assert(targetMemArea === Array(1, 0, 0, 1))
  }
  test("Hello wrold!") {
    val streams = new BinaryStreams
    val source = "+++++++++[>++\n++++++>++++++++++\t+>+  ++++<<<-]>.>++.+++++++..+++.>-.------------.<++++++++.--------.+++.------.--------.>+."
    val interp = new Interpreter(read = streams.input, write = streams.output)
    interp.run(source)
    assert(streams.toString === "Hello, world!")
  }
}
package net.zgmfx.bfscala

import scala.collection.mutable.Queue

object DefaultStreams {
  def input(a: Any) = {
    System.in.read
  }
  def output(c: Int) = {
    System.out.write(c)
  }
}

class BinaryStreams(waitInput: Boolean = false) {

  val outBuffer: StringBuilder = new StringBuilder
  val inQueue: Queue[Int] = new Queue[Int]

  def input(a: Any) = {
    if (inQueue.isEmpty && !waitInput)
      throw new IllegalStateException("Input is empty")
    inQueue.dequeue
  }

  def addInput(str: String) {
    inQueue.enqueue(str.map(_.toInt): _*)
  }

  def output(c: Int) {
    outBuffer += c.toChar
  }

  override def toString() = {
    outBuffer.toString
  }

}

class Interpreter(memsize: Int = 1024,
                  read: Unit => Int = DefaultStreams.input,
                  write: Int => Unit = DefaultStreams.output) {

  var memory = new Array[Int](memsize);
  var ptr = 0
  
  def run(code: String, flushNewLine: Boolean = false): Unit = {
    val tokens = BfParser.parse(code);
    val ast = new Ast(tokens)
    val optimized = ast.code
    eval(optimized, false)
    if (flushNewLine) write('\n')
  }

  private def expandMemory = {
    val newmemory = new Array[Int](memory.length * 2)
    Array.copy(memory, 0, newmemory, 0, memory.length)
    memory = newmemory
  }

  protected def inc(n: Int): Unit = {
    memory(ptr) += n;
  }

  protected def dec(n: Int): Unit = {
    memory(ptr) -= n;
  }

  protected def foward(n: Int): Unit = {
    ptr += n
    if (memory.length <= ptr)
      expandMemory
  }

  protected def back(n: Int): Unit = {
    ptr -= n
    if (ptr < 0)
      throw new IllegalStateException("Pointer cannot be negative number")
  }

  protected def output = {
    write(memory(ptr))
  }

  protected def input = {
    memory(ptr) = read()
  }

  private def eval(code: List[Inst], innerBlock: Boolean): Unit = {
    for (c <- code) c match {
      case Inc(n)  => inc(n)
      case Dec(n)  => dec(n)
      case Fwd(n)  => foward(n)
      case Back(n) => back(n)
      case Write   => output
      case Read    => input
      case Block(insts) => {
        if (memory(ptr) != 0)
          eval(insts, true)
      }
      case _ => throw new IllegalArgumentException("Illegal token" + c)
    }
    if (memory(ptr) != 0 && innerBlock) eval(code, true)
  }

  private def evalRaw(code: List[Any], innerBlock: Boolean): Unit = {
    for (c <- code) c match {
      case '+' => inc(1)
      case '-' => dec(1)
      case '>' => foward(1)
      case '<' => back(1)
      case '.' => output
      case ',' => input
      case block: List[_] => {
        if (memory(ptr) != 0)
          evalRaw(block, true) // run block
      }
      case _ => throw new IllegalArgumentException("Illegal token" + c)
    }
    if (memory(ptr) != 0 && innerBlock) evalRaw(code, true) // retry
  }
}
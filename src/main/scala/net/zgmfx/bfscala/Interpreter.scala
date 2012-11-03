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
    if (inQueue.isEmpty && !waitInput) throw new IllegalStateException("Input is empty")
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
  val memory = new Array[Int](memsize);
  var ptr = 0
  def run(code: List[Any]): Unit = {
    evaluate(code, false)
  }

  private def evaluate(code: List[Any], innerBlock: Boolean): Unit = {
    for (c <- code) {
      c match {
        case '+' => memory(ptr) += 1
        case '-' => memory(ptr) -= 1
        case '>' => ptr += 1
        case '<' => ptr -= 1
        case '.' => write(memory(ptr))
        case ',' => memory(ptr) = read()
        case (x :: xs) => {
          if (memory(ptr) != 0)
            evaluate(x :: xs, true) // run block
        }
      }

    }
    if (memory(ptr) != 0 && innerBlock) evaluate(code, true) // retry
  }
}
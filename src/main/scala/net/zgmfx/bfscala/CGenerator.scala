package net.zgmfx.bfscala

import scala.sys.process._
import org.apache.commons.io.FileUtils
import java.io.File

class CGenerator(tempPath: String = "temp.c") {
  def compile(ast: Ast, outPath: String = "a.out") = {
    val cLangSource = generate(ast)
    FileUtils.write(new File(tempPath), cLangSource)
    val result = Seq("gcc", "-O3", "-Wall", "-o", outPath, tempPath).!
    if (result != 0) {
      throw new RuntimeException("Compile failed")
    }
  }

  def generate(ast: Ast): String = {
    val sb = new StringBuilder
    sb ++= templateBegin
    formatCode(sb, ast.code)
    sb ++= templateEnd
    sb.toString
  }

  def formatCode(sb: StringBuilder, code: List[Inst]): Unit = {
    for (c <- code) c match {
      case Inc(n)  => sb ++= templateInc.format(n)
      case Dec(n)  => sb ++= templateDec.format(n)
      case Fwd(n)  => sb ++= templateFwd.format(n)
      case Back(n) => sb ++= templateBack.format(n)
      case Write   => sb ++= templateWrite
      case Read    => sb ++= templateRead
      case Block(insts) => {
        sb ++= templateBlockBegin
        formatCode(sb, insts)
        sb ++= templateBlockEnd
      }
    }
  }

  // ---------------
  //    templates   
  // ---------------
  val templateBegin =
    """#include <stdio.h>
#include <stdlib.h>

#define INIT_MEMSIZE 1024

int ptr = 0;
int* memory = NULL;
int memsize = INIT_MEMSIZE;

void
ensure_memory(size_t size) {
    if(memory != NULL)
        free(memory);
    memory = (int*) calloc(sizeof(int), size);
}

void
check_memory(void){
    if (memsize <= ptr) {
        memsize *= 2;
        ensure_memory(memsize);
    }
}

void
check_ptr(void) {
    if(ptr < 0) {
        printf("RuntimeError: negative pointer\n");
        exit(EXIT_FAILURE);
    }
}

int
main(int argc, char** argv) {
    ensure_memory(INIT_MEMSIZE);
"""
  val templateEnd = """printf("\n");
    return 0;
    }"""
  val templateInc = "memory[ptr] += %d;\n"
  val templateDec = "memory[ptr] -= %d;\n"
  val templateFwd = "ptr += %d; check_memory();\n"
  val templateBack = "ptr -= %d; check_ptr();\n"
  val templateWrite = "putchar(memory[ptr]);\n"
  val templateRead = "memory[ptr] = getchar();\n"
  val templateBlockBegin = "while(memory[ptr]) {\n"
  val templateBlockEnd = "}\n"
}
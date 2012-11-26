package net.zgmfx.bfscala

import scala.sys.process._
import org.apache.commons.io.FileUtils
import java.io.File

class JSGenerator(tempPath: String = "temp.js") extends CGenerator {
  override def compile(ast: Ast, outPath: String = "a.out.js") = {
    val jsSource = generate(ast)
    FileUtils.write(new File(outPath), jsSource)
  }

  override val templateBegin =
    """sys = require('sys');
var inputBuffer = [];
process.stdin.resume();
process.stdin.on('data', function(chunk){
    for(var i = 0; i < chunk.length; i++){
        inputBuffer.push(chunk[i]);
    }
    console.log(inputBuffer);
});
var getchar = function(){
    return inputBuffer.shift();
};
var putchar = function(i) {
    sys.print(String.fromCharCode(i));
};
var ptr = 0;
var memory = [];
var expandMemory = function(size) {
    if (size >= memory.length) {
        for(var i = memory.length; i < size; i++){
            memory[i] = 0;
        } 
    }
};
expandMemory(1024);
var check_ptr = function() {
    if (ptr < 0) {
        throw "Invalid program! Negative pointer is not allowed!";
    }
}
var check_memory = function(){
    expandMemory(ptr);
};

"""
  override val templateEnd = "sys.print(\"\\n\");\nprocess.exit();\n";
  override val templateBlockBegin = "while(memory[ptr] != 0) {\n"
  override val templateBlockEnd = "}\n"
}
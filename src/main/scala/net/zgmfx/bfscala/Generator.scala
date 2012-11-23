package net.zgmfx.bfscala

import org.apache.bcel.generic.ClassGen
import org.apache.bcel.Constants
import org.apache.bcel.generic.MethodGen
import org.apache.bcel.generic.ArrayType
import org.apache.bcel.generic.Type
import org.apache.bcel.generic.InstructionList
import org.apache.bcel.generic.InstructionFactory
import org.apache.bcel.generic.ObjectType
import org.apache.bcel.generic.PUSH
import org.apache.bcel.generic.InstructionConstants

class Generator(code: List[Any]) {
  lazy val optimized = optimize(code)

  def optimize(cod: List[Any]) = {
    cod
  }
}

class ClassGenerator(name: String) {
  val classGen = new ClassGen(
    name,
    "java.lang.Object",
    "<generated",
    Constants.ACC_PUBLIC | Constants.ACC_SUPER,
    null)
  val mainInsts = new InstructionList
  val consts = classGen.getConstantPool
  val mainMeth = new MethodGen(
    Constants.ACC_STATIC | Constants.ACC_PUBLIC,
    Type.VOID,
    Array(new ArrayType(Type.STRING, 1)),
    Array("args"),
    "main",
    "HelloWorld",
    mainInsts,
    consts)
  val instFactory = new InstructionFactory(classGen)
  val printStreamObjectType = new ObjectType("java.io.PrintStream");
  
  def appendWrite(c: Char) {
    mainInsts.append(
      instFactory.createFieldAccess(
        "java.lang.System",
        "out",
        printStreamObjectType,
        Constants.GETSTATIC))
    mainInsts.append(new PUSH(consts, c))
    mainInsts.append(
      instFactory.createInvoke(
        "java.io.PrintStream",
        "print",
        Type.VOID,
        Array(Type.CHAR),
        Constants.INVOKEVIRTUAL))
     mainInsts.append(InstructionConstants.RETURN)
  }

}
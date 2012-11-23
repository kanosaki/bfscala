package net.zgmfx.bfscala

class ClassDef(cname: String, superName: String) {
  def defmethod(mname: String)(methBuilder: MethodDef => Unit) {
    val meth = new MethodDef(mname)
    methBuilder(meth)
  }
}

class MethodDef(name: String) {

}

class Bcel {
  def defclass(name: String, superName: String)(clsBuilder: ClassDef => Unit) = {
    val cls = new ClassDef(name, superName)
    
  }

  def writeTo(path: String) {

  }
}
package axirassa.util

import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream

trait AutoSerializingObject {
	def toBytes() = {
		val baos = new ByteArrayOutputStream
		val out = new ObjectOutputStream(baos)
		
		out.writeObject(this)
		out.close
		
		baos.toByteArray
	}
}

object AutoSerializingObject {
	def fromBytes(bytes: Array[Byte]) = { 
		val bais = new ByteArrayInputStream(bytes)
		val in = new ObjectInputStream(bais)
		
		try {
			in.readObject
		} finally {
			in.close
		}
	}
}
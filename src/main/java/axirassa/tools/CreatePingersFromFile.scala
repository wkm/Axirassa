
package axirassa.tools

import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader

import org.apache.tapestry5.ioc.annotations.Inject
import org.hibernate.Session

import axirassa.dao.PingerDAO
import axirassa.dao.UserDAO
import axirassa.ioc.IocStandalone
import axirassa.model.PingerEntity
import axirassa.model.UserEntity

object CreatePingersFromFile {
  	def main(args : Array[String]){
		val tool = IocStandalone.autobuild(classOf[CreatePingersFromFile])
		tool.insert()
	}
}

class CreatePingersFromFile {

	@Inject
	var database : Session = _ 

	@Inject
	var pingerDAO: PingerDAO = _ 

	@Inject
	var userDAO : UserDAO = _ 

	var filename: String = _

	var email : String = _ 


	private def insert()  {
		val br = new BufferedReader(new InputStreamReader(System.in))

		System.out.println("User's primary e-mail: ")
		email = br.readLine().trim()

		val user = userDAO.getUserByEmail(email)

		if (user.isEmpty) {
			System.err.println("No user with primary e-mail: " + email)
			return
		}

		System.out.println("CSV containing addresses: ")
		filename = br.readLine().trim()

		System.out.println("Creating pingers for user w/ ID: " + user.get.getId())

		val fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)))
		var line = null
		while ((line = fileReader.readLine()) != null) {
			if (line.contains(","))
				line = line.split(",", 2)(1).trim()

			line = "http://www." + line
			println("\t" + line)
			
			val pinger = new PingerEntity()
			pinger.setUrl(line)
			pinger.setUser(user.get)
			pinger.setFrequency(3600)

			database.save(pinger)
		}

		println("Commiting transaction")
		database.getTransaction().commit()
		println("Done.")
	}
}

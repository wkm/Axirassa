package test.scala

import scala.actors.Actor
import scala.actors.Actor._
import scala.actors.TIMEOUT
import org.junit.Test

object TestScalaChat extends App {
  override def main(args: Array[String]) {
    new TestScalaChat().testChatApp()
  }
}

class TestScalaChat {
  @Test
  def testChatApp() {
    println("Creating chat room")
    val chatRoom = new ChatRoom()

    println("Chat room started")
    chatRoom.start()

    chatRoom ! Subscribe(User("Bob"))
    chatRoom ! UserPost(User("Bob"), Post("Hi There!"))
    chatRoom ! UserPost(User("Bob"), Post("What is up?"))
    chatRoom ! Subscribe(User("Charles"))
    chatRoom ! UserPost(User("Charles"), Post("Hi everyone"))
  }
}

case class User(name: String)
case class Subscribe(user: User)
case class Unsubscribe(user: User)
case class Post(msg: String)
case class UserPost(user: User, post: Post)

class ChatRoom extends Actor {
  var session = Map.empty[User, Actor]

  def act() {
    while (true) {
      receive {
        case Subscribe(user) =>
          println("Received subscribe: ", user)

          val sessionUser = createSessionUser(user.name)
          session += user -> sessionUser
          reply("Subscribed " + user)

        case Unsubscribe(user) =>
          println("Unsubscribing: ", user)

        case UserPost(user, post) =>
          println("Received post: ", post)
          for (key <- session.keys; if key != user)
            session(key) ! post
      }
    }
  }

  private def createSessionUser(name : String) =
    actor {
      while (true) {
        self.receiveWithin(1800 * 1800) {
          case Post(msg) =>
            println("MSG ["+name+"]: "+ msg)

          case TIMEOUT =>
          //            room ! Unsubscribe(user)
        }
      }
    }
}
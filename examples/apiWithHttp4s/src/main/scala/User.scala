package example

import io.circe.{Decoder, Encoder, HCursor}
import io.circe.syntax.EncoderOps
import io.circe.Json

case class User(
  name: String
)

object User {
  implicit val decoder: Decoder[User] = (c: HCursor) => for {
    name <- c.get[String]("name")
  } yield {
    User(name)
  }

  implicit val encoder: Encoder[User] = (a: User) => Json.obj(
    ("name", a.name.asJson),
    ("other", s"hello there, ${a.name}!".asJson)
    )
}

package apiWithHttp4s

import org.http4s.dsl.io._
import org.http4s.blaze.server.BlazeServerBuilder
import cats.effect.{
  IOApp,
  IO,
  ExitCode,
}
import org.http4s.{
  HttpRoutes,
  Status,
  Response,
  Request,
  InvalidMessageBodyFailure,
  MalformedMessageBodyFailure,
}
import org.http4s.{
  EntityDecoder,
  EntityEncoder,
}
import org.http4s.circe.{
  jsonOf,
  jsonEncoderOf,
  accumulatingJsonOf,
}
import io.circe.syntax.EncoderOps
import com.typesafe.scalalogging.Logger

import example.User


trait customJsonErrorHandling {
  def formatJsonParseError(error: Throwable): String = {
    error.toString
  }
}

object HelloAPI
  extends customJsonErrorHandling {
  /*Create a logger for outputs
   */
  val logger = Logger("Main")

  /*Define decoder & encoder mappings for: IO <-> Object
   *  for the different objects that need to be processed for this API definition set.
   */
  implicit val userDecoder: EntityDecoder[IO, User] = jsonOf[IO, User]
  implicit val userEncoder: EntityEncoder[IO, User] = jsonEncoderOf[IO, User]

  /** Compose the routing of the `hello` endpoint
   * Valid input handling
   *  In the case that the passed in request was valid, this exposes the `usr` here as a `User` case class
   *  instance. This can now be leveraged for doing whatever is needed.
   * TODO: would it be good to have this instead do things after the response object is returned?
   *  Technically the only thing we need to have for the acceptance of this payload is validating that it
   *  was an acceptable instance of our case class.
   * Error handling
   *  There are a number of possible errors that may emerge that throw a different error type when encountered.
   *  In the case that the data payload was not valid (for example, not passing in a json object as we are expecting
   *    a json object by our use of the `jsonOf` above) -- we encounter a `MalformedMessageBodyFailure` object
   *  In the case that the data payload was invalid, but a valid json object -- we encounter a
   *    `InvalidMessageBodyFailure` object. In this case, we consider some environmental value to act as a switch
   *    for what to return to the requester.
   */
  val httpApp = HttpRoutes.of[IO] {
    case req@POST -> Root / "hello" => req.as[User].flatMap {
      usr => {
        doThing(usr)
        Ok(usr)
      }
    }.handleErrorWith {
      case InvalidMessageBodyFailure(_, Some(y)) => {
        sys.env.getOrElse("composeErrorMessage", "false").toBoolean match {
          case true => UnprocessableEntity(formatJsonParseError(y))
          case false => UnprocessableEntity()
        }
      }
      case MalformedMessageBodyFailure(_, _) => BadRequest("Malformed payload -- did you send a valid JSON object?")
      case _ => BadRequest()
    }
  }.orNotFound

  def doThing(usr: User): Unit = {
    logger.info(s"${usr.toString}; ${usr.asJson.noSpaces}")
  }
}


object Main
  extends IOApp {
  /*Create a logger for outputs
   */
  val logger = Logger("Main")

  /*Define the routes that will exist
   *  Combine the various sources of routes together.
   */
  val routes = {
    HelloAPI.httpApp
  }

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(routes)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}

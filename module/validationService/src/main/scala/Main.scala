import cats.effect._
import com.styleseat.dataEngTools.s3.s3Action
import com.styleseat.dataEngTools.s3.s3Uri

import java.io.File

//object Main extends IOApp.Simple {
//    val x = s3Uri("s3://styleseat-logs/events/production/dt=2022-08-25-15/202208251540_f62c_0.gz")
//    val y = s3Url("s3.console.aws.amazon.com/s3/object/styleseat-logs/events/production/dt=2022-08-25-15/202208251540_f62c_0.gz")
//    val z = s3File("styleseat-logs", "events/production/dt=2022-08-25-15/202208251540_f62c_0.gz")
//    val run = s3Action.downloadFile(x, new File(s"temp.${x.fileExtension}"))
//}


object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] =
    for {
      x <- s3Uri("s3://styleseat-logs/events/production/dt=2022-08-25-15/202208251540_f62c_0.gz")
      target = new File(s"temp.${x.fileExtension}")
      _ <- s3Action.downloadFile(x, target)
      _ <- IO.println("Done")
    } yield ExitCode.Success


//    val x = OriginalEventValidation.validateEvent(
//      """{"event": "thing", "time": "2022-05-23 16:30:06.718", "platform": "app", "cookie_id": "some_cookie", "session_id": "some_session", "log_level": "value", "context": {"app_version": "0.0", "userdata": {"user_id":null,"email":"","hashed_email":"","phone_number":"","first_name":"","last_name":"","username":"","city":"Lagos","region":"05","postal_code":"","country":"NG","latitude":6.4474,"longitude":3.3903}, "logged_in": false, "user_agent": "agent_string", "utm_campaign": "SEM", "utm_medium": "blog"}}""",
//      AppVersion(0, 0, 0)
//    )
//    println(x)
}

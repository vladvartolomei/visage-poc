package computerdatabase

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation {

  val httpConf = http
    .baseURL("http://localhost:9200") // Here is the root for all relative URLs
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val scn = scenario("Try a milion sources") // A scenario is a chain of requests and pauses
    .exec(http("search-request")
      .get("/all-vectors/_search")
      .header("Content-Type", "application/json")
      .body(ElFileBody("big-search.json")).asJSON
    )
    .pause(30)

  setUp(scn.inject(constantUsersPerSec(0.2) during(1000 seconds))).maxDuration(85 minutes).protocols(httpConf)
}

/*
 * Copyright (c) 2016 Brendan McAdams & Thomas Lockney
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package codes.bytes.quaich.api.http

import java.io.{InputStream, OutputStream}

import codes.bytes.quaich.api.LambdaContext
import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}
import org.apache.commons.io.IOUtils
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization._
import org.json4s.{NoTypeHints, _}

import scala.io.Source

trait HTTPApp extends RequestStreamHandler {

  def newHandler: HTTPHandler

  protected implicit val formats = Serialization.formats(NoTypeHints)

  final override def handleRequest(input: InputStream, output: OutputStream, context: Context): Unit = {

    val ctx = new LambdaContext(context)
    val log = ctx.log

    val inputString = Source.fromInputStream(input).mkString
    log.debug(s"Passed input is:\n${inputString}")
    val json = parse(inputString)

    val req = json.extract[LambdaHTTPRequest]

    // route
    log.debug(s"Input: ${pretty(render(json))}")

    val response = newHandler.routeRequest(req, ctx)

    try {
      IOUtils.write(write(response), output)
    } catch {
      case e: Exception =>
        log.error("Error while writing response\n" + e.getMessage)
        throw new IllegalStateException(e)
    }
  }
}

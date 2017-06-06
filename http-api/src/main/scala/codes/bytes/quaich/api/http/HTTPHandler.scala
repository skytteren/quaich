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

import codes.bytes.quaich.api.Logger
import codes.bytes.quaich.api.http.routing.{HTTPRoute, MethodRoute, PathRoutingResolver, RouteRequest}


trait HTTPHandler {

  protected val routeBuilder = Map.newBuilder[(HTTPMethod, String), HTTPRoute[_]]

  protected val pathResolver = new PathRoutingResolver


  lazy val routes = routeBuilder.result

  def routeRequest(request: LambdaHTTPRequest, context: LambdaContext): LambdaHTTPResponse = {
    handlerByResource(request)
      .orElse(handlerByPath(request)(context.log)) match {
      case Some(RouteRequest(handler, request)) => handler(LambdaRequestContext(request, context))
      case None ⇒ LambdaHTTPResponse(statusCode = 404)
    }
  }

  def addRoute(method: HTTPMethod, route: String, handler: HTTPRoute[_]): Unit = {
    routeBuilder += (method -> route) -> handler
    pathResolver.bindPath(route, MethodRoute(method.toString, handler))
  }

  private def handlerByResource(request: LambdaHTTPRequest): Option[RouteRequest[_]] = {
    routes
      .get(HTTPMethod(request.httpMethod) -> request.resource)
      .map { route =>
        RouteRequest(route, request)
      }
  }

  private def handlerByPath(request: LambdaHTTPRequest)(implicit log: Logger): Option[RouteRequest[_]] = {
    pathResolver.resolveRequestRoute(request)
  }
}
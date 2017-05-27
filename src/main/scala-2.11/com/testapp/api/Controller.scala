package com.testapp.api

import akka.http.scaladsl.server.Route

trait Controller {
  def routes: Route
}

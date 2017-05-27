package com.testapp.utils

import com.typesafe.config.{Config, ConfigFactory}

object Settings {

  val config: Config = ConfigFactory.load()

  val host: String = config.getString("calc.host")
  val port: Int = config.getInt("calc.port")

  val actorSystemName: String = config.getString("calc.actorSystemName")
}

package com.skyinu.gradlebutterknife.plugin.util

import org.gradle.api.Project
import org.gradle.api.logging.Logger;

/**
 * Created by chen on 2018/3/12.*/

public class Log {
  private static Logger logger

  static def init(Project project) {
    logger = project.logger
  }

  static def debug(String var1) {
    logger.debug(var1)
  }

  static def info(String var1) {
    logger.info(var1)

  }

  static def warn(String var1) {
    logger.warn(var1)
  }

  static def error(String var1) {
    logger.error(var1)

  }
}

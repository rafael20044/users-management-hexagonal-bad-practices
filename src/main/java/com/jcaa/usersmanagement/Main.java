package com.jcaa.usersmanagement;

import com.jcaa.usersmanagement.infrastructure.config.DependencyContainer;
import com.jcaa.usersmanagement.infrastructure.entrypoint.IApplicationRunner;
import java.util.logging.Logger;

public final class Main {

  private static final Logger log = Logger.getLogger(Main.class.getName());

  public static void main(final String[] args) {
    log.info("Starting Users Management System...");
    IApplicationRunner applicationRunner = buildContainer();
    applicationRunner.init();
  }

  private static DependencyContainer buildContainer() {
    return new DependencyContainer();
  }

}
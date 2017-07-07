package com.kindustry.framework.jobs;

import org.junit.Before;
import org.junit.Test;

import com.kindustry.framework.jobs.CronJob;

public class CronJobTest {

  CronJob cronJob;

  @Before
  public void setUp() {
    cronJob = new CronJob();
  }

  @Test
  public void testexportData() {
    cronJob.exportDataForIntegralMall();
//    cronJob.writeFile();
  }
}

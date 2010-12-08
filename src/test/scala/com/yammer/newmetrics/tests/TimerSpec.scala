package com.yammer.newmetrics.tests

import com.codahale.simplespec.Spec
import com.yammer.newmetrics.TimerMetric
import java.util.concurrent.TimeUnit

object TimerSpec extends Spec {
  class `A blank timer` {
    val timer = new TimerMetric

    def `should have a max of zero` {
      timer.max(TimeUnit.MILLISECONDS) must beCloseTo(0.0, 0.001)
    }

    def `should have a min of zero` {
      timer.min(TimeUnit.MILLISECONDS) must beCloseTo(0.0, 0.001)
    }

    def `should have a mean of zero` {
      timer.mean(TimeUnit.MILLISECONDS) must beCloseTo(0.0, 0.001)
    }

    def `should have a count of zero` {
      timer.count must beEqualTo(0)
    }

    def `should have a standard deviation of zero` {
      timer.stdDev(TimeUnit.MILLISECONDS) must beCloseTo(0.0, 0.001)
    }

    def `should have a median/p95/p98/p99/p999 of zero` {
      val Array(median, p95, p98, p99, p999) = timer.percentiles(TimeUnit.MILLISECONDS, 0.5, 0.95, 0.98, 0.99, 0.999)
      median must beCloseTo(0.0, 0.001)
      p95 must beCloseTo(0.0, 0.001)
      p98 must beCloseTo(0.0, 0.001)
      p99 must beCloseTo(0.0, 0.001)
      p999 must beCloseTo(0.0, 0.001)
    }

    def `should have a mean rate of zero` {
      timer.meanRate(TimeUnit.SECONDS) must beCloseTo(0.0, 0.001)
    }

    def `should have a one-minute rate of zero` {
      timer.oneMinuteRate(TimeUnit.SECONDS) must beCloseTo(0.0, 0.001)
    }

    def `should have a five-minute rate of zero` {
      timer.fiveMinuteRate(TimeUnit.SECONDS) must beCloseTo(0.0, 0.001)
    }

    def `should have a fifteen-minute rate of zero` {
      timer.fifteenMinuteRate(TimeUnit.SECONDS) must beCloseTo(0.0, 0.001)
    }
  }

  class `Timing a series of events` {
    val timer = new TimerMetric
    timer.update(10, TimeUnit.MILLISECONDS)
    timer.update(20, TimeUnit.MILLISECONDS)
    timer.update(20, TimeUnit.MILLISECONDS)
    timer.update(30, TimeUnit.MILLISECONDS)
    timer.update(40, TimeUnit.MILLISECONDS)

    def `should record the count` {
      timer.count must beEqualTo(5)
    }

    def `should calculate the minimum duration` {
      timer.min(TimeUnit.MILLISECONDS) must beCloseTo(10.0, 0.001)
    }

    def `should calclate the maximum duration` {
      timer.max(TimeUnit.MILLISECONDS) must beCloseTo(40.0, 0.001)
    }

    def `should calclate the mean duration` {
      timer.mean(TimeUnit.MILLISECONDS) must beCloseTo(24.0, 0.001)
    }

    def `should calclate the standard deviation` {
      timer.stdDev(TimeUnit.MILLISECONDS) must beCloseTo(11.401, 0.001)
    }

    def `should calculate the median/p95/p98/p99/p999` {
      val Array(median, p95, p98, p99, p999) = timer.percentiles(TimeUnit.MILLISECONDS, 0.5, 0.95, 0.98, 0.99, 0.999)
      median must beCloseTo(20.0, 0.001)
      p95 must beCloseTo(40.0, 0.001)
      p98 must beCloseTo(40.0, 0.001)
      p99 must beCloseTo(40.0, 0.001)
      p999 must beCloseTo(40.0, 0.001)
    }
  }

  class `Timing crazy-variant values` {
    val timer = new TimerMetric
    timer.update(Long.MaxValue, TimeUnit.NANOSECONDS)
    timer.update(0, TimeUnit.NANOSECONDS)

    def `should calculate the standard deviation without overflowing` {
      timer.stdDev(TimeUnit.NANOSECONDS) must beCloseTo(6.5219089126663916E18, 3)
    }
  }
}

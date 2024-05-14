
package com.zmonster.recyclebuy.utils;

public final class TimeSpanUnit {

      public static final TimeSpanUnit MILLISECONDS = new TimeSpanUnit("MILLISECONDS", TimeSpanConstants.MILLISECONDS);

      private final int value;
      private final String name;

      private TimeSpanUnit(String name, int value) {
          this.name = name;
          this.value = value;
      }

      public int getName() {
          return value;
      }

      public int getValue() {
          return value;
      }


      public int hashCode() {
          return value;
      }

      public String toString() {
          return name + ":" + value;
      }

      interface TimeSpanConstants {

          int MILLISECONDS = 1;

          int SECONDS = MILLISECONDS * 1000;

          int MINUTES = SECONDS * 60;

          int HOURS = MINUTES * 60;

          int DAYS = HOURS * 24;
      }

      public boolean equals(Object aObj) { //NOPMD
          return super.equals(aObj);
      }
}

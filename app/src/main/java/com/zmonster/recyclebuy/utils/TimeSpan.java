package com.zmonster.recyclebuy.utils;

import java.util.Date;


public class TimeSpan implements Comparable, java.io.Serializable, Cloneable {


    private long time = 0;


    public TimeSpan(long time) {
        this.time = time;
    }

    public static TimeSpan subtract(Date date1, Date date2) {
        return new TimeSpan(date1.getTime() - date2.getTime());
    }

    public long getDays() {
        return (((this.time / 1000) / 60) / 60) / 24;
    }
    public void add(TimeSpan timespan) {
        add(TimeSpanUnit.MILLISECONDS, timespan.time);
    }
    public void add(TimeSpanUnit unit, long value) {
        this.time += TimeSpan.toMilliseconds(unit, value);
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public int compareTo(Object o) {
        TimeSpan compare = (TimeSpan) o;
        if (this.time == compare.time) {
            return 0;
        }
        if (this.time > compare.time) {
            return +1;
        }
        return -1;
    }

    public boolean equals(Object obj) {
        if (obj instanceof TimeSpan) {
            TimeSpan compare = (TimeSpan) obj;
            if (this.time == compare.time) {
                return true;
            }
        }
        return false;
    }
    public int hashCode() {
        return Long.valueOf(this.time).hashCode();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        try {
            long millis = this.time;
            if (millis < 0) {
                sb.append('-');
                millis = -millis;
            }

            long day = millis / TimeSpanUnit.TimeSpanConstants.DAYS;

            if (day != 0) {
                sb.append(day);
                sb.append("d.");
                millis = millis % TimeSpanUnit.TimeSpanConstants.DAYS;
            }

            sb.append(millis / TimeSpanUnit.TimeSpanConstants.HOURS);
            millis = millis % TimeSpanUnit.TimeSpanConstants.HOURS;
            sb.append("h:");
            sb.append(leftPad(Long.toString(millis / TimeSpanUnit.TimeSpanConstants.MINUTES), 2, "0"));
            millis = millis % TimeSpanUnit.TimeSpanConstants.MINUTES;
            sb.append("m:");
            sb.append(leftPad(Long.toString(millis / TimeSpanUnit.TimeSpanConstants.SECONDS), 2, "0"));
            sb.append('s');
            millis = millis % TimeSpanUnit.TimeSpanConstants.SECONDS;
            if (millis != 0) {
                sb.append('.');
                sb.append(millis);
                sb.append("ms");
            }

        } catch (Exception e) {
        }

        return sb.toString();
    }

    private static long toMilliseconds(TimeSpanUnit unit, long value) {
        return value * unit.getValue();
    }
    public static void main(String[] args) {
    }

    public static String repeat(char ch, int repeat) {
        char[] buf = new char[repeat];
        for (int i = repeat - 1; i >= 0; i--) {
            buf[i] = ch;
        }
        return new String(buf);
    }

    public static String leftPad(String str, int size, char padChar) {
        if (str == null) {
            return null;
        }
        int pads = size - str.length();
        if (pads <= 0) {
            return str;
        }
        if (pads > 8192) {
            return leftPad(str, size, String.valueOf(padChar));
        }
        return repeat(padChar, pads).concat(str);
    }

    public static String leftPad(String str, int size, String padStr) {
        if (str == null) {
            return null;
        }
        if (isEmpty(padStr)) {
            padStr = " ";
        }
        int padLen = padStr.length();
        int strLen = str.length();
        int pads = size - strLen;
        if (pads <= 0) {
            return str;
        }
        if ((padLen == 1) && (pads <= 8192)) {
            return leftPad(str, size, padStr.charAt(0));
        }

        if (pads == padLen)
            return padStr.concat(str);
        if (pads < padLen) {
            return padStr.substring(0, pads).concat(str);
        }
        char[] padding = new char[pads];
        char[] padChars = padStr.toCharArray();
        for (int i = 0; i < pads; i++) {
            padding[i] = padChars[(i % padLen)];
        }
        return new String(padding).concat(str);
    }

    public static boolean isEmpty(CharSequence cs) {
        return (cs == null) || (cs.length() == 0);
    }
}

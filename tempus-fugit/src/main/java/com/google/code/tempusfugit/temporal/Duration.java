/*
 * Copyright (c) 2009-2010, tempus-fugit committers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.code.tempusfugit.temporal;

import java.util.concurrent.TimeUnit;

public class Duration {

    private final Long value;
    private final TimeUnit unit;

    private Duration(Long value, TimeUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    public static Duration seconds(long seconds) {
        validate(seconds, TimeUnit.SECONDS);
        return new Duration(seconds, TimeUnit.SECONDS);
    }

    public static Duration millis(long millis) {
        validate(millis, TimeUnit.MILLISECONDS);
        return new Duration(millis, TimeUnit.MILLISECONDS);
    }

    public static Duration minutes(long minutes) {
        validate(minutes,  TimeUnit.MINUTES);
        return new Duration(minutes, TimeUnit.MINUTES);
    }

    public static Duration hours(long hours) {
        validate(hours,  TimeUnit.HOURS);
        return new Duration(hours, TimeUnit.HOURS);
    }

    public static Duration days(long days) {
        validate(days,  TimeUnit.DAYS);
        return new Duration(days, TimeUnit.DAYS);
    }

    private static void validate(long value, TimeUnit unit) {
        Duration duration = new Duration(value, unit);
        if (duration.inMillis() == Long.MAX_VALUE)
            throw new IllegalArgumentException();
    }

    public long inMillis() {
        return unit.toMillis(value);
    }

    public long inSeconds() {
        return unit.toSeconds(value);
    }

    public long inMinutes() {
        return unit.toMinutes(value);
    }

    public long inHours() {
        return unit.toHours(value);
    }

    public long inDays() {
        return unit.toDays(value);
    }

    public Duration plus(Duration duration) {
        return millis(duration.inMillis() + this.inMillis());
    }

    public Duration minus(Duration duration) {
        return millis(this.inMillis() - duration.inMillis());
    }

    public Boolean greaterThan(Duration duration) {
        return this.inMillis() > duration.inMillis();
    }

    public Boolean lessThan(Duration duration) {
        return this.inMillis() < duration.inMillis();
    }

    @Override
    public int hashCode() {
        return new Long(unit.toMillis(value)).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o.getClass().getName().equals(Duration.class.getName())))
            return false;
        Duration other = (Duration) o;
        return other.unit.toMillis(other.value) == this.unit.toMillis(this.value);
    }

    public String toString() {
        return "Duration " + value + " " + unit.toString().toLowerCase();
    }
}

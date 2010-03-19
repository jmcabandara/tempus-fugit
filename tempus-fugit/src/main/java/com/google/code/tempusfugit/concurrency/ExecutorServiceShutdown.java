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

package com.google.code.tempusfugit.concurrency;

import com.google.code.tempusfugit.temporal.Conditions;
import com.google.code.tempusfugit.temporal.Duration;
import com.google.code.tempusfugit.temporal.Timeout;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;

import static com.google.code.tempusfugit.concurrency.ThreadUtils.resetInterruptFlagWhen;
import static com.google.code.tempusfugit.temporal.Timeout.*;
import static com.google.code.tempusfugit.temporal.Timeout.*;
import static com.google.code.tempusfugit.temporal.Timeout.*;
import static com.google.code.tempusfugit.temporal.WaitFor.waitOrTimeout;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public final class ExecutorServiceShutdown {

    private final ExecutorService executor;

    private ExecutorServiceShutdown(ExecutorService executor) {
        this.executor = executor;
    }

    public static ExecutorServiceShutdown shutdown(ExecutorService executor) {
        return new ExecutorServiceShutdown(executor);
    }

    public Boolean waitingForCompletion(final Duration duration) {
        if (executor == null)
            return false;
        executor.shutdown();
        return resetInterruptFlagWhen(awaitingTerminationIsInterrupted(duration));
    }

    /** @since 1.1 */
    public Boolean waitingForShutdown(Timeout timeout) throws TimeoutException, InterruptedException {
        if (executor == null)
            return false;
        executor.shutdownNow();
        waitOrTimeout(Conditions.shutdown(executor), timeout);
        return true;
    }

    @Deprecated
    public Boolean waitingForShutdown(Duration timeout) throws TimeoutException, InterruptedException {
        return waitingForShutdown(timeout(timeout));
    }

    private Interruptible<Boolean> awaitingTerminationIsInterrupted(final Duration timeout) {
        return new Interruptible<Boolean>(){
            public Boolean call() throws InterruptedException {
                return executor.awaitTermination(timeout.inMillis(), MILLISECONDS);
            }
        };
    }

}

/*
 * MIT License
 *
 * Copyright (c) 2024 Bram
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.deathmotion.foliascheduler.utils;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a wrapper around {@code BukkitTask} and Paper's {@code ScheduledTask}.
 * This class provides a unified interface for interacting with both Bukkit's task scheduler
 * and Paper's task scheduler.
 */
public class TaskWrapper {

    private BukkitTask bukkitTask;
    private ScheduledTask scheduledTask;

    /**
     * Constructs a new TaskWrapper around a BukkitTask.
     *
     * @param bukkitTask the BukkitTask to wrap
     */
    public TaskWrapper(@NotNull BukkitTask bukkitTask) {
        this.bukkitTask = bukkitTask;
    }

    /**
     * Constructs a new TaskWrapper around Paper's ScheduledTask.
     *
     * @param scheduledTask the ScheduledTask to wrap
     */
    public TaskWrapper(@NotNull ScheduledTask scheduledTask) {
        this.scheduledTask = scheduledTask;
    }

    /**
     * Retrieves the Plugin that owns this task.
     *
     * @return the owning {@link Plugin}
     */
    public Plugin getOwner() {
        return bukkitTask != null ? bukkitTask.getOwner() : scheduledTask.getOwningPlugin();
    }

    /**
     * Checks if the task is canceled.
     *
     * @return true if the task is canceled, false otherwise
     */
    public boolean isCancelled() {
        return bukkitTask != null ? bukkitTask.isCancelled() : scheduledTask.isCancelled();
    }

    /**
     * Cancels the task. If the task is running, it will be canceled.
     */
    public void cancel() {
        if (bukkitTask != null) {
            bukkitTask.cancel();
        } else {
            scheduledTask.cancel();
        }
    }
}
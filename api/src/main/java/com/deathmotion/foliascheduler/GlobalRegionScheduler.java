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

package com.deathmotion.foliascheduler;

import com.deathmotion.foliascheduler.utils.TaskWrapper;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Represents a scheduler for executing global region tasks.
 */
public final class GlobalRegionScheduler {

    private BukkitScheduler bukkitScheduler;
    private io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler globalRegionScheduler;

    GlobalRegionScheduler() {
        if (FoliaScheduler.isFolia()) {
            globalRegionScheduler = Bukkit.getGlobalRegionScheduler();
        } else {
            bukkitScheduler = Bukkit.getScheduler();
        }
    }

    /**
     * Schedules a task to be executed on the global region.
     *
     * @param plugin The plugin that owns the task
     * @param run    The task to execute
     */
    public void execute(@NotNull Plugin plugin, @NotNull Runnable run) {
        if (!FoliaScheduler.isFolia()) {
            bukkitScheduler.runTask(plugin, run);
            return;
        }

        globalRegionScheduler.execute(plugin, run);
    }

    /**
     * Schedules a task to be executed on the global region.
     *
     * @param plugin The plugin that owns the task
     * @param task   The task to execute
     * @return {@link TaskWrapper} instance representing a wrapped task
     */
    public TaskWrapper run(@NotNull Plugin plugin, @NotNull Consumer<Object> task) {
        if (!FoliaScheduler.isFolia()) {
            return new TaskWrapper(bukkitScheduler.runTask(plugin, () -> task.accept(null)));
        }

        return new TaskWrapper(globalRegionScheduler.run(plugin, (o) -> task.accept(null)));
    }

    /**
     * Schedules a task to be executed on the global region after the specified delay in ticks.
     *
     * @param plugin The plugin that owns the task
     * @param task   The task to execute
     * @param delay  The delay, in ticks before the method is invoked. Any value less-than 1 is treated as 1.
     * @return {@link TaskWrapper} instance representing a wrapped task
     */
    public TaskWrapper runDelayed(@NotNull Plugin plugin, @NotNull Consumer<Object> task, long delay) {
        if (delay < 1) delay = 1;

        if (!FoliaScheduler.isFolia()) {
            return new TaskWrapper(bukkitScheduler.runTaskLater(plugin, () -> task.accept(null), delay));
        }

        return new TaskWrapper(globalRegionScheduler.runDelayed(plugin, (o) -> task.accept(null), delay));
    }

    /**
     * Schedules a repeating task to be executed on the global region after the initial delay with the specified period.
     *
     * @param plugin            The plugin that owns the task
     * @param task              The task to execute
     * @param initialDelayTicks The initial delay, in ticks before the method is invoked. Any value less-than 1 is treated as 1.
     * @param periodTicks       The period, in ticks. Any value less-than 1 is treated as 1.
     * @return {@link TaskWrapper} instance representing a wrapped task
     */
    public TaskWrapper runAtFixedRate(@NotNull Plugin plugin, @NotNull Consumer<Object> task, long initialDelayTicks, long periodTicks) {
        if (initialDelayTicks < 1) initialDelayTicks = 1;
        if (periodTicks < 1) periodTicks = 1;

        if (!FoliaScheduler.isFolia()) {
            return new TaskWrapper(bukkitScheduler.runTaskTimer(plugin, () -> task.accept(null), initialDelayTicks, periodTicks));
        }

        return new TaskWrapper(globalRegionScheduler.runAtFixedRate(plugin, (o) -> task.accept(null), initialDelayTicks, periodTicks));
    }

    /**
     * Attempts to cancel all tasks scheduled by the specified plugin.
     *
     * @param plugin Specified plugin.
     */
    public void cancel(@NotNull Plugin plugin) {
        if (!FoliaScheduler.isFolia()) {
            Bukkit.getScheduler().cancelTasks(plugin);
            return;
        }

        globalRegionScheduler.cancelTasks(plugin);
    }
}
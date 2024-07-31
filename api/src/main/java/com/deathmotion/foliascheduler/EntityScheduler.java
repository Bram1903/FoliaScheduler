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
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Represents a scheduler for executing entity tasks.
 */
public class EntityScheduler {
    private BukkitScheduler bukkitScheduler;

    protected EntityScheduler() {
        if (!FoliaScheduler.isFolia()) {
            bukkitScheduler = Bukkit.getScheduler();
        }
    }

    /**
     * Schedules a task with the given delay. If the task failed to schedule because the scheduler is retired (entity removed), then returns false.
     * Otherwise, either the run callback will be invoked after the specified delay, or the retired callback will be invoked if the scheduler is retired.
     * Note that the retired callback is invoked in critical code, so it should not attempt to remove the entity,
     * remove other entities, load chunks, load worlds, modify ticket levels, etc.
     * <p>
     * It is guaranteed that the run and retired callback are invoked on the region which owns the entity.
     *
     * @param plugin  Plugin which owns the specified task.
     * @param run     The callback to run after the specified delay, may not be null.
     * @param retired Retire callback to run if the entity is retired before the run callback can be invoked, may be null.
     * @param delay   The delay in ticks before the run callback is invoked.
     */
    public void execute(@NotNull Entity entity, @NotNull Plugin plugin, @NotNull Runnable run, @Nullable Runnable retired, long delay) {
        if (!FoliaScheduler.isFolia()) {
            bukkitScheduler.runTaskLater(plugin, run, delay);
            return;
        }

        entity.getScheduler().execute(plugin, run, retired, delay);
    }

    /**
     * Schedules a task to execute on the next tick. If the task failed to schedule because the scheduler is retired (entity removed),
     * then returns null. Otherwise, either the task callback will be invoked after the specified delay,
     * or the retired callback will be invoked if the scheduler is retired.
     * Note that the retired callback is invoked in critical code, so it should not attempt to remove the entity,
     * remove other entities, load chunks, load worlds, modify ticket levels, etc.
     * <p>
     * It is guaranteed that the task and retired callback are invoked on the region which owns the entity.
     *
     * @param plugin  The plugin that owns the task
     * @param task    The task to execute
     * @param retired Retire callback to run if the entity is retired before the run callback can be invoked, may be null.
     * @return {@link TaskWrapper} instance representing a wrapped task
     */
    public TaskWrapper run(@NotNull Entity entity, @NotNull Plugin plugin, @NotNull Consumer<Object> task, @Nullable Runnable retired) {
        if (!FoliaScheduler.isFolia()) {
            return new TaskWrapper(bukkitScheduler.runTask(plugin, () -> task.accept(null)));
        }

        return new TaskWrapper(entity.getScheduler().run(plugin, (o) -> task.accept(null), retired));
    }

    /**
     * Schedules a task with the given delay. If the task failed to schedule because the scheduler is retired (entity removed),
     * then returns null. Otherwise, either the task callback will be invoked after the specified delay, or the retired callback will be invoked if the scheduler is retired.
     * Note that the retired callback is invoked in critical code, so it should not attempt to remove the entity,
     * remove other entities, load chunks, load worlds, modify ticket levels, etc.
     * <p>
     * It is guaranteed that the task and retired callback are invoked on the region which owns the entity.
     *
     * @param plugin     The plugin that owns the task
     * @param task       The task to execute
     * @param retired    Retire callback to run if the entity is retired before the run callback can be invoked, may be null.
     * @param delayTicks The delay in ticks before the run callback is invoked. Any value less-than 1 is treated as 1.
     * @return {@link TaskWrapper} instance representing a wrapped task
     */
    public TaskWrapper runDelayed(@NotNull Entity entity, @NotNull Plugin plugin, @NotNull Consumer<Object> task, @Nullable Runnable retired, long delayTicks) {
        if (delayTicks < 1) delayTicks = 1;

        if (!FoliaScheduler.isFolia()) {
            return new TaskWrapper(bukkitScheduler.runTaskLater(plugin, () -> task.accept(null), delayTicks));
        }

        return new TaskWrapper(entity.getScheduler().runDelayed(plugin, (o) -> task.accept(null), retired, delayTicks));
    }

    /**
     * Schedules a repeating task with the given delay and period. If the task failed to schedule because the scheduler is retired (entity removed),
     * then returns null. Otherwise, either the task callback will be invoked after the specified delay, or the retired callback will be invoked if the scheduler is retired.
     * Note that the retired callback is invoked in critical code, so it should not attempt to remove the entity,
     * remove other entities, load chunks, load worlds, modify ticket levels, etc.
     * <p>
     * It is guaranteed that the task and retired callback are invoked on the region which owns the entity.
     *
     * @param plugin            The plugin that owns the task
     * @param task              The task to execute
     * @param retired           Retire callback to run if the entity is retired before the run callback can be invoked, may be null.
     * @param initialDelayTicks The initial delay, in ticks before the method is invoked. Any value less-than 1 is treated as 1.
     * @param periodTicks       The period, in ticks. Any value less-than 1 is treated as 1.
     * @return {@link TaskWrapper} instance representing a wrapped task
     */
    public TaskWrapper runAtFixedRate(@NotNull Entity entity, @NotNull Plugin plugin, @NotNull Consumer<Object> task, @Nullable Runnable retired, long initialDelayTicks, long periodTicks) {
        if (initialDelayTicks < 1) initialDelayTicks = 1;
        if (periodTicks < 1) periodTicks = 1;

        if (!FoliaScheduler.isFolia()) {
            return new TaskWrapper(bukkitScheduler.runTaskTimer(plugin, () -> task.accept(null), initialDelayTicks, periodTicks));
        }

        return new TaskWrapper(entity.getScheduler().runAtFixedRate(plugin, (o) -> task.accept(null), retired, initialDelayTicks, periodTicks));
    }
}
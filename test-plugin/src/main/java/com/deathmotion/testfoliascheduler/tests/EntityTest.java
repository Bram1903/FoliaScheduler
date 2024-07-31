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

package com.deathmotion.testfoliascheduler.tests;

import com.deathmotion.foliascheduler.EntityScheduler;
import com.deathmotion.foliascheduler.FoliaScheduler;
import com.deathmotion.foliascheduler.utils.TaskWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Skeleton;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class EntityTest {

    private final String baseMessage = "[EntityScheduler] ";

    private final JavaPlugin plugin;
    private final EntityScheduler entityScheduler;

    public EntityTest(JavaPlugin plugin) {
        this.plugin = plugin;
        entityScheduler = FoliaScheduler.getEntityScheduler();

        init();
    }

    private void init() {
        List<World> worlds = Bukkit.getWorlds();

        if (worlds.isEmpty()) {
            plugin.getLogger().severe(baseMessage + "No worlds found to spawn an entity!");
            return;
        }

        World world = worlds.get(0);
        Location location = new Location(world, 0, 0, 0);

        Entity skeleton = world.spawn(location, Skeleton.class);

        entityScheduler.execute(skeleton, plugin, () -> {
            plugin.getLogger().info(baseMessage + "runNow");
        }, null, 0);

        entityScheduler.run(skeleton, plugin, (o) -> {
            plugin.getLogger().info(baseMessage + "runDelayed");
        }, null);

        entityScheduler.runDelayed(skeleton, plugin, (o) -> {
            plugin.getLogger().info(baseMessage + "runDelayed");
        }, null, 40);

        TaskWrapper runAtFixedRateTask = entityScheduler.runAtFixedRate(skeleton, plugin, (o) -> {
            plugin.getLogger().info(baseMessage + "runAtFixedRate");
        }, null, 40, 40);

        entityScheduler.runDelayed(skeleton, plugin, (o) -> {
            runAtFixedRateTask.cancel();
            plugin.getLogger().info(baseMessage + "runAtFixedRateTask canceled");
        }, null, 60);
    }
}

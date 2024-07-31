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

import com.deathmotion.foliascheduler.FoliaScheduler;
import com.deathmotion.foliascheduler.internal.schedulers.RegionScheduler;
import com.deathmotion.foliascheduler.utils.TaskWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class RegionTest {

    private final String baseMessage = "[RegionScheduler] ";

    private final JavaPlugin plugin;
    private final RegionScheduler regionScheduler;

    public RegionTest(JavaPlugin plugin) {
        this.plugin = plugin;
        regionScheduler = FoliaScheduler.getRegionScheduler();

        init();
    }

    private void init() {
        List<World> worlds = Bukkit.getWorlds();

        if (worlds.isEmpty()) {
            plugin.getLogger().severe(baseMessage + "No worlds found to test regions!");
            return;
        }

        World world = worlds.get(0);
        Location location = new Location(world, 0, 0, 0);

        regionScheduler.execute(plugin, world, 0, 0, () -> {
            plugin.getLogger().info(baseMessage + "execute on chunk");
        });

        regionScheduler.execute(plugin, location, () -> {
            plugin.getLogger().info(baseMessage + "execute on location");
        });

        regionScheduler.run(plugin, world, 0, 0, (o) -> {
            plugin.getLogger().info(baseMessage + "run on chunk");
        });

        regionScheduler.run(plugin, location, (o) -> {
            plugin.getLogger().info(baseMessage + "run on location");
        });

        regionScheduler.runDelayed(plugin, world, 0, 0, (o) -> {
            plugin.getLogger().info(baseMessage + "runDelayed on chunk");
        }, 40);

        regionScheduler.runDelayed(plugin, location, (o) -> {
            plugin.getLogger().info(baseMessage + "runDelayed on location");
        }, 40);

        TaskWrapper chunkRunAtFixedRateTask = regionScheduler.runAtFixedRate(plugin, world, 0, 0, (o) -> {
            plugin.getLogger().info(baseMessage + "runAtFixedRate on chunk");
        }, 40, 40);

        TaskWrapper locationRunAtFixedRateTask = regionScheduler.runAtFixedRate(plugin, location, (o) -> {
            plugin.getLogger().info(baseMessage + "runAtFixedRate on location");
        }, 40, 40);

        regionScheduler.runDelayed(plugin, world, 0, 0, (o) -> {
            chunkRunAtFixedRateTask.cancel();
            plugin.getLogger().info(baseMessage + "runAtFixedRateTask canceled on chunk");
        }, 60);

        regionScheduler.runDelayed(plugin, location, (o) -> {
            locationRunAtFixedRateTask.cancel();
            plugin.getLogger().info(baseMessage + "runAtFixedRateTask canceled on location");
        }, 60);
    }
}

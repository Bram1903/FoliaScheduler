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
import com.deathmotion.foliascheduler.GlobalRegionScheduler;
import com.deathmotion.foliascheduler.utils.TaskWrapper;
import org.bukkit.plugin.java.JavaPlugin;

public class GlobalRegionTest {

    private final String baseMessage = "[GlobalRegionScheduler] ";

    private final JavaPlugin plugin;
    private final GlobalRegionScheduler globalRegionScheduler;

    public GlobalRegionTest(JavaPlugin plugin) {
        this.plugin = plugin;
        globalRegionScheduler = FoliaScheduler.getGlobalRegionScheduler();

        init();
    }

    private void init() {
        globalRegionScheduler.execute(plugin, () -> {
            plugin.getLogger().info(baseMessage + "execute");
        });

        globalRegionScheduler.runDelayed(plugin, (o) -> {
            plugin.getLogger().info(baseMessage + "runDelayed");
        }, 40);

        TaskWrapper runAtFixedRateTask = globalRegionScheduler.runAtFixedRate(plugin, (o) -> {
            plugin.getLogger().info(baseMessage + "runAtFixedRate");
        }, 40, 40);

        globalRegionScheduler.runDelayed(plugin, (o) -> {
            runAtFixedRateTask.cancel();
            plugin.getLogger().info(baseMessage + "runAtFixedRateTask canceled");
        }, 60);
    }
}

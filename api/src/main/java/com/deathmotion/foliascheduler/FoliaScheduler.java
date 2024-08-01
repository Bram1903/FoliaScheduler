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

import com.deathmotion.foliascheduler.internal.FSVersion;
import com.deathmotion.foliascheduler.internal.FSVersions;
import com.deathmotion.foliascheduler.internal.checks.RelocateCheck;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public class FoliaScheduler {
    private static final boolean isFolia;

    @Getter
    private static final AsyncScheduler asyncScheduler;
    @Getter
    private static final EntityScheduler entityScheduler;
    @Getter
    private static final GlobalRegionScheduler globalRegionScheduler;
    @Getter
    private static final RegionScheduler regionScheduler;

    private static Class<? extends Event> regionizedServerInitEventClass;

    static {
        new RelocateCheck();

        boolean folia;
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            folia = true;

            // Thanks for this code ViaVersion
            // The class is only part of the Folia API, so we need to use reflections to get it
            regionizedServerInitEventClass = (Class<? extends Event>) Class.forName("io.papermc.paper.threadedregions.RegionizedServerInitEvent");
        } catch (ClassNotFoundException e) {
            folia = false;
        }

        isFolia = folia;

        asyncScheduler = new AsyncScheduler();
        entityScheduler = new EntityScheduler();
        globalRegionScheduler = new GlobalRegionScheduler();
        regionScheduler = new RegionScheduler();
    }

    /**
     * @return Whether the server is running Folia
     */
    public static boolean isFolia() {
        return isFolia;
    }

    public static FSVersion getVersion() {
        return FSVersions.CURRENT;
    }

    public static String getRawVersion() {
        return FSVersions.RAW;
    }

    /**
     * Run a task after the server has finished initializing.
     * Undefined behavior if called after the server has finished initializing.
     * <p>
     * We still need to use reflections to get the server init event class, as this is only part of the Folia API.
     *
     * @param plugin Your plugin or PacketEvents
     * @param run    The task to run
     */
    public static void runTaskOnInit(Plugin plugin, Runnable run) {
        if (!isFolia) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, run);
            return;
        }

        Bukkit.getServer().getPluginManager().registerEvent(regionizedServerInitEventClass, new Listener() {
        }, EventPriority.HIGHEST, (listener, event) -> run.run(), plugin);
    }
}
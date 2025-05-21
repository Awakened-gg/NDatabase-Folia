package com.nivixx.ndatabase.platforms.bukkitplatform;

import com.nivixx.ndatabase.platforms.coreplatform.executor.SyncExecutor;
import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.wrapper.task.WrappedTask;

import java.util.function.Consumer;

public class BukkitSyncExecutor implements SyncExecutor {
    
    private final NDatabasePlugin plugin;
    private final FoliaLib foliaLib;
    
    public BukkitSyncExecutor(NDatabasePlugin plugin) {
        this.plugin = plugin;
        this.foliaLib = new FoliaLib(plugin);
    }
    
    @Override
    public void runSync(Runnable runnable) {
        try {
            // FoliaLib handles both Bukkit and Folia platforms automatically
            // Using runNextTick with the correct Consumer<WrappedTask> signature
            foliaLib.getScheduler().runNextTick(task -> {
                try {
                    runnable.run();
                } catch (Exception e) {
                    plugin.getLogger().warning("Error while executing sync task: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            plugin.getLogger().warning("Error scheduling sync task: " + e.getMessage());
            // Attempt to run directly if scheduling fails
            try {
                runnable.run();
            } catch (Exception ex) {
                plugin.getLogger().severe("Failed to execute sync task directly: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}

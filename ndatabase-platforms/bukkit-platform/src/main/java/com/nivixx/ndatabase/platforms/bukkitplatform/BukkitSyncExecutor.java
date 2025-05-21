package com.nivixx.ndatabase.platforms.bukkitplatform;

import com.nivixx.ndatabase.platforms.coreplatform.executor.SyncExecutor;
import org.bukkit.Bukkit;

import java.lang.reflect.Method;

public class BukkitSyncExecutor implements SyncExecutor {
    
    private final NDatabasePlugin plugin;
    private final boolean isFolia;
    private Object globalRegionScheduler;
    
    public BukkitSyncExecutor(NDatabasePlugin plugin) {
        this.plugin = plugin;
        this.isFolia = checkFolia();
        
        if (isFolia) {
            try {
                Method getGlobalRegionScheduler = plugin.getServer().getClass().getMethod("getGlobalRegionScheduler");
                this.globalRegionScheduler = getGlobalRegionScheduler.invoke(plugin.getServer());
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to get global region scheduler, falling back to Bukkit scheduler");
                this.globalRegionScheduler = null;
            }
        }
    }
    
    private boolean checkFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
    
    @Override
    public void runSync(Runnable runnable) {
        if (isFolia && globalRegionScheduler != null) {
            try {
                // Get the execute method: void execute(Plugin plugin, Runnable runnable)
                Method executeMethod = globalRegionScheduler.getClass().getMethod("execute", plugin.getClass().getInterfaces()[0], Runnable.class);
                executeMethod.invoke(globalRegionScheduler, plugin, runnable);
            } catch (Exception e) {
                // Fallback to Bukkit scheduler if reflection fails
                plugin.getLogger().warning("Failed to use Folia scheduler, falling back to Bukkit scheduler");
                Bukkit.getScheduler().runTask(plugin, runnable);
            }
        } else {
            // Standard Bukkit/Paper implementation
            Bukkit.getScheduler().runTask(plugin, runnable);
        }
    }
}

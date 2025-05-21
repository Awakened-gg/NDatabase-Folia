package com.nivixx.ndatabase.platforms.bukkitplatform;

import com.nivixx.ndatabase.api.model.NEntity;
import com.nivixx.ndatabase.platforms.coreplatform.logging.DBLogger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BukkitDBLogger extends DBLogger {

    private final Logger logger;

    public BukkitDBLogger(boolean isDebugMode) {
        super(isDebugMode);
        this.logger = NDatabasePlugin.getInstance().getLogger();
    }

    @Override
    public Consumer<Supplier<String>> consumeDebugMessage() {
        if(!isDebugMode) return (msg) -> {};
        return (msg) -> logger.info("NDatabase-debug: " + msg.get());
    }

    @Override
    public Consumer<Supplier<String>> consumeInfoMessage() {
        return (msg) -> logger.info(msg.get());
    }

    @Override
    public BiConsumer<Throwable, String> consumeErrorMessage() {
        return (throwable, msg) -> logger.log(Level.SEVERE, msg, throwable);
    }

    @Override
    public Consumer<Supplier<String>> consumeWarnMessage() {
        return (msg) -> logger.warning(msg.get());
    }
}

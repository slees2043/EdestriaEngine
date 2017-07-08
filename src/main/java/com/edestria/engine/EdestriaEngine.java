package com.edestria.engine;

import com.edestria.engine.database.mongo.connection.MongoConnection;
import com.edestria.engine.database.mongo.files.EngineFiles;
import com.edestria.engine.database.mongo.services.MongoInsertionService;
import com.edestria.engine.logging.EngineLogger;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class EdestriaEngine extends JavaPlugin {

    /*
    * Mongo Services
    * */
    @Getter private MongoConnection mongoConnection;
    @Getter private MongoInsertionService mongoInsertionService;

    /*
    * Logging
    * */
    @Getter private EngineLogger engineLogger;

    /*
    * Files
    * */
    @Getter private EngineFiles engineFiles;

    @Override
    public void onEnable() {
        this.reigsterConnections();
        this.registerServices();
        this.engineLogger = new EngineLogger(this);
    }

    @Override
    public void onDisable() {
        this.mongoInsertionService.purgeExecutions();
        this.mongoConnection.disconnect();
    }

    private void reigsterConnections() {
        this.mongoConnection =
                MongoConnection.builder()
                        .host(this.engineFiles.getStringProperty("settings", "host"))
                        .port(this.engineFiles.getIntegerProperty("settings", "port"))
                        .collections(this.engineFiles.getStringListProperty("settings", "collections"))
                        .edestriaEngine(this)
                        .build();
        this.mongoConnection.connect();
    }

    private void registerServices() {
        this.mongoInsertionService = new MongoInsertionService(this);
    }

    private void registerFiles() {
        this.engineFiles = new EngineFiles(this);
    }
}

/**
 * MongoConfiguration for MongoDb session
 *
 * @author  Lin Zhai
 * @version 0.1
 */
package com.team4.uberapp;

import org.mongolink.*;
import org.mongolink.domain.mapper.ContextBuilder;

public class MongoConfiguration {

    public static void stop() {
        Singleton.INSTANCE.mongoSessionManager.close();
    }

    public static MongoSession createSession() {
        return Singleton.INSTANCE.mongoSessionManager.createSession();
    }

    private enum Singleton {

        INSTANCE;

        private Singleton() {
            ContextBuilder builder = new ContextBuilder("com.team4.uberapp.persistence.mapping");
            mongoSessionManager = MongoSessionManager.create(builder, new Properties().addSettings(Settings.defaultInstance()));
        }

        private final MongoSessionManager mongoSessionManager;
    }
}

package com.mangcoding;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class App implements QuarkusApplication {
    public static void main(String... args) {
        Quarkus.run(App.class, args);
    }

    @Override
    public int run(String... args) throws Exception {
        System.out.println("Starting Simple Store App...");
        Quarkus.waitForExit();
        return 0;
    }
}

package org.dojo.livingdoc.application;

public class Configuration {

    public final String version;
    public final boolean verbose;

    public Configuration() {
        this("5.2", false);
    }

    public Configuration(String version, boolean verbose) {
        this.version = version;
        this.verbose = verbose;
    }

    public String getVersion() {
        return version;
    }

    public boolean isVerbose() {
        return verbose;
    }
}

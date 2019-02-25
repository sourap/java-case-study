package com.trivago.mp.casestudy.model;

/**
 * Identiry class which is extended by all the Models that require ID to be used for Identity.
 */
public abstract class Identity {
    private final int id;

    public Identity(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

package com.evandev.redomesticate.api;

public enum PetCommand {
    WANDER(0),
    SIT(1),
    FOLLOW(2);

    private final int id;

    PetCommand(int id) {
        this.id = id;
    }

    public static PetCommand fromId(int id) {
        for (PetCommand command : values()) {
            if (command.id == id) return command;
        }
        return WANDER;
    }

    public int getId() {
        return this.id;
    }

    public PetCommand next() {
        return fromId((this.id + 1) % 3);
    }
}
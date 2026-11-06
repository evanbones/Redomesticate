package com.evandev.redomesticate.platform;

import com.evandev.redomesticate.platform.services.IPlatformHelper;

public class Services {
    //? if fabric {
    public static final IPlatformHelper PLATFORM = new com.evandev.redomesticate.fabric.platform.FabricPlatformHelper();
    //?} else {
    /*public static final IPlatformHelper PLATFORM = new com.evandev.redomesticate.neoforge.platform.NeoForgePlatformHelper();
    *///?}
}

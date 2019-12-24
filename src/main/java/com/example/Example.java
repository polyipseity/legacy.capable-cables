package com.example;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

// TODO: Write your mod.
@Mod(modid = Example.MOD_ID, useMetadata = true, canBeDeactivated = true, acceptedMinecraftVersions = "${minecraftVersionRange}", certificateFingerprint = "${certificateFingerprint}")
public class Example {
    public static final String MOD_ID = "${modid}";

    @Mod.EventHandler
    public void preInitialize(FMLPreInitializationEvent event) {
        System.out.println("Hello, world!");
    }
}

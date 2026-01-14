package com.ssdsaad.rcdiamondgh.rcseffect;

import java.util.*;

import net.minecraft.item.*;

import com.ssdsaad.rcdiamondgh.rcseffect.common.*;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.*;

@Mod(
    modid = "rcseffect",
    name = "RCsEffect",
    version = "NEO-Special-Fix",
    acceptedMinecraftVersions = "1.7.10",
    dependencies = "after:Baubles")
public class MainClass {

    public static final String MODID = "rcseffect";
    public static final String NAME = "RCsEffect";
    public static final String VERSION = "NEO-Special-Fix";
    public static boolean hasBaubles;
    public static Map<String, Item> rceItems;
    public static final boolean DEBUG = false;
    public static final boolean ALLOWCREATEITEM = true;
    public static final boolean CHEATER_FUCKER = false;
    public static final int ACCURACY = 60;
    @SidedProxy(
        clientSide = "com.ssdsaad.rcdiamondgh.rcseffect.client.ClientProxy",
        serverSide = "com.ssdsaad.rcdiamondgh.rcseffect.common.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        if (Loader.isModLoaded("Baubles")) {
            MainClass.hasBaubles = true;
        }
        MainClass.proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        MainClass.proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        MainClass.proxy.postInit(event);
    }

    @Mod.EventHandler
    public void serverStarting(final FMLServerStartingEvent event) {
        MainClass.proxy.serverStarting(event);
    }

    static {
        MainClass.hasBaubles = false;
        MainClass.rceItems = new HashMap<String, Item>();
    }
}

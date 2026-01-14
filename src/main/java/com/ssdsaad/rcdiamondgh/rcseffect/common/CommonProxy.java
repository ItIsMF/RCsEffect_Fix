package com.ssdsaad.rcdiamondgh.rcseffect.common;

import com.ssdsaad.rcdiamondgh.rcseffect.command.*;
import com.ssdsaad.rcdiamondgh.rcseffect.event.*;

import cpw.mods.fml.common.event.*;

public class CommonProxy {

    public void preInit(final FMLPreInitializationEvent event) {
        new ConfigLoader(event);
    }

    public void init(final FMLInitializationEvent event) {
        new EventBase();
        new SpecialEvent();
    }

    public void postInit(final FMLPostInitializationEvent event) {}

    public void serverStarting(final FMLServerStartingEvent event) {
        new CommandLoader(event);
    }
}

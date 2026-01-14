package com.ssdsaad.rcdiamondgh.rcseffect.command;

import net.minecraft.command.*;

import cpw.mods.fml.common.event.*;

public class CommandLoader {

    public CommandLoader(final FMLServerStartingEvent event) {
        event.registerServerCommand((ICommand) new CommandConTell());
        event.registerServerCommand((ICommand) new CommandPlaySound());
    }
}

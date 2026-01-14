package com.ssdsaad.rcdiamondgh.rcseffect.command;

import net.minecraft.command.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;

public class CommandPlaySound extends CommandBase {

    public String getCommandName() {
        return "soundplayer";
    }

    public String getCommandUsage(final ICommandSender p_71518_1_) {
        return "commands.soundplayer.usage";
    }

    public void processCommand(final ICommandSender sender, final String[] coms) {
        if (coms.length != 4) {
            throw new WrongUsageException("commands.soundplayer.usage", new Object[0]);
        }
        final EntityPlayerMP player = CommandBase.getPlayer(sender, coms[0]);
        try {
            player.worldObj
                .playSoundAtEntity((Entity) player, coms[1], Float.parseFloat(coms[2]), Float.parseFloat(coms[3]));
        } catch (Exception e) {
            throw new WrongUsageException("commands.soundplayer.usage", new Object[0]);
        }
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }
}

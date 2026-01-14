package com.ssdsaad.rcdiamondgh.rcseffect.command;

import net.minecraft.command.*;
import net.minecraft.entity.player.*;
import net.minecraft.server.*;
import net.minecraft.util.*;

public class CommandConTell extends CommandBase {

    public String getCommandName() {
        return "contell";
    }

    public String getCommandUsage(final ICommandSender p_71518_1_) {
        return "commands.contell.usage";
    }

    public void processCommand(final ICommandSender sender, final String[] coms) {
        if (coms.length == 0) {
            throw new WrongUsageException("commands.contell.usage", new Object[0]);
        }
        final EntityPlayerMP players = CommandBase.getPlayer(sender, coms[0]);
        final String[] temps = coms;
        if (coms.length % 2 != 0) {
            throw new WrongUsageException("commands.contell.usage", new Object[0]);
        }
        final String[] words = new String[coms.length / 2];
        final int[] de = new int[(coms.length - 2) / 2];
        if (temps.length == 1) {
            throw new WrongUsageException("commands.contell.usage", new Object[0]);
        }
        int a = 2;
        int b = 0;
        int c = 1;
        words[0] = coms[1];
        words[0] = words[0].replace('|', ' ');
        while (a < coms.length) {
            words[c] = coms[a + 1];
            words[c] = words[c].replace('|', ' ');
            try {
                de[b] = Integer.parseInt(coms[a]);
            } catch (Exception e) {
                throw new WrongUsageException("commands.contell.usage", new Object[0]);
            }
            ++b;
            ++c;
            a += 2;
        }
        if (new StringBuffer(words[0]).indexOf("/") == 0) {
            final MinecraftServer server = MinecraftServer.getServer();
            if (server != null) {
                server.getCommandManager()
                    .executeCommand(sender, words[0]);
            }
        } else {
            players.addChatMessage((IChatComponent) new ChatComponentText(words[0]));
        }
        new Thread(new ConTellHelper(players, words, de, sender)).start();
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }
}

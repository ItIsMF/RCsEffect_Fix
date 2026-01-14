package com.ssdsaad.rcdiamondgh.rcseffect.command;

import net.minecraft.command.*;
import net.minecraft.entity.player.*;
import net.minecraft.server.*;
import net.minecraft.util.*;

class ConTellHelper implements Runnable {

    EntityPlayerMP players;
    String[] words;
    int[] deley;
    ICommandSender sender;

    public ConTellHelper(final EntityPlayerMP players, final String[] words, final int[] deley,
        final ICommandSender sender) {
        this.players = players;
        this.words = words;
        this.deley = deley;
        this.sender = sender;
    }

    @Override
    public void run() {
        for (int a = 1; a < this.words.length; ++a) {
            try {
                Thread.sleep(this.deley[a - 1]);
                if (new StringBuffer(this.words[a]).indexOf("/") == 0) {
                    final MinecraftServer server = MinecraftServer.getServer();
                    if (server != null) {
                        server.getCommandManager()
                            .executeCommand(this.sender, this.words[a]);
                    }
                } else {
                    this.players.addChatMessage((IChatComponent) new ChatComponentText(this.words[a]));
                }
            } catch (Exception ex) {}
        }
    }
}

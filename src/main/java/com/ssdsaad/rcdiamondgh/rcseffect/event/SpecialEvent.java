package com.ssdsaad.rcdiamondgh.rcseffect.event;

import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraftforge.common.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;

import com.rcdiamondgh.utils.*;

import cpw.mods.fml.client.*;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.eventhandler.*;
import cpw.mods.fml.common.gameevent.*;

public class SpecialEvent {

    public SpecialEvent() {
        MinecraftForge.EVENT_BUS.register((Object) this);
        FMLCommonHandler.instance()
            .bus()
            .register((Object) this);
    }

    @SubscribeEvent
    public void repairXP(final PlayerPickupXpEvent event) {
        final EntityPlayer player = event.entityPlayer;
        final ItemStack[] array;
        final ItemStack[] items = array = new ItemStack[] { player.getEquipmentInSlot(0), player.getEquipmentInSlot(1),
            player.getEquipmentInSlot(2), player.getEquipmentInSlot(3), player.getEquipmentInSlot(4) };
        for (final ItemStack i : array) {
            if (i != null && i.hasTagCompound()
                && i.getTagCompound()
                    .hasKey("XPrepair")) {
                final int a = i.getTagCompound()
                    .getInteger("XPrepair");
                final int b = event.orb.getXpValue();
                final int c = b / a;
                i.setItemDamage(i.getItemDamage() - c);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent e) {
        if (e.phase == TickEvent.Phase.END && e.player != null && e.player.swingProgressInt == 1) {
            final ItemStack itemstack = e.player.getCurrentEquippedItem();
            if (itemstack != null && itemstack.hasTagCompound()) {
                final NBTTagCompound nbt = itemstack.getTagCompound();
                if (nbt.hasKey("AttackRange")) {
                    final float a = nbt.getFloat("AttackRange");
                    final MovingObjectPosition mov = Tools.getPosition(0.0f, a);
                    if (mov != null && mov.entityHit != null
                        && mov.entityHit != e.player
                        && mov.entityHit.hurtResistantTime == 0) {
                        FMLClientHandler.instance()
                            .getClient().playerController.attackEntity(e.player, mov.entityHit);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void bloodPoolAttacker(final LivingHurtEvent event) {
        final EntityLivingBase attacker = (EntityLivingBase) event.source.getEntity();
        if (attacker != null && attacker.getHeldItem() != null) {
            final ItemStack item = attacker.getHeldItem();
            if (item.hasTagCompound()) {
                final NBTTagCompound nbt = item.getTagCompound();
                if (nbt.hasKey("EnableBloodPool")) {
                    final int[] bp = nbt.getIntArray("EnableBloodPool");
                    if (bp.length != 5) {
                        return;
                    }
                    if (!nbt.hasKey("BloodPool")) {
                        nbt.setDouble("BloodPool", 0.0);
                    }
                    if (Tools.percenter(bp[2])) {
                        final float upper = event.ammount / 100.0f * bp[0];
                        if ((float) bp[1] - nbt.getDouble("BloodPool") <= upper) {
                            nbt.setDouble("BloodPool", (double) (float) bp[1]);
                        } else {
                            nbt.setDouble("BloodPool", nbt.getDouble("BloodPool") + upper);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void bloodPoolWound(final LivingHurtEvent event) {
        final EntityLivingBase wounded = event.entityLiving;
        if (wounded.getHeldItem() != null) {
            final ItemStack item = wounded.getHeldItem();
            if (item.hasTagCompound()) {
                final NBTTagCompound nbt = item.getTagCompound();
                if (nbt.hasKey("EnableBloodPool")) {
                    final int[] bp = nbt.getIntArray("EnableBloodPool");
                    if (bp.length != 5) {
                        return;
                    }
                    if (wounded.getHealth() / wounded.getMaxHealth() * 100.0f <= bp[3] && Tools.percenter(bp[4])) {
                        if (!nbt.hasKey("BloodPool")) {
                            return;
                        }
                        if (wounded.getMaxHealth() - wounded.getHealth() >= nbt.getDouble("BloodPool")) {
                            wounded.heal((float) nbt.getDouble("BloodPool"));
                            nbt.setDouble("BloodPool", 0.0);
                        } else {
                            nbt.setDouble(
                                "BloodPool",
                                nbt.getDouble("BloodPool") - wounded.getMaxHealth() + wounded.getHealth());
                            wounded.setHealth(wounded.getMaxHealth());
                        }
                    }
                }
            }
        }
    }
}

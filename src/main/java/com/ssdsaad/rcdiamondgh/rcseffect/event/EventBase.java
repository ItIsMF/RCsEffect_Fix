package com.ssdsaad.rcdiamondgh.rcseffect.event;

import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraftforge.common.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;

import com.rcdiamondgh.utils.*;

import cpw.mods.fml.common.eventhandler.*;

public class EventBase {

    public EventBase() {
        MinecraftForge.EVENT_BUS.register((Object) this);
    }

    @SubscribeEvent
    public void onLivingHurt(final LivingHurtEvent event) {
        // 防止在药水伤害(Magic/Wither)的处理逻辑中再次修改药水列表
        if (event.source.isMagicDamage() || "wither".equals(event.source.getDamageType())) {
            return;
        }

        final EntityLivingBase wounded = event.entityLiving;
        // 获取伤害来源实体，增加 instanceof 检查安全性
        final EntityLivingBase attacker = (event.source.getEntity() instanceof EntityLivingBase)
            ? (EntityLivingBase) event.source.getEntity()
            : null;

        // --- 处理受伤者 (Wounded) 的防御/反击效果 ---
        if (wounded != null) {
            for (int i = 0; i < 5; i++) {
                ItemStack stack = wounded.getEquipmentInSlot(i);
                if (stack == null || !stack.hasTagCompound()) continue;

                NBTTagCompound nbt = stack.getTagCompound();

                // 触发受伤药水效果
                if (nbt.hasKey("HurtEffect")) {
                    int[] effects = nbt.getIntArray("HurtEffect");
                    if (nbt.hasKey("HurtEffect_random")) {
                        String randomStr = nbt.getString("HurtEffect_random");
                        Tools.bufferGiverController(randomStr, wounded, effects);
                    } else {
                        Tools.bufferGiverController(wounded, effects);
                    }
                }

                // 受伤音效
                if (nbt.hasKey("HurtEffect_sound")) {
                    Tools.soundPlayer(wounded, nbt.getString("HurtEffect_sound"));
                }

                // 受伤提示 (仅玩家)
                if (wounded instanceof EntityPlayer && nbt.hasKey("HurtTips")) {
                    Tools.playerTalker((EntityPlayer) wounded, nbt.getTagList("HurtTips", 8));
                }
            }
        }

        // --- 处理攻击者 (Attacker) 的攻击/吸血效果 ---
        if (attacker != null && wounded != null) {
            for (int i = 0; i < 5; i++) {
                ItemStack stack = attacker.getEquipmentInSlot(i);
                if (stack == null || !stack.hasTagCompound()) continue;

                NBTTagCompound nbt = stack.getTagCompound();

                // 攻击无敌时间修改
                if (nbt.hasKey("attackSpeed")) {
                    Tools.immuneChanger(wounded, nbt.getInteger("attackSpeed"));
                }

                // 攻击给予敌人效果 (AttackEnemy)
                if (nbt.hasKey("AttackEnemy")) {
                    int[] effects = nbt.getIntArray("AttackEnemy");
                    if (nbt.hasKey("AttackEnemy_random")) {
                        Tools.bufferGiverController(nbt.getString("AttackEnemy_random"), wounded, effects);
                    } else {
                        Tools.bufferGiverController(wounded, effects);
                    }
                }
                // 音效
                if (nbt.hasKey("AttackEnemy_sound")) {
                    Tools.soundPlayer(wounded, nbt.getString("AttackEnemy_sound"));
                }

                // 攻击给予自身效果 (PlayerAttack / SelfBuff)
                if (nbt.hasKey("PlayerAttack")) {
                    int[] effects = nbt.getIntArray("PlayerAttack");
                    if (nbt.hasKey("PlayerAttack_random")) {
                        Tools.bufferGiverController(nbt.getString("PlayerAttack_random"), attacker, effects);
                    } else {
                        Tools.bufferGiverController(attacker, effects);
                    }
                }
                if (nbt.hasKey("PlayerAttack_sound")) {
                    Tools.soundPlayer(attacker, nbt.getString("PlayerAttack_sound"));
                }

                // 击退逻辑
                if (nbt.hasKey("knockBack")) {
                    Tools.knockBacker(wounded, attacker, nbt.getFloat("knockBack"));
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerRightClick(final PlayerUseItemEvent.Start event) {
        final EntityPlayer player = event.entityPlayer;
        final ItemStack item = event.item;
        if (player == null || item == null) {
            return;
        }
        if (item.hasTagCompound()) {
            final NBTTagCompound nbt = item.getTagCompound();
            if (nbt.hasKey("UseEffect")) {
                if (nbt.hasKey("AllowSneaking")) {
                    if (nbt.getByte("AllowSneaking") != 0 && player.isSneaking()) {
                        if (nbt.hasKey("UseEffect_random")) {
                            Tools.bufferGiverController(
                                nbt.getString("UseEffect_random"),
                                (EntityLivingBase) player,
                                nbt.getIntArray("UseEffect"));
                        } else {
                            Tools.bufferGiverController((EntityLivingBase) player, nbt.getIntArray("UseEffect"));
                        }
                    }
                } else {
                    if (nbt.hasKey("UseEffect_random")) {
                        Tools.bufferGiverController(
                            nbt.getString("UseEffect_random"),
                            (EntityLivingBase) player,
                            nbt.getIntArray("UseEffect"));
                    } else {
                        Tools.bufferGiverController((EntityLivingBase) player, nbt.getIntArray("UseEffect"));
                    }
                    if (nbt.hasKey("UseEffect_sound")) {
                        Tools.soundPlayer((EntityLivingBase) player, nbt.getString("UseEffect_sound"));
                    }
                }
            }
            if (nbt.hasKey("UseEffect_sound")) {
                boolean x = true;
                if (nbt.hasKey("AllowSneaking") && nbt.getByte("AllowSneaking") != 0) {
                    x = false;
                    if (player.isSneaking()) {
                        x = true;
                    }
                }
                if (x) {
                    Tools.soundPlayer((EntityLivingBase) player, nbt.getString("UseEffect_sound"));
                }
            }
            if (nbt.hasKey("TellrawSomething")) {
                if (nbt.getByte("AllowSneaking") != 0) {
                    if (player.isSneaking()) {
                        Tools.playerTalker(player, nbt.getTagList("TellrawSomething", 8));
                    }
                } else {
                    Tools.playerTalker(player, nbt.getTagList("TellrawSomething", 8));
                }
            }
            if (nbt.hasKey("DamageItem")) {
                if (nbt.hasKey("AllowSneaking")) {
                    if (nbt.getByte("AllowSneaking") != 0 && player.isSneaking()) {
                        item.damageItem(nbt.getInteger("DamageItem"), (EntityLivingBase) player);
                    }
                } else {
                    item.damageItem(nbt.getInteger("DamageItem"), (EntityLivingBase) player);
                }
            }
        }
    }

    @SubscribeEvent
    public void onFinishUseAnItem(final PlayerUseItemEvent.Finish event) {
        final EntityPlayer player = event.entityPlayer;
        final ItemStack item = event.item;
        if (item.hasTagCompound()) {
            final NBTTagCompound nbt = item.getTagCompound();
            if (nbt.hasKey("FinishUseItemEffect")) {
                if (nbt.hasKey("FinishUseItemEffect_random")) {
                    final double[] rans = Tools.stringToDoubleArray(nbt.getString("FinishUseItemEffect_random"), ",");
                    Tools.bufferGiverController(
                        nbt.getString("FinishUseItemEffect_random"),
                        (EntityLivingBase) player,
                        nbt.getIntArray("FinishUseItemEffect"));
                } else {
                    Tools.bufferGiverController((EntityLivingBase) player, nbt.getIntArray("FinishUseItemEffect"));
                }
            }
            if (nbt.hasKey("FinishUseItemEffect_sound")) {
                Tools.soundPlayer((EntityLivingBase) player, nbt.getString("FinishUseItemEffect_sound"));
            }
        }
    }

    @SubscribeEvent
    public void onKillSomeone(final LivingDeathEvent event) {
        if (!(event.source.getEntity() instanceof EntityLivingBase)) {
            return;
        }
        EntityLivingBase killer = null;
        if (event.source.getSourceOfDamage() != null && event.source.getSourceOfDamage() instanceof EntityLivingBase) {
            killer = (EntityLivingBase) event.source.getSourceOfDamage();
        }
        if (killer != null) {
            ItemStack item = null;
            try {
                item = killer.getHeldItem();
            } catch (Exception ex) {}
            if (item != null && item.hasTagCompound()) {
                final NBTTagCompound nbt = item.getTagCompound();
                if (nbt.hasKey("KilledEffect")) {
                    if (nbt.hasKey("KilledEffect_random")) {
                        Tools.bufferGiverController(
                            nbt.getString("KilledEffect_random"),
                            killer,
                            nbt.getIntArray("KilledEffect"));
                    } else {
                        Tools.bufferGiverController(killer, nbt.getIntArray("KilledEffect"));
                    }
                }
                if (nbt.hasKey("KilledEffect_sound")) {
                    Tools.soundPlayer(killer, nbt.getString("KilledEffect_sound"));
                }
                if (nbt.hasKey("KilledTell") && killer instanceof EntityPlayer) {
                    Tools.playerTalker((EntityPlayer) killer, nbt.getTagList("KilledTell", 8));
                }
            }
        }
    }

    @SubscribeEvent
    public void onUpDate(final LivingEvent.LivingUpdateEvent event) {
        // LivingUpdateEvent 发生在 updatePotionEffects 之后，此时修改药水列表是安全的
        Tools.applyPendingBuffs(event.entityLiving);

        final EntityLivingBase entity = event.entityLiving;

        if (entity.ticksExisted % 20 != 0 || entity.worldObj.isRemote) {
            return;
        }

        // 装备栏检测
        for (int i = 0; i < 5; i++) {
            ItemStack stack = entity.getEquipmentInSlot(i);
            if (stack != null && stack.hasTagCompound()) {
                NBTTagCompound nbt = stack.getTagCompound();
                if (nbt.hasKey("WearEffect")) {
                    Tools.bufferGiverController(entity, nbt.getIntArray("WearEffect"));
                }
            }
        }

        // 玩家背包检测
        if (entity instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer) entity;
            for (ItemStack stack : player.inventory.mainInventory) {
                if (stack != null && stack.hasTagCompound()) {
                    NBTTagCompound nbt = stack.getTagCompound();
                    if (nbt.hasKey("HasEffect")) {
                        Tools.bufferGiverController(entity, nbt.getIntArray("HasEffect"));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerAttack(final AttackEntityEvent event) {
        final EntityPlayer player = event.entityPlayer;
        final ItemStack item = player.getHeldItem();
        if (item != null && item.hasTagCompound()) {
            final NBTTagCompound nbt = item.getTagCompound();
            if (nbt.hasKey("PlayerAttackII")) {
                if (nbt.hasKey("PlayerAttackII_random")) {
                    Tools.bufferGiverController(
                        nbt.getString("PlayerAttackII_random"),
                        (EntityLivingBase) player,
                        nbt.getIntArray("PlayerAttackII"));
                } else {
                    Tools.bufferGiverController((EntityLivingBase) player, nbt.getIntArray("PlayerAttackII"));
                }
            }
            if (nbt.hasKey("PlayerAttackII_sound")) {
                Tools.soundPlayer((EntityLivingBase) player, nbt.getString("PlayerAttackII_sound"));
            }
        }
    }

    @SubscribeEvent
    public void suitEffect(final LivingEvent.LivingUpdateEvent event) {
        final EntityLivingBase entity = event.entityLiving;
        final ItemStack[] items = { entity.getEquipmentInSlot(0), entity.getEquipmentInSlot(1),
            entity.getEquipmentInSlot(2), entity.getEquipmentInSlot(3), entity.getEquipmentInSlot(4) };
        final int[][] effNums = { null, null, null, null, null };
        final int[][] marks = { null, null, null, null, null };
        final boolean[] alo = { false, false, false, false, false };
        for (int x = 0; x < 5; ++x) {
            if (items[x] != null && items[x].hasTagCompound()) {
                final NBTTagCompound nbt = items[x].getTagCompound();
                if (nbt.hasKey("SuitMark")) {
                    marks[x] = nbt.getIntArray("SuitMark");
                }
                if (nbt.hasKey("SuitEffect")) {
                    effNums[x] = nbt.getIntArray("SuitEffect");
                }
            }
        }
        for (int x = 0; x < 5; ++x) {
            if (!alo[x] && marks[x] != null) {
                int u = marks[x][0];
                if (u <= 1) {
                    u = 2;
                }
                if (u > 5) {
                    u = 5;
                }
                int flag = 0;
                for (int y = 0; y < 5; ++y) {
                    if (marks[y] != null && marks[y][1] == marks[x][1]) {
                        ++flag;
                    }
                }
                if (flag == u) {
                    for (int y = 0; y < 5; ++y) {
                        if (marks[y] != null && marks[y][1] == marks[x][1]) {
                            alo[y] = true;
                        }
                    }
                }
            }
        }
        for (int x = 0; x < 5; ++x) {
            if (alo[x] && effNums[x] != null) {
                Tools.bufferGiverController(entity, effNums[x]);
            }
        }
    }
}

package com.rcdiamondgh.utils;

import java.io.*;
import java.util.*;

import net.minecraft.client.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.potion.*;
import net.minecraft.util.*;
import net.minecraftforge.client.*;

import cpw.mods.fml.client.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.relauncher.*;

public class Tools {

    public static final String MODID = "rcseffect:";
    public static String gameFile;
    public static File debugFile;
    private static Minecraft mc;

    public Tools(final FMLPreInitializationEvent event) {
        Tools.gameFile = event.getModConfigurationDirectory()
            .getParentFile()
            .getPath();
    }

    public static void fucker(final int times, final String text) {
        for (int a = 0; a < times; ++a) {
            System.out.println(text);
        }
    }

    @Debug
    public static void debugInfo() {
        if (!Tools.debugFile.exists()) {
            try {
                Tools.debugFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Debug
    public static void writeInDebugInfo(final String info) {
        OutputStream writer = null;
        try {
            writer = new FileOutputStream(Tools.debugFile, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            final String str = getTime() + "\r\n" + info + "\r\n";
            writer.write(str.getBytes());
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static void buffGiver(final EntityLivingBase entity, final PotionEffect ea) {
        if (entity == null || ea == null || entity.isDead) {
            return;
        }

        // 获取对应的药水对象进行安全检查
        if (ea.getPotionID() < 0 || ea.getPotionID() >= Potion.potionTypes.length
            || Potion.potionTypes[ea.getPotionID()] == null) {
            return;
        }

        PotionEffect active = entity.getActivePotionEffect(Potion.potionTypes[ea.getPotionID()]);

        if (active == null) {
            entity.addPotionEffect(new PotionEffect(ea));
        } else {
            boolean isAmplifierStronger = ea.getAmplifier() > active.getAmplifier();
            boolean isDurationRunningOut = (ea.getAmplifier() == active.getAmplifier()) && (active.getDuration() <= 10);

            if (isAmplifierStronger || isDurationRunningOut) {
                entity.addPotionEffect(new PotionEffect(ea));
            }
        }
    }

    public static void bufferGiverController(final String str, final EntityLivingBase entity, final int[] buff) {
        final PotionEffect[] b = new PotionEffect[buff.length / 3];
        int index = 0;
        if (buff.length % 3 == 0) {
            final int a = buff.length / 3;
            int foot = 0;
            for (int aa = 0; aa < a; ++aa) {
                b[index] = new PotionEffect(buff[foot], buff[foot + 1], buff[foot + 2]);
                foot += 3;
                ++index;
            }
        } else {
            final int a = (buff.length - buff.length % 3) / 3;
            int foot = 0;
            for (int aa = 0; aa < a; ++aa) {
                b[index] = new PotionEffect(buff[foot], buff[foot + 1], buff[foot + 2]);
                foot += 3;
                ++index;
            }
        }
        final double[] ran = stringToDoubleArray(str, ",");
        int a2 = 0;
        for (final PotionEffect e : b) {
            if (a2 > ran.length - 1) {
                ++a2;
                buffGiver(entity, e);
            } else if (percenter(ran[a2])) {
                buffGiver(entity, e);
                ++a2;
            } else {
                ++a2;
            }
        }
    }

    public static void bufferGiverController(final EntityLivingBase entity, final int[] buff) {
        final PotionEffect[] b = new PotionEffect[buff.length / 3];
        int index = 0;
        if (buff.length % 3 == 0) {
            final int a = buff.length / 3;
            int foot = 0;
            for (int aa = 0; aa < a; ++aa) {
                b[index] = new PotionEffect(buff[foot], buff[foot + 1], buff[foot + 2]);
                foot += 3;
                ++index;
            }
        } else {
            final int a = (buff.length - buff.length % 3) / 3;
            int foot = 0;
            for (int aa = 0; aa < a; ++aa) {
                b[index] = new PotionEffect(buff[foot], buff[foot + 1], buff[foot + 2]);
                foot += 3;
                ++index;
            }
        }
        for (final PotionEffect p : b) {
            buffGiver(entity, p);
        }
    }

    public static void playerTalker(final EntityPlayer player, final NBTTagList list) {
        final int v = list.tagCount();
        final String[] str = new String[v];
        for (int a = 0; a < list.tagCount(); ++a) {
            str[a] = list.getStringTagAt(a);
        }
        final Random r = new Random();
        final int cl = r.nextInt(v);
        player.addChatMessage((IChatComponent) new ChatComponentText(str[cl]));
    }

    public static String getTime() {
        final Date c = new Date();
        final String basicTime = c.getHours() + ":" + c.getMinutes() + ":" + c.getSeconds();
        return "[" + basicTime + "]";
    }

    public static void immuneChanger(final EntityLivingBase entity, final int time) {
        entity.hurtResistantTime = time;
    }

    public static void knockBacker(final EntityLivingBase entity, final EntityLivingBase attacker,
        final float knockback) {
        int kbmf = 0;
        double kbmx = 0.0;
        double kbmy = 0.0;
        double kbmz = 0.0;
        kbmf = EnchantmentHelper.getKnockbackModifier(attacker, entity);
        if (attacker.isSprinting()) {
            ++kbmf;
        }
        kbmx = entity.motionX;
        kbmy = entity.motionY;
        kbmz = entity.motionZ;
        entity.motionX = kbmx;
        entity.motionY = kbmy;
        entity.motionZ = kbmz;
        double dx;
        double dz;
        for (dx = attacker.posX - entity.posX, dz = attacker.posZ - entity.posZ; dx * dx + dz * dz
            < 1.0E-4; dx = (Math.random() - Math.random()) * 0.01, dz = (Math.random() - Math.random()) * 0.01) {}
        entity.attackedAtYaw = (float) (Math.atan2(dz, dx) * 180.0 / 3.141592653589793) - entity.rotationYaw;
        final float f = (float) Math.sqrt(dx * dx + dz * dz);
        entity.motionX -= dx / f * knockback;
        entity.motionY += knockback;
        entity.motionZ -= dz / f * knockback;
        if (entity.motionY > 0.4) {
            entity.motionY = 0.4;
        }
        if (kbmf > 0) {
            dx = -Math.sin(Math.toRadians(attacker.rotationYaw)) * kbmf * 0.5;
            dz = Math.cos(Math.toRadians(attacker.rotationYaw)) * kbmf * 0.5;
            entity.addVelocity(dx, 0.1, dz);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerRenders(final Item item, final IItemRenderer model) {
        MinecraftForgeClient.registerItemRenderer(item, model);
    }

    public static MovingObjectPosition getPosition(final float frame, final float dist) {
        MovingObjectPosition mop = null;
        if (Tools.mc.renderViewEntity != null && Tools.mc.theWorld != null) {
            double var2 = dist;
            mop = Tools.mc.renderViewEntity.rayTrace(var2, frame);
            double calcdist = var2;
            final Vec3 pos = Tools.mc.renderViewEntity.getPosition(frame);
            var2 = calcdist;
            if (mop != null) {
                calcdist = mop.hitVec.distanceTo(pos);
            }
            final Vec3 lookvec = Tools.mc.renderViewEntity.getLook(frame);
            final Vec3 var3 = pos.addVector(lookvec.xCoord * var2, lookvec.yCoord * var2, lookvec.zCoord * var2);
            Entity pointedEntity = null;
            final float var4 = 1.0f;
            final List<Entity> list = (List<Entity>) Tools.mc.theWorld.getEntitiesWithinAABBExcludingEntity(
                (Entity) Tools.mc.renderViewEntity,
                Tools.mc.renderViewEntity.boundingBox
                    .addCoord(lookvec.xCoord * var2, lookvec.yCoord * var2, lookvec.zCoord * var2)
                    .expand((double) var4, (double) var4, (double) var4));
            double d = calcdist;
            for (final Entity entity : list) {
                if (entity.canBeCollidedWith()) {
                    final float bordersize = entity.getCollisionBorderSize();
                    final AxisAlignedBB aabb = entity.boundingBox
                        .expand((double) bordersize, (double) bordersize, (double) bordersize);
                    final MovingObjectPosition mop2 = aabb.calculateIntercept(pos, var3);
                    if (aabb.isVecInside(pos)) {
                        if (0.0 >= d && d != 0.0) {
                            continue;
                        }
                        pointedEntity = entity;
                        d = 0.0;
                    } else {
                        if (mop2 == null) {
                            continue;
                        }
                        final double d2 = pos.distanceTo(mop2.hitVec);
                        if (d2 >= d && d != 0.0) {
                            continue;
                        }
                        pointedEntity = entity;
                        d = d2;
                    }
                }
            }
            if (pointedEntity != null && (d < calcdist || mop == null)) {
                mop = new MovingObjectPosition(pointedEntity);
            }
        }
        return mop;
    }

    public static boolean percenter(final double num) {
        boolean foot = false;
        if (num >= 1.0) {
            return true;
        }
        if (num <= 0.0) {
            return false;
        }
        final double ranNum = Math.random();
        if (ranNum <= num) {
            foot = true;
        }
        return foot;
    }

    public static boolean percenter(final int x) {
        boolean foot = false;
        if (x >= 100) {
            return true;
        }
        if (x <= 0) {
            return false;
        }
        final int u = (int) (1.0 + Math.random() * 100.0);
        if (u < x) {
            foot = true;
        }
        return foot;
    }

    public static double[] stringToDoubleArray(final String str, final String pil) {
        try {
            if (str == null || str.isEmpty()) return new double[0];
            final String[] nums = str.split(pil);
            final double[] t = new double[nums.length];
            for (int x = 0; x < nums.length; ++x) {
                t[x] = Double.parseDouble(nums[x].trim());
            }
            return t;
        } catch (NumberFormatException e) {
            System.err.println("[RCE-Fix] Error parsing NBT double array: " + str);
            return new double[0]; // 返回空数组而不是崩溃
        }
    }

    public static String[] stringPlit(final String str, final String pil) {
        return str.split(pil);
    }

    public static int[] StringToIntArray(final String str, final String pil) {
        final String[] nums = str.split(pil);
        final int[] t = new int[nums.length];
        for (int x = 0; x < nums.length; ++x) {
            t[x] = Integer.parseInt(nums[x]);
        }
        return t;
    }

    public static double[] doubleArrayGeter(final Double[] array) {
        final double[] a = new double[array.length];
        for (int b = 0; b < array.length; ++b) {
            a[b] = array[b];
        }
        return a;
    }

    public static String[] stringArrayAdder(final String[] strs, final String str) {
        if (strs.length == 1 && strs[0] == null) {
            strs[0] = str;
            return strs;
        }
        final String[] result = new String[strs.length + 1];
        for (int x = 0; x < strs.length; ++x) {
            result[x] = strs[x];
        }
        result[result.length - 1] = str;
        return result;
    }

    public static int[] integerArrayGeter(final Integer[] array) {
        final int[] i = new int[array.length];
        for (int b = 0; b < array.length; ++b) {
            i[b] = array[b];
        }
        return i;
    }

    public static void soundPlayer(final EntityLivingBase entity, final String sound) {
        try {
            entity.worldObj.playSoundAtEntity((Entity) entity, sound, 1.0f, 1.0f);
        } catch (Exception ex) {}
    }

    static {
        Tools.gameFile = null;
        Tools.debugFile = new File("e:" + File.separator + "debugFile.txt");
        Tools.mc = FMLClientHandler.instance()
            .getClient();
    }
}

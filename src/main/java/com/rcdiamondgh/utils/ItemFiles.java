package com.rcdiamondgh.utils;

import java.io.*;
import java.util.*;

import cpw.mods.fml.common.event.*;

public class ItemFiles {

    List<File> itemNormal;
    List<File> itemSword;
    List<File> itemFood;
    List<File> itemBlock;
    List<File> itemArmor;
    List<File> itemBaubles;
    List<File> itemGun;
    public static List<CusItem> itemSwordData;
    public static List<CusItem> itemNormalData;
    public static List<CusItem> itemFoodData;
    public static List<CusItem> itemBlockData;
    public static List<CusItem> itemArmorData;
    public static List<CusItem> itemBaublesData;
    public static List<CusItem> itemGunData;
    public static File rceDir;

    public ItemFiles(final FMLPreInitializationEvent event) {
        this.itemNormal = new ArrayList<File>();
        this.itemSword = new ArrayList<File>();
        this.itemFood = new ArrayList<File>();
        this.itemBlock = new ArrayList<File>();
        this.itemArmor = new ArrayList<File>();
        this.itemBaubles = new ArrayList<File>();
        this.itemGun = new ArrayList<File>();
        ItemFiles.rceDir = new File(
            event.getModConfigurationDirectory()
                .getParentFile(),
            "/RCS/");
        if (!ItemFiles.rceDir.exists()) {
            ItemFiles.rceDir.mkdirs();
        }
        final File[] dirs = ItemFiles.rceDir.listFiles();
        if (!ItemFiles.rceDir.isDirectory()) {
            return;
        }
        if (dirs.length == 0) {
            return;
        }
        for (final File f : dirs) {
            if (f.isDirectory()) {
                if (f.getName()
                    .equals("sword")) {
                    for (final File fil : f.listFiles()) {
                        this.itemSword.add(fil);
                    }
                }
                if (f.getName()
                    .equals("item")) {
                    for (final File fil : f.listFiles()) {
                        this.itemNormal.add(fil);
                    }
                }
                if (f.getName()
                    .equals("food")) {
                    for (final File fil : f.listFiles()) {
                        this.itemFood.add(fil);
                    }
                }
                if (f.getName()
                    .equals("block")) {
                    for (final File fil : f.listFiles()) {
                        this.itemBlock.add(fil);
                    }
                }
                if (f.getName()
                    .equals("armor")) {
                    for (final File fil : f.listFiles()) {
                        this.itemArmor.add(fil);
                    }
                }
                if (f.getName()
                    .equals("baubles")) {
                    for (final File fil : f.listFiles()) {
                        this.itemBaubles.add(fil);
                    }
                }
                if (f.getName()
                    .equals("guns")) {
                    for (final File fil : f.listFiles()) {
                        this.itemGun.add(fil);
                    }
                }
            }
        }
        for (final File f2 : this.itemNormal) {
            ItemFiles.itemNormalData.add(reader(f2));
        }
        for (final File f2 : this.itemSword) {
            ItemFiles.itemSwordData.add(reader(f2));
        }
        for (final File f2 : this.itemFood) {
            ItemFiles.itemFoodData.add(reader(f2));
        }
        for (final File f2 : this.itemBlock) {
            ItemFiles.itemBlockData.add(reader(f2));
        }
        for (final File f2 : this.itemArmor) {
            ItemFiles.itemArmorData.add(reader(f2));
        }
        for (final File f2 : this.itemBaubles) {
            ItemFiles.itemBaublesData.add(reader(f2));
        }
        for (final File f2 : this.itemGun) {
            ItemFiles.itemGunData.add(reader(f2));
        }
    }

    public static CusItem reader(final File file) {
        if (file.isDirectory()) {
            return null;
        }
        final CusItem item = new CusItem();
        try {
            final InputStream read = new FileInputStream(file);
            final Properties prop = new Properties();
            prop.load(read);
            for (final String key : prop.stringPropertyNames()) {
                final String value = prop.getProperty(key);
                item.dataMaker(key, value);
            }
        } catch (Exception ex) {}
        return item;
    }

    static {
        ItemFiles.itemSwordData = new ArrayList<CusItem>();
        ItemFiles.itemNormalData = new ArrayList<CusItem>();
        ItemFiles.itemFoodData = new ArrayList<CusItem>();
        ItemFiles.itemBlockData = new ArrayList<CusItem>();
        ItemFiles.itemArmorData = new ArrayList<CusItem>();
        ItemFiles.itemBaublesData = new ArrayList<CusItem>();
        ItemFiles.itemGunData = new ArrayList<CusItem>();
    }
}

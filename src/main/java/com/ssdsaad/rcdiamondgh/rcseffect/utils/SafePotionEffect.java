package com.ssdsaad.rcdiamondgh.rcseffect.utils;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;

/**
 * 一个安全的空对象
 * 当药水在迭代过程中被移除时，使用此对象顶替，防止 NPE
 */
public class SafePotionEffect extends PotionEffect {

    // 覆盖原有药水效果逻辑
    public SafePotionEffect() {
        super(1, 10);
    }

    @Override
    public boolean onUpdate(EntityLivingBase entity) {
        return true;
    }

    @Override
    public void performEffect(EntityLivingBase entity) {}

    @Override
    public int getPotionID() {
        return 1;
    }

    @Override
    public int getDuration() {
        return 11;
    }

    @Override
    public String toString() {
        return "SafePotionEffect(Dummy)";
    }
}

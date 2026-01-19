package com.ssdsaad.rcdiamondgh.rcseffect.mixin;

import java.util.*;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.ssdsaad.rcdiamondgh.rcseffect.utils.SafePotionEffect;
import com.ssdsaad.rcdiamondgh.rcseffect.utils.SafePotionIterator;

/**
 * 注入 EntityLivingBase.updatePotionEffect 方法
 * 修复药水效果更新时的 CME 和 NPE 问题
 */
@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase {

    // Shadow 获取真实 Map 的引用，以便传递给迭代器
    @Shadow
    private HashMap activePotionsMap;

    @Unique
    private static final PotionEffect DUMMY_EFFECT = new SafePotionEffect();

    // 修复 CME + 幽灵效果
    @Redirect(
        method = "updatePotionEffects",
        at = @At(value = "INVOKE", target = "Ljava/util/Set;iterator()Ljava/util/Iterator;", remap = false))
    public Iterator safePotionIterator(Set instance) {
        // 1.传入 Set 用于生成快照
        // 2.传入真实 Map 用于删除操作
        return new SafePotionIterator(instance, this.activePotionsMap);
    }

    // 修复 NPE
    @Redirect(
        method = "updatePotionEffects",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/HashMap;get(Ljava/lang/Object;)Ljava/lang/Object;",
            remap = false))
    public Object safePotionGet(HashMap instance, Object key) {
        Object effect = instance.get(key);
        return effect == null ? DUMMY_EFFECT : effect;
    }
}

package com.ssdsaad.rcdiamondgh.rcseffect.mixin;

import java.util.*;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.ssdsaad.rcdiamondgh.rcseffect.utils.SafePotionEffect;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase {

    @Unique
    private static final PotionEffect DUMMY_EFFECT = new SafePotionEffect();

    /**
     * 修复 CME
     * 拦截 updatePotionEffects 中对 activePotionsMap.keySet().iterator() 的调用
     */
    @Redirect(
        method = "updatePotionEffects",
        at = @At(value = "INVOKE", target = "Ljava/util/Set;iterator()Ljava/util/Iterator;", remap = false))
    public Iterator safePotionIterator(Set instance) {
        // 创建快照
        return new ArrayList(instance).iterator();
    }

    /**
     * 修复 NPE
     * 如果脚本在遍历过程中移除了某个药水，Map.get(key) 会返回 null
     * 拦截调用，如果返回 null，则替换为 DUMMY_EFFECT
     */
    @Redirect(
        method = "updatePotionEffects",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/HashMap;get(Ljava/lang/Object;)Ljava/lang/Object;",
            remap = false))
    public Object safePotionGet(HashMap instance, Object key) {
        Object effect = instance.get(key);

        if (effect == null) {
            return DUMMY_EFFECT;
        }

        return effect;
    }
}

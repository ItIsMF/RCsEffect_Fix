package com.ssdsaad.rcdiamondgh.rcseffect.mixin;

import com.gtnewhorizon.gtnhmixins.builders.IMixins;
import com.gtnewhorizon.gtnhmixins.builders.MixinBuilder;

public enum MixinEnum implements IMixins {

    ENTITY_LIVING_BASE_MIXIN(Phase.EARLY, Side.COMMON, "com.ssdsaad.rcdiamondgh.rcseffect.mixin.MixinEntityLivingBase");

    private final MixinBuilder builder;

    MixinEnum(Phase phase, Side side, String... mixins) {
        builder = new MixinBuilder().setPhase(phase)
            .addSidedMixins(side, mixins);
    }

    @Override
    public MixinBuilder getBuilder() {
        return this.builder;
    }
}

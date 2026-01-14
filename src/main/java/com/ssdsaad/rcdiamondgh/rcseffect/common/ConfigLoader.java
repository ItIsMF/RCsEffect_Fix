package com.ssdsaad.rcdiamondgh.rcseffect.common;

import net.minecraftforge.common.config.*;

import org.apache.logging.log4j.*;

import cpw.mods.fml.common.event.*;

public class ConfigLoader {

    private static Configuration config;
    private static Logger logger;

    public ConfigLoader(final FMLPreInitializationEvent event) {
        ConfigLoader.logger = event.getModLog();
        (ConfigLoader.config = new Configuration(event.getSuggestedConfigurationFile())).load();
        load();
    }

    public static void load() {
        ConfigLoader.logger.info(
            "\u951f\u65a4\u62f7\u832b\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u951f\u65a4\u62f7mod\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u951f\u65a4\u62f7RC_diamond_GH\n\r\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u951f\u7d64od\u53ea\u951f\u65a4\u62f7\u6743\u951f\u65a4\u62f7\u951f\u527f\u2605\u62f7ChaosWorld\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u951f\u79f8\u65a4\u62f7\u647a\u951f\u8f83\u7889\u62f7\u951f\u65a4\u62f7\u951f\u72e1\u51e4\u62f7\u6c40\u951f\u65a4\u62f7\u951f\u7686\u7877\u62f7\u951f\u7ede\u655d\ue1c6\u62f7\u8c29\u951f\u7ede\u7678\u62f7\u951f\u7d53n\r\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u951f\u8f7f\u8fbe\u62f7\u951f\u65a4\u62f7\u951f\u9175\ue10a\u62f7\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u951f\u7ede\u7678\u62f7\u4e48\u951f\u7d64od\n\r\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u951f\u65a4\u62f7\u951f\u65a4\u62f7");
        ConfigLoader.config.save();
    }

    public static Logger logger() {
        return ConfigLoader.logger;
    }
}

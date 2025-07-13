package net.deadlydiamond98.koalalib.util.datagen;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;

public class LangDatagenUtil {

    public static void addPotion(FabricLanguageProvider.TranslationBuilder translationBuilder, StatusEffect effect, String name) {
        translationBuilder.add(effect, name);

        String lower = name.toLowerCase().replaceAll(" ", "_");

        translationBuilder.add("item.minecraft.potion.effect." + lower + "_potion", "Potion of " + name);
        translationBuilder.add("item.minecraft.potion.effect." + lower + "_potion_strong", "Potion of " + name);

        translationBuilder.add("item.minecraft.splash_potion.effect." + lower + "_potion", "Splash Potion of " + name);
        translationBuilder.add("item.minecraft.splash_potion.effect." + lower + "_potion_strong", "Splash Potion of " + name);

        translationBuilder.add("item.minecraft.lingering_potion.effect." + lower + "_potion", "Lingering Potion of " + name);
        translationBuilder.add("item.minecraft.lingering_potion.effect." + lower + "_potion_strong", "Lingering Potion of " + name);

        translationBuilder.add("item.minecraft.tipped_arrow.effect." + lower + "_potion", "Arrow of " + name);
        translationBuilder.add("item.minecraft.tipped_arrow.effect." + lower + "_potion_strong", "Arrow of " + name);
    }

    public static void addTemplate(FabricLanguageProvider.TranslationBuilder translationBuilder, Item template, String name, String additionalSlotDesc, String appliesTo, String baseSlotDesc, String ingredients, String title) {
        String modID = Registries.ITEM.getId(template).getNamespace();
        String id = Registries.ITEM.getId(template).getPath();

        translationBuilder.add(template, name);
        translationBuilder.add("smithing_template." + modID + "." + id + ".additions_slot_description", additionalSlotDesc);
        translationBuilder.add("smithing_template." + modID + "." + id + ".applies_to", appliesTo);
        translationBuilder.add("smithing_template." + modID + "." + id + ".base_slot_description", baseSlotDesc);
        translationBuilder.add("smithing_template." + modID + "." + id + ".ingredients", ingredients);
        translationBuilder.add("smithing_template." + modID + "." + id + ".title", title);
    }

    public static void addAdvancement(FabricLanguageProvider.TranslationBuilder translationBuilder, String key, String name, String desc) {
        translationBuilder.add(key, name);
        translationBuilder.add(key + ".desc", desc);
    }
}

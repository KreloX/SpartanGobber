package krelox.spartangobber;

import com.kwpugh.gobber2.init.ItemInit;
import com.kwpugh.gobber2.lists.tiers.ToolMaterialTiers;
import com.oblivioussp.spartanweaponry.ModSpartanWeaponry;
import com.oblivioussp.spartanweaponry.api.data.model.ModelGenerator;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import krelox.spartantoolkit.SpartanAddon;
import krelox.spartantoolkit.SpartanMaterial;
import krelox.spartantoolkit.WeaponMap;
import krelox.spartantoolkit.WeaponType;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Mod(SpartanGobber.MOD_ID)
public class SpartanGobber extends SpartanAddon {
    public static final String MOD_ID = "spartangobber";

    public static final WeaponMap WEAPONS = new WeaponMap();
    public static final DeferredRegister<Item> ITEMS = itemRegister(MOD_ID);
    public static final DeferredRegister<WeaponTrait> TRAITS = traitRegister(MOD_ID);
    public static final DeferredRegister<CreativeModeTab> TABS = tabRegister(MOD_ID);

    public static final RegistryObject<Item> GOBBER_POLE = ITEMS.register("gobber_pole", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NETHER_GOBBER_POLE = ITEMS.register("nether_gobber_pole", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> END_GOBBER_POLE = ITEMS.register("end_gobber_pole", () -> new Item(new Item.Properties()));

    // Materials
    public static final List<SpartanMaterial> MATERIALS = new ArrayList<>();

    public static final SpartanMaterial OVERWORLD_GOBBER = material("gobber", () -> ToolMaterialTiers.OVERWORLD_GOBBER, "ingots/gobber")
            .setAttackDamageModifier(-2).setAttackSpeedModifier(0.4F).setBow(ItemInit.GOBBER2_BOW).setHandle(ItemInit.GOBBER2_ROD).setPole(GOBBER_POLE);
    public static final SpartanMaterial NETHER_GOBBER = material("nether", () -> ToolMaterialTiers.NETHER_GOBBER, "ingots/gobber_nether")
            .setAttackSpeedModifier(0.6F).setBow(ItemInit.GOBBER2_BOW_NETHER).setHandle(ItemInit.GOBBER2_ROD_NETHER).setPole(NETHER_GOBBER_POLE);
    public static final SpartanMaterial END_GOBBER = material("end", () -> ToolMaterialTiers.END_GOBBER, "ingots/gobber_end")
            .setAttackDamageModifier(3).setAttackSpeedModifier(0.8F).setBow(ItemInit.GOBBER2_BOW_END).setHandle(ItemInit.GOBBER2_ROD_END).setPole(END_GOBBER_POLE);

    @SafeVarargs
    private static SpartanMaterial material(String name, Supplier<Tier> tier, String repairTag, RegistryObject<WeaponTrait>... traits) {
        TagKey<Item> repairTag1 = TagKey.create(Registries.ITEM, new ResourceLocation("forge", repairTag));
        SpartanMaterial material = new SpartanMaterial(name, MOD_ID, tier.get(), repairTag1, traits);
        MATERIALS.add(material);
        return material;
    }

    @SuppressWarnings("unused")
    public static final RegistryObject<CreativeModeTab> SPARTAN_SKIES_TAB = registerTab(TABS, MOD_ID,
            () -> WEAPONS.get(OVERWORLD_GOBBER, WeaponType.GREATSWORD).get(),
            (parameters, output) -> ITEMS.getEntries().forEach(item -> output.accept(item.get())));

    public SpartanGobber() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        registerSpartanWeapons(ITEMS);
        ITEMS.register(bus);
        TRAITS.register(bus);
        TABS.register(bus);
    }

    @Override
    protected void addTranslations(LanguageProvider provider, Function<RegistryObject<?>, String> formatName) {
        super.addTranslations(provider, formatName);
        provider.add(GOBBER_POLE.get(), formatName.apply(GOBBER_POLE));
        provider.add(NETHER_GOBBER_POLE.get(), formatName.apply(NETHER_GOBBER_POLE));
        provider.add(END_GOBBER_POLE.get(), formatName.apply(END_GOBBER_POLE));
    }

    @Override
    protected void registerModels(ItemModelProvider provider, ModelGenerator generator) {
        super.registerModels(provider, generator);
        generator.createSimpleModel(GOBBER_POLE.get(), new ResourceLocation(ModSpartanWeaponry.ID, "item/base/pole"));
        generator.createSimpleModel(NETHER_GOBBER_POLE.get(), new ResourceLocation(ModSpartanWeaponry.ID, "item/base/pole"));
        generator.createSimpleModel(END_GOBBER_POLE.get(), new ResourceLocation(ModSpartanWeaponry.ID, "item/base/pole"));
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        super.buildCraftingRecipes(consumer);
        BiConsumer<Item, RegistryObject<Item>> poleRecipe = (result, ingredient) -> ShapedRecipeBuilder
                .shaped(RecipeCategory.MISC, result).define('#', ingredient.get())
                .pattern(" #")
                .pattern("# ")
                .unlockedBy("has_rod", has(ingredient.get()))
                .save(consumer);
        poleRecipe.accept(GOBBER_POLE.get(), ItemInit.GOBBER2_ROD);
        poleRecipe.accept(NETHER_GOBBER_POLE.get(), ItemInit.GOBBER2_ROD_NETHER);
        poleRecipe.accept(END_GOBBER_POLE.get(), ItemInit.GOBBER2_ROD_END);
    }

    @Override
    public String modid() {
        return MOD_ID;
    }

    @Override
    public List<SpartanMaterial> getMaterials() {
        return MATERIALS;
    }

    @Override
    public WeaponMap getWeaponMap() {
        return WEAPONS;
    }
}

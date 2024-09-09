package krelox.spartangobber;

import com.kwpugh.gobber2.init.ItemInit;
import com.kwpugh.gobber2.lists.tiers.ToolMaterialTiers;
import com.oblivioussp.spartanweaponry.api.trait.WeaponTrait;
import krelox.spartantoolkit.SpartanAddon;
import krelox.spartantoolkit.SpartanMaterial;
import krelox.spartantoolkit.WeaponMap;
import krelox.spartantoolkit.WeaponType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
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
            .setHandle(ItemInit.GOBBER2_ROD).setPole(GOBBER_POLE).setBow(ItemInit.GOBBER2_BOW).setAttackSpeedModifier(0.4F);
    public static final SpartanMaterial NETHER_GOBBER = material("nether", () -> ToolMaterialTiers.NETHER_GOBBER, "ingots/gobber_nether")
            .setHandle(ItemInit.GOBBER2_ROD_NETHER).setPole(NETHER_GOBBER_POLE).setBow(ItemInit.GOBBER2_BOW_NETHER).setAttackSpeedModifier(0.6F);
    public static final SpartanMaterial END_GOBBER = material("end", () -> ToolMaterialTiers.END_GOBBER, "ingots/gobber_end")
            .setHandle(ItemInit.GOBBER2_ROD_END).setPole(END_GOBBER_POLE).setBow(ItemInit.GOBBER2_BOW_END).setAttackSpeedModifier(0.8F);

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

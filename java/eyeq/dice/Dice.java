package eyeq.dice;

import com.google.common.collect.Lists;
import eyeq.util.client.model.UModelCreator;
import eyeq.util.client.model.UModelLoader;
import eyeq.util.client.model.gson.ItemmodelJsonFactory;
import eyeq.util.client.renderer.ResourceLocationFactory;
import eyeq.util.client.resource.ULanguageCreator;
import eyeq.util.client.resource.USoundCreator;
import eyeq.util.client.resource.gson.SoundResourceManager;
import eyeq.util.client.resource.lang.LanguageResourceManager;
import eyeq.util.common.registry.UEntityRegistry;
import eyeq.util.common.registry.USoundEventRegistry;
import eyeq.util.oredict.CategoryTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import eyeq.dice.entity.projectile.EntityCoin;
import eyeq.dice.entity.projectile.EntityDice;
import eyeq.dice.item.ItemCoin;
import eyeq.dice.item.ItemDice;

import java.io.File;

import static eyeq.dice.Dice.MOD_ID;

@Mod(modid = MOD_ID, version = "1.0", dependencies = "after:eyeq_util")
@Mod.EventBusSubscriber
public class Dice {
    public static final String MOD_ID = "eyeq_dice";

    @Mod.Instance(MOD_ID)
    public static Dice instance;

    private static final ResourceLocationFactory resource = new ResourceLocationFactory(MOD_ID);

    public static final String I18n_COIN = "msg.coin.txt";
    public static final String I18n_HEADS = "msg.heads.txt";
    public static final String I18n_TAILS = "msg.tails.txt";
    public static final String I18n_DICE = "msg.dice.txt";

    public static SoundEvent entityCoinThrow;
    public static SoundEvent entityDiceThrow;

    public static Item coin;
    public static Item dice;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        addRecipes();
        registerEntities();
        registerSoundEvents();
        if(event.getSide().isServer()) {
            return;
        }
        renderItemModels();
        registerEntityRenderings();
        createFiles();
    }

    @SubscribeEvent
    protected static void registerItems(RegistryEvent.Register<Item> event) {
        coin = new ItemCoin().setUnlocalizedName("coin");
        dice = new ItemDice().setUnlocalizedName("dice");

        GameRegistry.register(coin, resource.createResourceLocation("coin"));
        GameRegistry.register(dice, resource.createResourceLocation("dice"));
    }

    public static void addRecipes() {
        GameRegistry.addRecipe(new ItemStack(coin), "X",
                'X', Items.GOLD_NUGGET);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(dice), " R ", "BWB", " B ",
                'B', CategoryTypes.PREFIX_DYE.getDictionaryName("black"), 'R', CategoryTypes.PREFIX_DYE.getDictionaryName("red"), 'W', new ItemStack(Blocks.WOOL)));
    }

    public static void registerEntities() {
        UEntityRegistry.registerModEntity(resource, EntityCoin.class, "Coin", 0, instance);
        UEntityRegistry.registerModEntity(resource, EntityDice.class, "Dice", 0, instance);
    }

    public static void registerSoundEvents() {
        entityCoinThrow = new SoundEvent(resource.createResourceLocation("entityCoinThrow"));
        entityDiceThrow = new SoundEvent(resource.createResourceLocation("entityDiceThrow"));

        USoundEventRegistry.registry(entityCoinThrow);
        USoundEventRegistry.registry(entityDiceThrow);
    }

    @SideOnly(Side.CLIENT)
    public static void renderItemModels() {
        UModelLoader.setCustomModelResourceLocation(coin);
        UModelLoader.setCustomModelResourceLocation(dice);
    }

    @SideOnly(Side.CLIENT)
    public static void registerEntityRenderings() {
        RenderingRegistry.registerEntityRenderingHandler(EntityCoin.class, manager -> new RenderSnowball(manager, coin, Minecraft.getMinecraft().getRenderItem()));
        RenderingRegistry.registerEntityRenderingHandler(EntityDice.class, manager -> new RenderSnowball(manager, dice, Minecraft.getMinecraft().getRenderItem()));
    }

    public static void createFiles() {
        File project = new File("../1.11.2-Dice");

        LanguageResourceManager language = new LanguageResourceManager();

        language.register(LanguageResourceManager.EN_US, coin, "Coin");
        language.register(LanguageResourceManager.JA_JP, coin, "コイン");
        language.register(LanguageResourceManager.EN_US, dice, "Dice");
        language.register(LanguageResourceManager.JA_JP, dice, "サイコロ");

        language.register(LanguageResourceManager.EN_US, EntityCoin.class, "Coin");
        language.register(LanguageResourceManager.JA_JP, EntityCoin.class, "コイン");
        language.register(LanguageResourceManager.EN_US, EntityDice.class, "Dice");
        language.register(LanguageResourceManager.JA_JP, EntityDice.class, "サイコロ");

        language.register(LanguageResourceManager.EN_US, I18n_COIN, "ping");
        language.register(LanguageResourceManager.JA_JP, I18n_COIN, "ピーン");
        language.register(LanguageResourceManager.EN_US, I18n_HEADS, "heads");
        language.register(LanguageResourceManager.JA_JP, I18n_HEADS, "おもて");
        language.register(LanguageResourceManager.EN_US, I18n_TAILS, "tails");
        language.register(LanguageResourceManager.JA_JP, I18n_TAILS, "うら");
        language.register(LanguageResourceManager.EN_US, I18n_DICE, "roll...");
        language.register(LanguageResourceManager.JA_JP, I18n_DICE, "コロコロ……");

        ULanguageCreator.createLanguage(project, MOD_ID, language);

        SoundResourceManager sound = new SoundResourceManager();

        sound.register(entityCoinThrow, SoundCategory.PLAYERS.getName(), Lists.newArrayList("random/bow"));
        sound.register(entityDiceThrow, SoundCategory.PLAYERS.getName(), Lists.newArrayList("random/bow"));

        USoundCreator.createSoundJson(project, MOD_ID, sound);

        UModelCreator.createItemJson(project, coin, ItemmodelJsonFactory.ItemmodelParent.GENERATED);
        UModelCreator.createItemJson(project, dice, ItemmodelJsonFactory.ItemmodelParent.GENERATED);
    }
}

package thesimpleton;

import basemod.*;
import basemod.abstracts.CustomUnlockBundle;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.relics.PaperCrane;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.AbstractUnlock;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import thesimpleton.cards.SimpletonCardHelper;
import thesimpleton.cards.HarvestTriggeredCard;
import thesimpleton.cards.ShuffleTriggeredCard;
import thesimpleton.cards.attack.*;
import thesimpleton.cards.curse.*;
import thesimpleton.cards.power.*;
import thesimpleton.cards.power.crop.*;
import thesimpleton.cards.skill.*;
import thesimpleton.characters.TheSimpletonCharacter;
import thesimpleton.crops.AbstractCrop;
import thesimpleton.crops.Crop;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.enums.TheSimpletonCharEnum;
import thesimpleton.events.*;
import thesimpleton.potions.AbundancePotion;
import thesimpleton.relics.*;
import thesimpleton.savedata.CardPoolCustomSavable;
import thesimpleton.savedata.SeasonCropsCustomSavable;
import thesimpleton.savedata.SeasonCustomSavable;
import thesimpleton.seasons.*;
import thesimpleton.seasons.cropsetdefinitions.RandomPartialUnlockCropSetTemplate;
import thesimpleton.seasons.cropsetdefinitions.RandomSeasonCropSetDefinition;
import thesimpleton.seasons.Season;
import thesimpleton.seasons.SeasonInfo;
import thesimpleton.seasons.cropsetdefinitions.UnlockableSeasonCropSetDefinition;
import thesimpleton.ui.seasons.SeasonIndicator;
import thesimpleton.ui.seasons.SeasonScreen;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


@SpireInitializer
public class TheSimpletonMod implements EditCardsSubscriber, EditCharactersSubscriber, EditRelicsSubscriber,
        EditStringsSubscriber, EditKeywordsSubscriber, PostInitializeSubscriber, PostCreateStartingDeckSubscriber,
        PostCreateStartingRelicsSubscriber, PostDungeonInitializeSubscriber, StartActSubscriber, StartGameSubscriber,
        OnStartBattleSubscriber, PostBattleSubscriber, PreRoomRenderSubscriber, PostDeathSubscriber, SetUnlocksSubscriber  {
    private static final Color CUSTOM_COLOR = CardHelper.getColor(57.0F, 131.0F, 245.0F);

    private static final String ATTACK_CARD = "512/attack_thesimpleton.png";
    private static final String SKILL_CARD = "512/skill_thesimpleton.png";
    private static final String POWER_CARD = "512/power_thesimpleton.png";
    private static final String ENERGY_ORB = "512/card_thesimpleton_orb.png";
    private static final String CARD_ENERGY_ORB = "512/card_small_orb.png";

    private static final String ATTACK_CARD_PORTRAIT = "1024/attack_thesimpleton.png";
    private static final String SKILL_CARD_PORTRAIT = "1024/skill_thesimpleton.png";
    private static final String POWER_CARD_PORTRAIT = "1024/power_thesimpleton.png";
    private static final String ENERGY_ORB_PORTRAIT = "1024/card_small_orb.png";

    private static final String MOD_NAME = "TheSimpleton";
    private static final String CONFIG_NAME = "TheSimpletonConfig";

    public static TheSimpletonCharacter theSimpletonCharacter;
    private static Properties theSimpletonProperties = new Properties();

    private static Map<String, Keyword> keywords;
    private ThemeState currentTheme;
    private static final List<AbstractCropPowerCard> SEASONAL_CROP_CARDS = new ArrayList<>();
    private static final  List<AbstractCard> cardPoolFromSave = new ArrayList<>();



    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
        logger.info("TheSimpletonMod::receivePostBattle isSeasonInitialized:"
            + isSeasonInitialized() + "; isDebug: " + Settings.isDebug);

        if (AbstractDungeon.player.hasRelic(HeatStroke.ID)) {
           ((HeatStroke)(AbstractDungeon.player.getRelic(HeatStroke.ID))).removeIfUsed();
        }

//        // DEBUG: reset unlock level
//        if (UnlockTracker.getUnlockLevel(TheSimpletonCharEnum.THE_SIMPLETON) > 0) {
//            logger.info("Resetting UnlockLevel to 0");
//            UnlockTracker.unlockProgress.putInteger(TheSimpletonCharEnum.THE_SIMPLETON.toString() + "UnlockLevel", 0);
//            SaveFile saveFile = new SaveFile(SaveFile.SaveType.POST_COMBAT);
//            SaveAndContinue.save(saveFile);
//        }
    }

    @Override
    public void receivePreRoomRender(SpriteBatch sb) {
//        if (seasonIndicator != null) {
//            seasonIndicator.render(sb);
//        }
    }

    @Override
    public void receivePostDeath() {
        logger.info("$@$@$@$@$@$@$@$@$@$ receivePostDeath: setting seasonInfo to NULL");
        seasonInfo = null;
        CUSTOM_SAVABLES.seasonCustomSavable.reset();
        seasonScreen.reset();
        SEASONAL_CROP_CARDS.clear();
        seasonCurseMap.clear();
        saveConfigData(true);
    }
//
//    @Override
//    public void receivePostEnergyRecharge() {
//        logger.info("TheSimpletonMod::receivePostEnergyRecharge : resetting hasHarvestedThisTurn");
//        AbstractCrop.resetHasHarvestedThisTurn();
//    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        logger.info("TheSimpletonMod::receiveOnBattleStart : resetting hasHarvestedThisTurn");
        AbstractCrop.resetHasHarvestedThisTurn();

        logger.info("TheSimpletonMod::receiveOnBattleStart: curse card pool cards: "
            + AbstractDungeon.curseCardPool.group.stream()
            .map(c -> c.name).collect(Collectors.joining(", ")));
    }


    public static void onBeforeStartOfTurnOrbs() {
        logger.info("TheSimpletonMod::onBeforeStartOfTurnOrbs : resetting hasHarvestedThisTurn");

        logger.info(">>>>>>>>>> DEBUG <<<<<<<<<< TheSimpletonMod::onBeforeStartOfTurnOrbs unlockLevel: " + UnlockTracker.getUnlockLevel(AbstractDungeon.player.chosenClass));


        AbstractCrop.resetHasHarvestedThisTurn();
    }

    public static boolean isPlayingAsSimpleton() {
        return  AbstractDungeon.player != null && AbstractDungeon.player.chosenClass == TheSimpletonCharEnum.THE_SIMPLETON;
    }

    private static class CUSTOM_SAVABLES {
        static CardPoolCustomSavable cardPoolSavable = new CardPoolCustomSavable();
        static SeasonCustomSavable seasonCustomSavable = new SeasonCustomSavable();
        static SeasonCropsCustomSavable seasonCropsCustomSavable = new SeasonCropsCustomSavable();
    }

    @Override
    public void receiveStartAct() {
        logger.info("TheSimpletonMod::receiveStartAct receiveStartAct called ===========================>>>>>>>");
        logger.info("TheSimpletonMod::receiveStartAct Doing nothing though.");
    }

    @Override
    public void receivePostDungeonInitialize() {
        logger.info("TheSimpletonMod::receivePostDungeonInitialize receivePostDungeonInitialize called ===========================>>>>>>>");
//        initializeSeason();
        seasonScreen.reset();
    }

    @Override
    public void receiveSetUnlocks() {
        BaseMod.addUnlockBundle(new CustomUnlockBundle(
            AbstractUnlock.UnlockType.CARD,
            "TheSimpletonMod:Squash",
            "TheSimpletonMod:Artichokes",
            "TheSimpletonMod:Spinach"
        ), TheSimpletonCharEnum.THE_SIMPLETON, 0);
        UnlockTracker.addCard("TheSimpletonMod:Squash");
        UnlockTracker.addCard("TheSimpletonMod:Artichokes");
        UnlockTracker.addCard("TheSimpletonMod:Spinach");

        BaseMod.addUnlockBundle(new CustomUnlockBundle(
            AbstractUnlock.UnlockType.CARD,
            "TheSimpletonMod:Onions",
            "TheSimpletonMod:Turnips",
            "TheSimpletonMod:Strawberries"
        ), TheSimpletonCharEnum.THE_SIMPLETON, 1);
        UnlockTracker.addCard("TheSimpletonMod:Onions");
        UnlockTracker.addCard("TheSimpletonMod:Turnips");
        UnlockTracker.addCard("TheSimpletonMod:Strawberries");

        BaseMod.addUnlockBundle(new CustomUnlockBundle(
            AbstractUnlock.UnlockType.CARD,
            "TheSimpletonMod:Coffee",
            "TheSimpletonMod:Chilis",
            "TheSimpletonMod:FanTheFlames"
        ), TheSimpletonCharEnum.THE_SIMPLETON, 2);
        UnlockTracker.addCard("TheSimpletonMod:Coffee");
        UnlockTracker.addCard("TheSimpletonMod:Chilis");
        UnlockTracker.addCard("TheSimpletonMod:FanTheFlames");

        BaseMod.addUnlockBundle(new CustomUnlockBundle(
            AbstractUnlock.UnlockType.CARD,
            "TheSimpletonMod:DoubleDigging",
            "TheSimpletonMod:CropDiversity",
            "TheSimpletonMod:Fecundity"
        ), TheSimpletonCharEnum.THE_SIMPLETON, 3);
        UnlockTracker.addCard("TheSimpletonMod:DoubleDigging");
        UnlockTracker.addCard("TheSimpletonMod:CropDiversity");
        UnlockTracker.addCard("TheSimpletonMod:Fecundity");

        BaseMod.addUnlockBundle(new CustomUnlockBundle(
            AbstractUnlock.UnlockType.CARD,
            "TheSimpletonMod:Polyculture",
            "TheSimpletonMod:LandGrant",
            "TheSimpletonMod:Barnstorm"
        ), TheSimpletonCharEnum.THE_SIMPLETON, 4);
        UnlockTracker.addCard("TheSimpletonMod:Polyculture");
        UnlockTracker.addCard("TheSimpletonMod:LandGrant");
        UnlockTracker.addCard("Barnstorm:Barnstorm");
    }

    private class ThemeState {
        private HashMap<TheSimpletonCharEnum.Theme, Boolean> currentThemeMap;

        public ThemeState() {
            currentThemeMap = new HashMap();
        }

        public Optional<TheSimpletonCharEnum.Theme> getCurrentTheme() {
            return currentThemeMap.entrySet()
                    .stream()
                    .filter(Map.Entry::getValue)
                    .sorted(Comparator.comparing(e -> e.getKey().ordinal()))
                    .findFirst()
                    .map(e -> e.getKey());
        }

        public void update(TheSimpletonCharEnum.Theme theme, boolean state) {
            currentThemeMap.put(theme, state);
        }

        public boolean isCurrentTheme(TheSimpletonCharEnum.Theme theme) {
            logger.info("ThemeState::isCurrentTheme called for theme: " + theme + "; isCurrentTheme: " + currentTheme.equals(theme));
            return this.getCurrentTheme().map(cur -> cur.equals(theme)).orElse(false);
        }
    }

    public static final Logger logger = LogManager.getLogger(TheSimpletonMod.class.getName());

    public TheSimpletonMod() {
        logger.info("Instantiating TheSimpletonMod.");

        logger.info("Logger class: " + logger.getClass().getCanonicalName());

        if (logger instanceof org.apache.logging.log4j.core.Logger && Loader.DEBUG) {
            logger.info("Setting log level to: " + Level.DEBUG);

            ((org.apache.logging.log4j.core.Logger)logger).setLevel(Level.DEBUG);
            Configurator.setRootLevel(Level.DEBUG);
            logger.debug("Show me the debug logs!");
        } else {
            logger.info("Unable to set log level. Loader.DEBUG: " + Loader.DEBUG);
        }

//        SimpletonUtil.initialize();

        BaseMod.subscribe(this);

        BaseMod.addColor(AbstractCardEnum.THE_SIMPLETON_BLUE,
                CUSTOM_COLOR, CUSTOM_COLOR, CUSTOM_COLOR, CUSTOM_COLOR, CUSTOM_COLOR, CUSTOM_COLOR, CUSTOM_COLOR,
                getResourcePath(ATTACK_CARD), getResourcePath(SKILL_CARD),
                getResourcePath(POWER_CARD), getResourcePath(ENERGY_ORB),
                getResourcePath(ATTACK_CARD_PORTRAIT), getResourcePath(SKILL_CARD_PORTRAIT),
                getResourcePath(POWER_CARD_PORTRAIT), getResourcePath(ENERGY_ORB_PORTRAIT),
                getResourcePath(CARD_ENERGY_ORB));

        currentTheme = new ThemeState();

        // initialize season screen
        // seasonScreen.init

        BaseMod.addSaveField(CUSTOM_SAVABLES.cardPoolSavable.getCustomSaveKey(),
            CUSTOM_SAVABLES.cardPoolSavable);
        BaseMod.addSaveField(CUSTOM_SAVABLES.seasonCustomSavable.getCustomSaveKey(),
            CUSTOM_SAVABLES.seasonCustomSavable);
        BaseMod.addSaveField(CUSTOM_SAVABLES.seasonCropsCustomSavable.getCustomSaveKey(),
            CUSTOM_SAVABLES.seasonCropsCustomSavable);

        loadConfigData();
    }

    public static String makeID(String idText) {
        return "TheSimpletonMod:" + idText;
    }

    public static final String getResourcePath(String resource) {
        return "TheSimpletonMod/img/" + resource;
    }

    public static void initialize() {
        new TheSimpletonMod();
    }

    private static boolean isGameInitialized = false;

    public static boolean isGameInitialized() {
        return isGameInitialized;
    }

    @Override
    public void receivePostInitialize() {
        logger.info("TheSimpletonMod::receivePostInitialize called ===========================>>>>>>>");

        TheSimpletonMod.isGameInitialized = true;

        Texture badgeTexture = new Texture(getResourcePath("badge.png"));
        ModPanel modPanel = new ModPanel();

        UIStrings descriptionString = CardCrawlGame.languagePack.getUIString("TheSimpletonMod:ThemeDescription");
        modPanel.addUIElement(
                new ModLabel(descriptionString.TEXT[0], 350.0f, 750.0f, Color.GOLD, modPanel, label -> {}));
        modPanel.addUIElement(
                new ModLabel(descriptionString.TEXT[1], 350.0f, 700.0f, Color.LIME, modPanel, label -> {}));

        Arrays.stream(TheSimpletonCharEnum.Theme.values())
                .filter(theme -> theme != TheSimpletonCharEnum.Theme.BASE_THEME)
                .forEach(theme -> {
                    logger.info("Adding theme: " + theme);
                    modPanel.addUIElement(new ModLabeledToggleButton(
                        CardCrawlGame.languagePack.getUIString(theme.toString()).TEXT[0],
                        350.0f, 650.0f - 50.f * theme.ordinal(), Settings.CREAM_COLOR,
                        FontHelper.charDescFont, currentTheme.isCurrentTheme(theme), modPanel,
                        label -> {},
                        button -> {
                            currentTheme.update(theme, button.enabled);
                            saveConfigData(false);
                        })
                    );}
                );

        // TODO: determine events based on season
        // TODO: intialize these programatically from SeasonalEvents
        BaseMod.addEvent(ReaptideEvent.ID, ReaptideEvent.class, Exordium.ID);
        BaseMod.addEvent(SnowedInEvent.ID, SnowedInEvent.class, Exordium.ID);
        BaseMod.addEvent(EarlyThawEvent.ID, EarlyThawEvent.class, Exordium.ID);
        BaseMod.addEvent(FirefliesEvent.ID, FirefliesEvent.class, Exordium.ID);
        BaseMod.addEvent(BorealisEvent.ID, BorealisEvent.class, TheCity.ID);
        BaseMod.addEvent(HeatWaveEvent.ID, HeatWaveEvent.class, TheCity.ID);

        BaseMod.registerModBadge(
                badgeTexture, "The Hayseed", "jwoolley",
                "Adds a new creature to the game - The Hayseed", modPanel);

        BaseMod.addPotion(
            AbundancePotion.class, AbundancePotion.BASE_COLOR, AbundancePotion.HYBRID_COLOR,
                AbundancePotion.SPOTS_COLOR, AbundancePotion.POTION_ID, TheSimpletonCharEnum.THE_SIMPLETON);

        HashMap<String, Sfx> reflectedMap = getSoundsMap();

        reflectedMap.put("ATTACK_BUZZ_1",
            new Sfx("TheSimpletonMod/sounds/TheSimpleton_Buzz1.ogg"));
        reflectedMap.put("ATTACK_FIRE_IMPACT_1",
            new Sfx("TheSimpletonMod/sounds/TheSimpleton_ImpactFire1.ogg"));
        reflectedMap.put("ATTACK_FIRE_IMPACT_2",
            new Sfx("TheSimpletonMod/sounds/TheSimpleton_ImpactFire2.ogg"));
        reflectedMap.put("ATTACK_SCIMITAR_1",
            new Sfx("TheSimpletonMod/sounds/TheSimpleton_AttackScimitar1.ogg"));
        reflectedMap.put("ATTACK_SCYTHE_1",
            new Sfx("TheSimpletonMod/sounds/TheSimpleton_AttackScythe1.ogg"));
        reflectedMap.put("BIRD_TWEET_1",
            new Sfx("TheSimpletonMod/sounds/TheSimpleton_BirdTweet1.ogg"));
        reflectedMap.put("FALL_MEADOW_1",
            new Sfx("TheSimpletonMod/sounds/TheSimpleton_FallMeadow1.ogg"));
        reflectedMap.put("GRADUAL_RUMBLE_1",
            new Sfx("TheSimpletonMod/sounds/TheSimpleton_GradualBuzz1.ogg"));
        reflectedMap.put("HOOTING_BIRD_1",
            new Sfx("TheSimpletonMod/sounds/TheSimpleton_HootingBird1.ogg"));
        reflectedMap.put("LOW_RUMBLE_1",
            new Sfx("TheSimpletonMod/sounds/TheSimpleton_LowRumble1.ogg"));
        reflectedMap.put("MAGIC_CHIMES_1",
            new Sfx("TheSimpletonMod/sounds/TheSimpleton_MagicChimes1.ogg"));
    }

    public static List<String> getSeasonalEventIds() {
        SeasonalEvents seasonalEvents = SeasonalEvents.getSeasonalEvents(getSeason());
        List<String> ids = new ArrayList<>();
        ids.addAll(seasonalEvents.exordiumEvents);
        ids.addAll(seasonalEvents.cityEvents);
        ids.addAll(seasonalEvents.beyondEvents);
        ids.addAll(seasonalEvents.globalEvents);
        return ids;
    }



    private void registerCustomSaveKeys() {

    }

    @Override
    public void receiveStartGame() {
        logger.info("TheSimpletonMod::receiveStartGame called ===========================>>>>>>>");
        initializeSeasonInfoIfNeeded();
        isGameInitialized = true;

        //TODO: initialize SeasonInfo on load from save
        seasonIndicator = SeasonIndicator.getIndicator(getSeason());
    }


    @SuppressWarnings("unchecked")
    private HashMap<String, Sfx> getSoundsMap() {
        return (HashMap<String, Sfx>) ReflectionHacks.getPrivate(CardCrawlGame.sound, SoundMaster.class, "map");
    }

    @Override
    public void receiveEditCharacters() {
        theSimpletonCharacter = new TheSimpletonCharacter("The Hayseed");
        BaseMod.addCharacter(
            theSimpletonCharacter, getResourcePath("charSelect/button.png"), getResourcePath("charSelect/portrait.png"),
                TheSimpletonCharEnum.THE_SIMPLETON);
    }

    public static List<AbstractCard> getBaseCardList() {
        List<AbstractCard> cards = new ArrayList<>();
        // Basic (4)
        cards.add(new Strike_TheSimpleton());
        cards.add(new Defend_TheSimpleton());
        cards.add(new Haymaker());
        cards.add(new ReapAndSow());

        // Attack (11)
        cards.add(new Barnstorm());
        cards.add(new BuzzBomb());
        cards.add(new CloseScrape());
        cards.add(new CullingStrike());
        cards.add(new DoubleBarrel());
        cards.add(new Fertilaser());
        cards.add(new FlashPasteurize());
        cards.add(new HitTheSack());
        cards.add(new KeenEdge());
        cards.add(new PestManagement());
        cards.add(new Rake());
        cards.add(new RootDown());
        cards.add(new SaltTheEarth());
        cards.add(new SlashAndBurn());
        cards.add(new SaladShooter());
        cards.add(new Sunseed());
        cards.add(new Sunchoke());
        cards.add(new Thresh());

        // Skill (20)
        cards.add(new Aerate());
        cards.add(new BumperCrop());
        cards.add(new ControlledBurn());
        cards.add(new CropDiversity());
        cards.add(new CropRotation());
        cards.add(new DesperatePlunge());
        cards.add(new DoubleDigging());
        cards.add(new ErosionControl());
        cards.add(new DigIn());
        cards.add(new FanTheFlames());
        cards.add(new Ferment());
        cards.add(new GoToMarket());
        cards.add(new Innovate());
        cards.add(new Mulch());
        cards.add(new OnionBloom());
        cards.add(new SeedCoat());
        cards.add(new Polyculture());
        cards.add(new Pruning());
        cards.add(new ResearchGrant());
        cards.add(new Rototilling());
        cards.add(new SoilSample());
        cards.add(new SpiceUp());
        cards.add(new StockTheCellar());
        cards.add(new Surplus());
        cards.add(new Swelter());
        cards.add(new Germinate());
        cards.add(new TakeHeart());
        cards.add(new ToughenUp());
        cards.add(new VineRipen());

        // Power (6)
        cards.add(new BirdFeeder());
        cards.add(new Biorefinement());
        cards.add(new CrackOfDawn());
        cards.add(new Fecundity());
        cards.add(new LandGrant());
        cards.add(new Photosynthesis());
        cards.add(new ResistantStrain());
        cards.add(new VolatileFumes());

        // Curse(6)
//        cards.add(new Frostbite());
//        cards.add(new Gnats());
//        cards.add(new Nettles());
//        cards.add(new Spoilage());
        return cards;
    }

    public static List<AbstractCropPowerCard> getCropCardList() {
        List<AbstractCropPowerCard> cards = new ArrayList<>();

        // Crop (10)
        cards.add(new Artichokes());
        cards.add(new Asparagus());
        cards.add(new Chilis());
        cards.add(new Coffee());
        cards.add(new Corn());
        cards.add(new Mushrooms());
        cards.add(new Onions());
        cards.add(new Potatoes());
        cards.add(new Spinach());
        cards.add(new Squash());
        cards.add(new Strawberries());
        cards.add(new Turnips());

        return cards;
    }

    public static List<AbstractCard> getSeasonalCurseCardList() {
        List<AbstractCard> cards = new ArrayList<>();

        cards.add(new Gnats());
        cards.add(new Frostbite());
        cards.add(new Nettles());
        cards.add(new Spoilage());

        return cards;
    }

    @Override
    public void receiveEditCards() {
        //  initializeSeason(); // TODO: NOT SURE IF THIS NEEDS TO BE IN THIS HOOK OR CAN BE ELSEWHERE

        logger.info("TheSimpletonMod::receiveEditCards called ===========================>>>>>>>");

        for(AbstractCard card : getBaseCardList()) {
            BaseMod.addCard(card);
        }

        for(AbstractCard card : getCropCardList()) {
            BaseMod.addCard(card);
        }
    }

    private static SeasonInfo seasonInfo;

    public static boolean isSeasonInitialized() {
        logger.info("@@@@@DEBUG@@@@@ TheSimpletonMod::isSeasonInitialized : " + (seasonInfo != null));
        return seasonInfo != null;
    }

    public static Season getSeason() {
        return seasonInfo.getSeason();
    }

//    public void setSeasonFromSave() {
//        seasonInfo = new SeasonInfo(CUSTOM_SAVABLES.seasonCustomSavable.getSeason(), SeasonInfo.RANDOM_CROP_SET_BY_RARE_CROP_RARITY);
//    }

    // When loading a saved game, this need to be called after all custom saves are loaded (since it needs all
    // SeasonInfo data), but before card pools initialized. So the getCardPool before hook will call this before it
    // needs the SeasonInfo.
    //
    // For new games, SeasonInfo should be instantiated by now, so leave it alone.
    public static void initializeSeasonInfoIfNeeded() {
        logger.info("initializeSeasonInfoIfNeeded called");
        if (!isSeasonInitialized()) {
            Season savedSeason = CUSTOM_SAVABLES.seasonCustomSavable.getSeason();
            List<Crop> savedCrops = CUSTOM_SAVABLES.seasonCropsCustomSavable.getCropsInSeason();

            if (!savedCrops.isEmpty()) {
                List<AbstractCropPowerCard> cropCards = savedCrops.stream()
                    .map(c -> c.getCropInfo().powerCard).filter(Objects::nonNull).collect(Collectors.toList());

                SEASONAL_CROP_CARDS.clear();
                SEASONAL_CROP_CARDS.addAll(cropCards);

                logger.info("initializeSeasonInfoIfNeeded | Loaded in-season crops from save:"
                    + savedCrops.stream().map(c -> c.name()).collect(Collectors.joining(", ")));
                logger.info("initializeSeasonInfoIfNeeded | Setting in-season crop cards:"
                    + cropCards.stream().map(c -> c.name).collect(Collectors.joining(", ")));
            } else {
                logger.info("initializeSeasonInfoIfNeeded | empty crop list from savable."
                    + savedCrops.stream().map(c -> c.name()).collect(Collectors.joining(", ")));
            }

            Season season;
            if (savedSeason != null) {
                logger.info("TheSimpletonMod::receiveStartGame applying season from save");
                seasonInfo = new SeasonInfo(savedSeason, savedCrops);
                // season = seasonInfo.getSeason();
            } else if (isSeasonInitialized()) {
                logger.info("TheSimpletonMod::receiveStartGame applying season from seasonInfo (game start) "
                    + " (currently this does nothing");
                // season = seasonInfo.getSeason();
            } else {
                logger.info("TheSimpletonMod::receiveStartGame choosing season");
                seasonInfo = chooseSeason();
            }
        }
    }

    private void initializeSeason() {
        logger.info("@@@@@DEBUG@@@@@ TheSimpletonMod::initializeSeason Initializing Season ...");

        seasonInfo = chooseSeason();

        List<AbstractCropPowerCard> seasonalCropCards = chooseSeasonalCropCards(seasonInfo);
        setSeasonalCropCards(seasonalCropCards);
        if (seasonIndicator != null) {
            seasonIndicator.reset();
        }

        getSeasonalCurseCards(seasonInfo.getSeason());

        CUSTOM_SAVABLES.seasonCustomSavable.reset();
    }

    public static void setSeasonalCropCards() {
        logger.info("@@@@@DEBUG@@@@@ setSeasonalCropCards Inferring from card pool..");
        setSeasonalCropCards(SimpletonCardHelper.getCurrentCardPool().stream()
            .filter(c -> c instanceof AbstractCropPowerCard)
            .map(c -> (AbstractCropPowerCard) c)
            .collect(Collectors.toList()));
    }


    public static void setSeasonalCropCards(List<AbstractCropPowerCard> cards) {
        logger.info("@@@@@DEBUG@@@@@ setSeasonalCropCards Generating season info ...");
        logger.info("@@@@@DEBUG@@@@@ setSeasonalCropCards adding cards: " + cards.stream()
            .filter(c -> c instanceof  AbstractCropPowerCard).map(c -> c.name)
            .collect(Collectors.joining(", ")));

        SEASONAL_CROP_CARDS.clear();
        SEASONAL_CROP_CARDS.addAll(cards);
    }


    private static boolean unlockLevelChanged() {
       return UnlockTracker.getUnlockLevel(AbstractDungeon.player.chosenClass) > ConfigData.unlockLevelLastRun;
    }

    private static SeasonInfo chooseSeason() {
        final boolean isFirstRunAtCurrentUnlockLevel = unlockLevelChanged();
        final int unlockLevel = UnlockTracker.getUnlockLevel(AbstractDungeon.player.chosenClass);

        logger.info("==========|||||==========|||||============ chooseSeason: Unlock level"
            + (isFirstRunAtCurrentUnlockLevel ?
                " CHANGED FROM LAST RUN FROM " + ConfigData.unlockLevelLastRun + " to " + unlockLevel
                : " unchanged from last run: " +  UnlockTracker.getUnlockLevel(AbstractDungeon.player.chosenClass)));

        ConfigData.unlockLevelLastRun = UnlockTracker.getUnlockLevel(AbstractDungeon.player.chosenClass);

        switch (UnlockTracker.getUnlockLevel(AbstractDungeon.player.chosenClass)) {
        case 0:
            logger.info("chooseSeason: Choosing predefined season for unlock level: " + unlockLevel);
            return new SeasonInfo(UnlockableSeasonCropSetDefinition.UNLOCK_CROP_SET_0);
        case 1:
            if (isFirstRunAtCurrentUnlockLevel) {
                logger.info("chooseSeason: Choosing predefined season for unlock level: " + unlockLevel);
                return new SeasonInfo(UnlockableSeasonCropSetDefinition.UNLOCK_CROP_SET_1);
            } else {
                logger.info("chooseSeason: Choosing partial randomized season for unlock level: " + unlockLevel);
                return new SeasonInfo(
                        RandomPartialUnlockCropSetTemplate.PARTIAL_UNLOCK_CROP_SET_1.getRandomCropSetDefinition());
            }
        case 2:
            if (isFirstRunAtCurrentUnlockLevel) {
                logger.info("chooseSeason: Choosing predefined season for unlock level: " + unlockLevel);
                return new SeasonInfo(UnlockableSeasonCropSetDefinition.UNLOCK_CROP_SET_2);
            } else {
                logger.info("chooseSeason: Choosing partial randomized season for unlock level: " + unlockLevel);
                return new SeasonInfo(
                        RandomPartialUnlockCropSetTemplate.PARTIAL_UNLOCK_CROP_SET_2.getRandomCropSetDefinition());
            }
        case 3:
            if (isFirstRunAtCurrentUnlockLevel) {
                logger.info("chooseSeason: Choosing predefined season for unlock level: " + unlockLevel);
                return new SeasonInfo(UnlockableSeasonCropSetDefinition.UNLOCK_CROP_SET_3);
            }  else {
                logger.info("chooseSeason: Choosing fully randomized season for unlock level: " + unlockLevel);
                return new SeasonInfo(Season.randomSeason(),
                        RandomSeasonCropSetDefinition.RANDOM_CROP_SET_BY_RARE_CROP_RARITY);
            }
        default:
            logger.info("chooseSeason: Choosing fully randomized season for unlock level post unlock level 3."
            + " Current unlock level: " + unlockLevel);
            return new SeasonInfo(Season.randomSeason(),
                    RandomSeasonCropSetDefinition.RANDOM_CROP_SET_BY_RARE_CROP_RARITY);
        }
    }

    private List<AbstractCropPowerCard> chooseSeasonalCropCards(SeasonInfo seasonInfo) {
        logger.info("@@@@@DEBUG@@@@@ Generating season info ...");
        logger.info("SeasonInfo | "
            + "season: " + seasonInfo.getSeason()
            + " cropsInSeason: "
            + seasonInfo.getCropsInSeason().stream().map(c -> c.getName()).collect(Collectors.joining(", "))
            + "\n\n"
        );
        return seasonInfo.getCropsInSeason().stream()
            .map(crop -> crop.getCropInfo().powerCard)
            .collect(Collectors.toList());
    }

    public static void addToCardPool(AbstractCard card) {
        logger.info("Attempting to add card to pool:" + card.name);

        switch (card.rarity) {
            case COMMON:
                logger.info("Adding common card to pool:" + card.name);
                // TODO: check for and don't add duplicates
                if (AbstractDungeon.commonCardPool.group.stream().noneMatch(c -> c.cardID == card.cardID)) {
                    AbstractDungeon.commonCardPool.group.add(card);
                }
                break;
            case UNCOMMON:
                if (AbstractDungeon.uncommonCardPool.group.stream().noneMatch(c -> c.cardID == card.cardID)) {
                    AbstractDungeon.uncommonCardPool.group.add(card);
                    logger.info("Adding uncommon card to pool:" + card.name);
                }
                break;
            case RARE:
                if (AbstractDungeon.rareCardPool.group.stream().noneMatch(c -> c.cardID == card.cardID)) {
                    AbstractDungeon.rareCardPool.group.add(card);
                    logger.info("Adding rare card to pool:" + card.name);
                }
                break;

            case CURSE:
                if (AbstractDungeon.curseCardPool.group.stream().noneMatch(c -> c.cardID == card.cardID)) {
                    AbstractDungeon.curseCardPool.group.add(card);
                    logger.info("Adding curse card to pool:" + card.name);
                }
                break;

            default:
                logger.warn("Can't add card " + card.name + " to card pool: " + card.rarity + " is not a supported rarity");
                break;
        }
    }

    public static void removeUnseasonalCardsFromPool() {
        removeUnusedCropPowerCardsFromPool();
        removeUnusedSeasonalCurseCardsFromPool();
    }

    public static void removeUnusedCropPowerCardsFromPool() {
        List<AbstractCropPowerCard> seasonalCrops = getSeasonalCropCards();

        List<AbstractCropPowerCard> cardsToRemove = getCropCardList().stream()
            .filter(c -> !seasonalCrops.stream().anyMatch(c2 -> c.cardID == c2.cardID))
            .collect(Collectors.toList());

        logger.info("removeUnusedCropPowerCardsFromPool called. Complete List: " + getCropCardList().stream().map(c -> c.name).collect(Collectors.joining(", ")));
        logger.info("removeUnusedCropPowerCardsFromPool called. Seasonal Crops: " + getSeasonalCropCards().stream().map(c -> c.name).collect(Collectors.joining(", ")));
        logger.info("removeUnusedCropPowerCardsFromPool called. Unseasonal Crops: " + cardsToRemove.stream().map(c -> c.name).collect(Collectors.joining(", ")));

        TheSimpletonMod.removeCropPowerCardsFromPool(cardsToRemove);
    }

    public static void removeUnusedSeasonalCurseCardsFromPool() {
        if (!isSeasonInitialized()) {
            logger.info("removeUnusedSeasonalCurseCardsFromPool : season not initialized. Not removing any cards.");
        }

        List<AbstractCard> seasonalCurses = seasonCurseMap.get(getSeason());

        List<AbstractCard> cardsToRemove = getSeasonalCurseCardList().stream()
            .filter(c -> !seasonalCurses.stream().anyMatch(c2 -> c.cardID == c2.cardID))
            .collect(Collectors.toList());

        logger.info("removeUnusedSeasonalCurseCardsFromPool called. Complete List: " + getSeasonalCurseCardList().stream().map(c -> c.name).collect(Collectors.joining(", ")));
        logger.info("removeUnusedSeasonalCurseCardsFromPool called. Seasonal Curses: " + getSeasonalCropCards().stream().map(c -> c.name).collect(Collectors.joining(", ")));
        logger.info("removeUnusedSeasonalCurseCardsFromPool called. Removing unseasonal Curses: " + cardsToRemove.stream().map(c -> c.name).collect(Collectors.joining(", ")));

        TheSimpletonMod.removeCardsFromPool(cardsToRemove);
    }

    public static SeasonInfo getSeasonInfo() {
        return seasonInfo;
    }

    private static void removeCardsFromPool(List<AbstractCard> cardsToRemove) {
        logger.info("removeCardsFromPool called ===========================>>>>>>> # of cards: " + cardsToRemove.size());

        for (AbstractCard card : cardsToRemove) {
            switch (card.rarity) {
                case COMMON:
                    logger.info("Removing common card from pool:" + card.name);
                    AbstractDungeon.commonCardPool.group.removeIf(c -> c.cardID == card.cardID);
                    break;
                case UNCOMMON:
                    AbstractDungeon.uncommonCardPool.group.removeIf(c -> c.cardID == card.cardID);
                    logger.info("Removing uncommon card from pool:" + card.name);
                    break;
                case RARE:
                    AbstractDungeon.rareCardPool.group.removeIf(c -> c.cardID == card.cardID);
                    logger.info("Removing rare card from pool:" + card.name);
                    break;
                case CURSE:
                    logger.info("Removing curse card from pool:" + card.name);
                    AbstractDungeon.curseCardPool.group.removeIf(c -> c.cardID == card.cardID);
                    break;
                default:
                    logger.warn("Can't remove card " + card.name + " from card pool: " + card.rarity + " is not a supported rarity");
                    break;
            }
        }
    }

    private static void removeCropPowerCardsFromPool(List<AbstractCropPowerCard> cardsToRemove) {
        logger.info("removeCropPowerCardsFromPool called ===========================>>>>>>> # of cards: " + cardsToRemove.size());

        for (AbstractCropPowerCard card : cardsToRemove) {
            switch (card.rarity) {
                case COMMON:
                    logger.info("Removing common crop power from pool:" + card.name);
                    AbstractDungeon.commonCardPool.group.removeIf(c -> c.cardID == card.cardID);
                    break;
                case UNCOMMON:
                    AbstractDungeon.uncommonCardPool.group.removeIf(c -> c.cardID == card.cardID);
                    logger.info("Removing uncommon crop power from pool:" + card.name);
                    break;
                case RARE:
                    AbstractDungeon.rareCardPool.group.removeIf(c -> c.cardID == card.cardID);
                    logger.info("Removing rare crop power from pool:" + card.name);
                    break;
                default:
                    logger.warn("Can't remove card " + card.name + " from card pool: " + card.rarity + " is not a supported rarity");
                    break;
            }
        }
    }

    public static List<AbstractCropPowerCard> getSeasonalCropCards() {
        logger.info("getSeasonalCropCards called: " + SEASONAL_CROP_CARDS.stream()
            .distinct().map(c -> c.name).collect(Collectors.joining(", ")));
        return Collections.unmodifiableList(SEASONAL_CROP_CARDS.stream().distinct().collect(Collectors.toList()));
    }

    private static Map<Season, List<AbstractCard>> seasonCurseMap = new HashMap<>();

    private static void initializeSeasonCurses() {
        seasonCurseMap.clear();
//        seasonCurseMap.put(Season.AUTUMN, Collections.unmodifiableList(Arrays.asList(new Spoilage())));
        seasonCurseMap.put(Season.AUTUMN, Collections.unmodifiableList(Arrays.asList(new Spoilage())));
        seasonCurseMap.put(Season.WINTER, Collections.unmodifiableList(Arrays.asList(new Frostbite())));
        seasonCurseMap.put(Season.SPRING, Collections.unmodifiableList(Arrays.asList(new Nettles())));
        seasonCurseMap.put(Season.SUMMER, Collections.unmodifiableList(Arrays.asList(new Gnats())));
    }

    public static List<AbstractCard> getSeasonalCurseCards() {
        if (!isSeasonInitialized()) {
            logger.info("getSeasonalCurseCards: season not initialized.");
            return Collections.emptyList();
        }
        return getSeasonalCurseCards(getSeasonInfo().getSeason());
    }


    public static List<AbstractCard> getSeasonalCurseCards(Season season) {
        if (seasonCurseMap.isEmpty()) {
            logger.info("getSeasonalCurseCards initializing curse map.");
            initializeSeasonCurses();
        }

        logger.info("getSeasonalCurseCards called for season. curses: " + seasonCurseMap.get(season).stream()
            .distinct().map(c -> c.name).collect(Collectors.joining(", ")));

        return seasonCurseMap.get(season);
    }

    @Override
    public void receiveEditRelics() {
        logger.info("TheSimpletonMod::receiveEditRelics called ===========================>>>>>>>");

        BaseMod.addRelicToCustomPool(new SpudOfTheInnocent(), AbstractCardEnum.THE_SIMPLETON_BLUE);

        BaseMod.addRelicToCustomPool(new CashCrop(), AbstractCardEnum.THE_SIMPLETON_BLUE);
        BaseMod.addRelicToCustomPool(new GasCan(), AbstractCardEnum.THE_SIMPLETON_BLUE);
        BaseMod.addRelicToCustomPool(new GourdCharm(), AbstractCardEnum.THE_SIMPLETON_BLUE);
        BaseMod.addRelicToCustomPool(new TheHarvester(), AbstractCardEnum.THE_SIMPLETON_BLUE);
        BaseMod.addRelicToCustomPool(new HornOfPlenty(), AbstractCardEnum.THE_SIMPLETON_BLUE);
        BaseMod.addRelicToCustomPool(new HotPotato(), AbstractCardEnum.THE_SIMPLETON_BLUE);
        BaseMod.addRelicToCustomPool(new OnionBelt(), AbstractCardEnum.THE_SIMPLETON_BLUE);
        BaseMod.addRelicToCustomPool(new PicklingJar(), AbstractCardEnum.THE_SIMPLETON_BLUE);
        BaseMod.addRelicToCustomPool(new PlanterBox(), AbstractCardEnum.THE_SIMPLETON_BLUE);
        BaseMod.addRelicToCustomPool(new NightSoil(), AbstractCardEnum.THE_SIMPLETON_BLUE);
        BaseMod.addRelicToCustomPool(new SpudOfTheMartyr(), AbstractCardEnum.THE_SIMPLETON_BLUE);
        BaseMod.addRelicToCustomPool(new WoodChipper(), AbstractCardEnum.THE_SIMPLETON_BLUE);
        BaseMod.addRelicToCustomPool(new PaperCrane(), AbstractCardEnum.THE_SIMPLETON_BLUE);
    }

    @Override
    public void receivePostCreateStartingDeck(AbstractPlayer.PlayerClass playerClass, CardGroup group) {
        logger.info("receivePostCreateStartingDeck UnlockTracker.getUnlockLevel(): " + UnlockTracker.getUnlockLevel(TheSimpletonCharEnum.THE_SIMPLETON));
        logger.info("receivePostCreateStartingDeck UnlockTracker.getCurrentProgress(): " + UnlockTracker.getCurrentProgress(TheSimpletonCharEnum.THE_SIMPLETON));
        logger.info("receivePostCreateStartingDeck UnlockTracker.getCurrentScoreCost(): " + UnlockTracker.getCurrentScoreCost(TheSimpletonCharEnum.THE_SIMPLETON));
        logger.info("receivePostCreateStartingDeck UnlockTracker.getCurrentScoreTotal(): " + UnlockTracker.getCurrentScoreTotal(TheSimpletonCharEnum.THE_SIMPLETON));
        logger.info("receivePostCreateStartingDeck UnlockTracker.getCompletionPercentage(): " + UnlockTracker.getCompletionPercentage());

        if (isPlayingAsSimpleton()) {
            logger.info("TheSimpletonMod.receivePostCreateStartingDeck receivePostCreateStartingDeck called");

            if (playerClass != TheSimpletonCharEnum.THE_SIMPLETON) {
                return;
            }
            logger.info("TheSimpletonMod.receivePostCreateStartingDeck initializing Season");

            initializeSeason();

            logger.info("TheSimpletonMod.receivePostCreateStartingDeck adding seasonal cards to card pool");

            for (AbstractCropPowerCard card : getSeasonalCropCards()) {
                addToCardPool(card);
            }

            for (AbstractCard card : getSeasonalCurseCards()) {
                addToCardPool(card);
            }

            TheSimpletonMod.removeUnusedCropPowerCardsFromPool();

            Optional<TheSimpletonCharEnum.Theme> themeToApply = currentTheme.getCurrentTheme();
            if (!themeToApply.isPresent()) {
                return;
            }

            try {
                group.clear();
                themeToApply.get().getStartingDeck().forEach(card -> group.addToTop(card));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void receivePostCreateStartingRelics(AbstractPlayer.PlayerClass playerClass, ArrayList<String> relics) {
        if (playerClass != TheSimpletonCharEnum.THE_SIMPLETON) {
            return;
        }
        Optional<TheSimpletonCharEnum.Theme> themeToApply = currentTheme.getCurrentTheme();
        if (!themeToApply.isPresent()) {
            return;
        }

        try {
            relics.clear();
            relics.add(themeToApply.get().getStartingRelicId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO: introduce class to handle this

    static List<AbstractCard> deckBeforeShuffle = new ArrayList<>();
    private static List<AbstractCard> getDeckBeforeShuffle() {
        List<AbstractCard> deckBefore = AbstractDungeon.player.drawPile.group;
        return deckBefore;
    }

    public static List<AbstractCard> getSaveCardPool() {
        return CUSTOM_SAVABLES.cardPoolSavable.getCardPool();
    }

    public static void handleUseCard(AbstractCard card, UseCardAction action) {
        logger.info("TheSimpletonMod.handleUseCard called for card: " + card.name);

        AbstractCrop.getActiveCrops().forEach(crop -> {
            logger.info("TheSimpletonMod.handleUseCard triggering: " + crop.getName() + " for " + card.name);
            crop.onUseCard(card, action);
        });
    }

    public static void updateCardsOnHarvest() {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractDungeon.player.hand.group.stream()
                .filter(c -> c instanceof HarvestTriggeredCard)
                .forEach(c -> ((HarvestTriggeredCard)c).harvestedTrigger());

            AbstractDungeon.player.discardPile.group.stream()
                .filter(c -> c instanceof HarvestTriggeredCard)
                .forEach(c -> ((HarvestTriggeredCard)c).harvestedTrigger());

            AbstractDungeon.player.drawPile.group.stream()
                .filter(c -> c instanceof HarvestTriggeredCard)
                .forEach(c -> ((HarvestTriggeredCard)c).harvestedTrigger());
        }
    }

    public static void handleEmptyDrawShuffleBefore() {
        deckBeforeShuffle = new ArrayList<>(getDeckBeforeShuffle());
    }

    public static void handleEmptyDrawShuffleAfter() {
        handleEmptyDrawShuffleTriggeredCardsAfter();

        // in this case, cards are shuffled back one-at-a-time into the deck, so we need to update the list each time
        deckBeforeShuffle = new ArrayList<>(getDeckBeforeShuffle());
    }

    public static void handleOtherShuffleBefore() {
        deckBeforeShuffle = new ArrayList<>(getDeckBeforeShuffle());
    }

    public static void handleOtherShuffleAfter() {
        handleOtherShuffleTriggeredCardsAfter();
    }

    private static void handleEmptyDrawShuffleTriggeredCardsAfter() {
        final List<AbstractCard> deckAfterShuffle = AbstractDungeon.player.drawPile.group;

        List<AbstractCard> newlyShuffledCards = deckAfterShuffle.stream()
            .filter(c -> !deckBeforeShuffle.contains(c))
            .collect(Collectors.toList());

        newlyShuffledCards.stream()
            .filter(c -> c instanceof ShuffleTriggeredCard)
            .forEach(c -> {
                logger.info("handleEmptyDrawShuffleTriggeredCardsAfter :: Triggering willBeShuffledTrigger for " + c.name);
                ((ShuffleTriggeredCard) c).willBeShuffledTrigger();
            });
    }

    private static void handleOtherShuffleTriggeredCardsAfter() {
        final List<AbstractCard> deckAfterShuffle = AbstractDungeon.player.drawPile.group;

        List<AbstractCard> newlyShuffledCards = deckAfterShuffle.stream()
            .filter(c -> !deckBeforeShuffle.contains(c))
            .collect(Collectors.toList());

        newlyShuffledCards.stream()
            .filter(c -> c instanceof ShuffleTriggeredCard)
            .forEach(c -> {
                logger.info("handleOtherShuffleTriggeredCardsAfter :: Triggering willBeShuffledTrigger for " + c.name);
                ((ShuffleTriggeredCard) c).willBeShuffledTrigger();
            });
    }

    public static String cardListToString(List<AbstractCard> list) {
        return list.stream().map(c -> c.name).collect(Collectors.joining(", "));
    }


    static class ConfigData {
        public static int unlockLevelLastRun = 0;
    }

    private void loadConfigData() {
        try {
            SpireConfig config = new SpireConfig(MOD_NAME, CONFIG_NAME, theSimpletonProperties);
            config.load();
            Arrays.stream(TheSimpletonCharEnum.Theme.values())
                    .forEach(theme -> currentTheme.update(theme, config.getBool(theme.toString())));

            ConfigData.unlockLevelLastRun = config.getInt(ConfigKeys.UNLOCK_LEVEL_LAST_RUN);

            logger.info("loadConfigData :: read config value from save: unlockLevelLastRun: " + ConfigData.unlockLevelLastRun);
        } catch (Exception e) {
            e.printStackTrace();
            saveConfigData(false);
        }
    }

    // TODO: move to config initialize helper method, if possible
    static {
        theSimpletonProperties.setProperty(ConfigKeys.UNLOCK_LEVEL_LAST_RUN, "0");
    }

    static class ConfigKeys {
        public static final String UNLOCK_LEVEL_LAST_RUN = "UnlockLevelLastRun";
    }

    private void saveConfigData(boolean updateLastUnlockLevel) {
        try {
            SpireConfig config = new SpireConfig(MOD_NAME, CONFIG_NAME, theSimpletonProperties);

            Arrays.stream(TheSimpletonCharEnum.Theme.values())
                    .forEach(theme -> config.setBool(theme.toString(), false));
            currentTheme.getCurrentTheme().ifPresent(theme -> config.setBool(theme.toString(), true));

//            config.setInt(ConfigKeys.UNLOCK_LEVEL_LAST_RUN,
//                updateLastUnlockLevel ?
//                    UnlockTracker.getUnlockLevel(TheSimpletonCharEnum.THE_SIMPLETON) : ConfigData.unlockLevelLastRun);

            if (updateLastUnlockLevel) {
                config.setInt(ConfigKeys.UNLOCK_LEVEL_LAST_RUN, UnlockTracker.getUnlockLevel(TheSimpletonCharEnum.THE_SIMPLETON));
            } else {
                config.setInt(ConfigKeys.UNLOCK_LEVEL_LAST_RUN, ConfigData.unlockLevelLastRun);
            }

            logger.info("saveConfigData :: setting config value unlockLevelLastRun: "
                + ConfigData.unlockLevelLastRun + " (uploadLastUnlockLevel: " + updateLastUnlockLevel + ")");

            config.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getLanguageString() {
        // Note to translators - add your language here (by alphabetical order).
        switch (Settings.language) {
            default:
                return "eng";
        }
    }

    @Override
    public void receiveEditStrings() {
        String language = getLanguageString();
        String l10nPath = "TheSimpletonMod/localization/";
        BaseMod.loadCustomStringsFile(RelicStrings.class, l10nPath + language + "/RelicStrings.json");
        BaseMod.loadCustomStringsFile(CardStrings.class, l10nPath + language + "/CardStrings.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, l10nPath + language + "/PowerStrings.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, l10nPath + language + "/UIStrings.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, l10nPath + language + "/CharacterStrings.json");
        BaseMod.loadCustomStringsFile(PotionStrings.class, l10nPath + language + "/PotionStrings.json");
        BaseMod.loadCustomStringsFile(OrbStrings.class, l10nPath + language + "/OrbStrings.json");
        BaseMod.loadCustomStringsFile(EventStrings.class, l10nPath + language + "/EventStrings.json");

    }

    @Override
    public void receiveEditKeywords() {
        final Gson gson = new Gson();
        String language = getLanguageString();

        String keywordStrings =
                Gdx.files.internal("TheSimpletonMod/localization/" + language + "/KeywordStrings.json")
                        .readString(String.valueOf(StandardCharsets.UTF_8));
        Type typeToken = new TypeToken<Map<String, Keyword>>() {
        }.getType();

        keywords = gson.fromJson(keywordStrings, typeToken);
        keywords.forEach((k, v) -> {
            BaseMod.addKeyword(MOD_NAME, v.PROPER_NAME, v.NAMES, v.DESCRIPTION);
        });
    }

    public static Keyword getKeyword(String key) {
       return keywords.get(key);
    }

    public static final SeasonScreen seasonScreen = new SeasonScreen();
    public static SeasonIndicator seasonIndicator;

    private static HashMap<String, Texture> imgMap;
    private static HashMap<String, Texture> getImgMap() {
        if (imgMap == null) {
            imgMap = new HashMap<>();
        }
        return imgMap;
    };

    public static Texture loadTexture(String path) {
        logger.info("TheSimpletonMod::loadTexture loading texture: " + path);

        HashMap<String, Texture> imgMap = getImgMap();
        if (!imgMap.containsKey(path)) {
            Texture img = ImageMaster.loadImage(path);
            logger.info("TheSimpletonMod::adding texture to map: " + img);
            imgMap.put(path, img);
        } else {
            logger.info("TheSimpletonMod::loadTexture already in imgMap: " + path);

        }
        return imgMap.get(path);
    }
}
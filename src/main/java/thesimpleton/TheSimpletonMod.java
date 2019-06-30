package thesimpleton;

import basemod.*;
import basemod.abstracts.CustomUnlockBundle;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
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
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.relics.PaperCrane;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.AbstractUnlock;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thesimpleton.cards.HarvestTriggeredCard;
import thesimpleton.cards.ShuffleTriggeredCard;
import thesimpleton.cards.attack.*;
import thesimpleton.cards.curse.Nettles;
import thesimpleton.cards.power.*;
import thesimpleton.cards.power.crop.*;
import thesimpleton.cards.skill.*;
import thesimpleton.characters.TheSimpletonCharacter;
import thesimpleton.crops.AbstractCrop;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.enums.TheSimpletonCharEnum;
import thesimpleton.events.BorealisEvent;
import thesimpleton.events.EarlyThawEvent;
import thesimpleton.potions.AbundancePotion;
import thesimpleton.relics.*;
import thesimpleton.savedata.CardPoolCustomSavable;
import thesimpleton.savedata.SeasonCustomSavable;
import thesimpleton.seasons.RandomSeasonCropSetDefinition;
import thesimpleton.seasons.Season;
import thesimpleton.seasons.SeasonInfo;
import thesimpleton.seasons.UnlockableSeasonCropSetDefinition;
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
        logger.debug("TheSimpletonMod::receivePostBattle isSeasonInitialized:"
            + isSeasonInitialized() + "; isDebug: " + Settings.isDebug);

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
        seasonInfo = null;
        CUSTOM_SAVABLES.seasonCustomSavable.reset();
        seasonScreen.reset();
        SEASONAL_CROP_CARDS.clear();
    }
//
//    @Override
//    public void receivePostEnergyRecharge() {
//        logger.debug("TheSimpletonMod::receivePostEnergyRecharge : resetting hasHarvestedThisTurn");
//        AbstractCrop.resetHasHarvestedThisTurn();
//    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        logger.debug("TheSimpletonMod::receiveOnBattleStart : resetting hasHarvestedThisTurn");
        AbstractCrop.resetHasHarvestedThisTurn();
    }

    public static void onBeforeStartOfTurnOrbs() {
        logger.debug("TheSimpletonMod::onBeforeStartOfTurnOrbs : resetting hasHarvestedThisTurn");

        logger.debug(">>>>>>>>>> DEBUG <<<<<<<<<< TheSimpletonMod::onBeforeStartOfTurnOrbs unlockLevel: " + UnlockTracker.getUnlockLevel(AbstractDungeon.player.chosenClass));


        AbstractCrop.resetHasHarvestedThisTurn();
    }

    public static boolean isPlayingAsSimpleton() {
        return  AbstractDungeon.player != null && AbstractDungeon.player.chosenClass == TheSimpletonCharEnum.THE_SIMPLETON;
    }

    private static class CUSTOM_SAVABLES {
        static CardPoolCustomSavable cardPoolSavable = new CardPoolCustomSavable();
        static SeasonCustomSavable seasonCustomSavable = new SeasonCustomSavable();
    }

    @Override
    public void receiveStartAct() {
        logger.debug("TheSimpletonMod::receiveStartAct receiveStartAct called ===========================>>>>>>>");
        logger.debug("TheSimpletonMod::receiveStartAct Doing nothing though.");
    }

    @Override
    public void receivePostDungeonInitialize() {
        logger.debug("TheSimpletonMod::receivePostDungeonInitialize receivePostDungeonInitialize called ===========================>>>>>>>");
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
            logger.debug("ThemeState::isCurrentTheme called for theme: " + theme + "; isCurrentTheme: " + currentTheme.equals(theme));
            return this.getCurrentTheme().map(cur -> cur.equals(theme)).orElse(false);
        }
    }

    public static final Logger logger = LogManager.getLogger(TheSimpletonMod.class.getName());

    public TheSimpletonMod() {
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

        BaseMod.addSaveField(CUSTOM_SAVABLES.cardPoolSavable.getCustomSaveKey(), CUSTOM_SAVABLES.cardPoolSavable);
        BaseMod.addSaveField(CUSTOM_SAVABLES.seasonCustomSavable.getCustomSaveKey(), CUSTOM_SAVABLES.seasonCustomSavable);

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
        logger.debug("TheSimpletonMod::receivePostInitialize called ===========================>>>>>>>");

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
                    logger.debug("Adding theme: " + theme);
                    modPanel.addUIElement(new ModLabeledToggleButton(
                        CardCrawlGame.languagePack.getUIString(theme.toString()).TEXT[0],
                        350.0f, 650.0f - 50.f * theme.ordinal(), Settings.CREAM_COLOR,
                        FontHelper.charDescFont, currentTheme.isCurrentTheme(theme), modPanel,
                        label -> {},
                        button -> {
                            currentTheme.update(theme, button.enabled);
                            saveConfigData();
                        })
                    );}
                );

        // TODO: determine events based on season
        BaseMod.addEvent(BorealisEvent.ID, BorealisEvent.class, Exordium.ID);
        BaseMod.addEvent(EarlyThawEvent.ID, EarlyThawEvent.class, Exordium.ID);


        BaseMod.registerModBadge(
                badgeTexture, "The Hayseed", "jwoolley",
                "Adds a new creature to the game - The Hayseed", modPanel);

        BaseMod.addPotion(
            AbundancePotion.class, AbundancePotion.BASE_COLOR, AbundancePotion.HYBRID_COLOR,
                AbundancePotion.SPOTS_COLOR, AbundancePotion.POTION_ID, TheSimpletonCharEnum.THE_SIMPLETON);

        HashMap<String, Sfx> reflectedMap = getSoundsMap();

        reflectedMap.put("ATTACK_SCYTHE_1",
            new Sfx("TheSimpletonMod/sounds/TheSimpleton_AttackScythe1.ogg"));
        reflectedMap.put("ATTACK_BUZZ_1",
            new Sfx("TheSimpletonMod/sounds/TheSimpleton_Buzz1.ogg"));
        reflectedMap.put("ATTACK_FIRE_IMPACT_1",
            new Sfx("TheSimpletonMod/sounds/TheSimpleton_ImpactFire1.ogg"));
        reflectedMap.put("ATTACK_FIRE_IMPACT_2",
            new Sfx("TheSimpletonMod/sounds/TheSimpleton_ImpactFire2.ogg"));
    }

    private void registerCustomSaveKeys() {

    }

    @Override
    public void receiveStartGame() {
        logger.debug("TheSimpletonMod::receiveStartGame called ===========================>>>>>>>");
        isGameInitialized = true;

        Season savedSeason = CUSTOM_SAVABLES.seasonCustomSavable.getSeason();

        Season season;
        if (savedSeason != null) {
            logger.debug("TheSimpletonMod::receiveStartGame applying season from save");
            seasonInfo = new SeasonInfo(savedSeason, RandomSeasonCropSetDefinition.RANDOM_CROP_SET_BY_RARITY);
            season = savedSeason;
        } else if (isSeasonInitialized()) {
            logger.debug("TheSimpletonMod::receiveStartGame applying season from seasonInfo (game start)");

            season = seasonInfo.getSeason();
        } else {
            logger.debug("TheSimpletonMod::receiveStartGame applying random season");
            season = Season.randomSeason();
        }

        //TODO: initialize SeasonInfo on load from save
        seasonIndicator = SeasonIndicator.getIndicator(season);
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
        cards.add(new CullingStrike());
        cards.add(new DoubleBarrel());
        cards.add(new Fertilaser());
        cards.add(new FlashPasteurize());
        cards.add(new HitTheSack());
        cards.add(new PestManagement());
        cards.add(new Rake());
        cards.add(new RootDown());
        cards.add(new SaltTheEarth());
        cards.add(new SlashAndBurn());
        cards.add(new CloseScrape());
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
        cards.add(new TillTheField());
        cards.add(new ToughenUp());
        cards.add(new VineRipen());

        // Power (6)
        cards.add(new BirdFeeder());
        cards.add(new Biorefinement());
        cards.add(new Fecundity());
        cards.add(new LandGrant());
        cards.add(new Photosynthesis());
        cards.add(new ResistantStrain());
        cards.add(new VolatileFumes());

        // Curse(6)
        cards.add(new Nettles());
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

    @Override
    public void receiveEditCards() {
        //  initializeSeason(); // TODO: NOT SURE IF THIS NEEDS TO BE IN THIS HOOK OR CAN BE ELSEWHERE

        logger.debug("TheSimpletonMod::receiveEditCards called ===========================>>>>>>>");

        for(AbstractCard card : getBaseCardList()) {
            BaseMod.addCard(card);
        }

        for(AbstractCard card : getCropCardList()) {
            BaseMod.addCard(card);
        }
    }

    private static SeasonInfo seasonInfo;

    public static boolean isSeasonInitialized() {
        logger.debug("@@@@@DEBUG@@@@@ TheSimpletonMod::isSeasonInitialized : " + (seasonInfo != null));
        return seasonInfo != null;
    }

    public static Season getSeason() {
        return seasonInfo.getSeason();
    }

//    public void setSeasonFromSave() {
//        seasonInfo = new SeasonInfo(CUSTOM_SAVABLES.seasonCustomSavable.getSeason(), SeasonInfo.RANDOM_CROP_SET_BY_RARE_CROP_RARITY);
//    }

    private void initializeSeason() {
        logger.debug("@@@@@DEBUG@@@@@ TheSimpletonMod::initializeSeason Initializing Season ...");

        seasonInfo = chooseSeason();

        List<AbstractCropPowerCard> seasonalCropCards = chooseSeasonalCropCards(seasonInfo);

        setSeasonalCropCards(seasonalCropCards);
        if (seasonIndicator != null) {
            seasonIndicator.reset();
        }

        CUSTOM_SAVABLES.seasonCustomSavable.reset();
    }

    public static void setSeasonalCropCards(List<AbstractCropPowerCard> cards) {
        logger.debug("@@@@@DEBUG@@@@@ Generating season info ...");
        SEASONAL_CROP_CARDS.clear();
        SEASONAL_CROP_CARDS.addAll(cards);
    }

    private SeasonInfo chooseSeason() {
        switch (UnlockTracker.getUnlockLevel(AbstractDungeon.player.chosenClass)) {
            case 0:
                return new SeasonInfo(UnlockableSeasonCropSetDefinition.UNLOCK_CROP_SET_0);
            case 1:
                return new SeasonInfo(UnlockableSeasonCropSetDefinition.UNLOCK_CROP_SET_1);
            case 2:
                return new SeasonInfo(UnlockableSeasonCropSetDefinition.UNLOCK_CROP_SET_2);
            case 3:
                return new SeasonInfo(UnlockableSeasonCropSetDefinition.UNLOCK_CROP_SET_3);
            default:
                return new SeasonInfo(Season.randomSeason(), RandomSeasonCropSetDefinition.RANDOM_CROP_SET_BY_RARE_CROP_RARITY);
        }
    }

    private List<AbstractCropPowerCard> chooseSeasonalCropCards(SeasonInfo seasonInfo) {
        logger.debug("@@@@@DEBUG@@@@@ Generating season info ...");
        logger.debug("SeasonInfo | "
            + "season: " + seasonInfo.getSeason()
            + " cropsInSeason: "
            + seasonInfo.getCropsInSeason().stream().map(c -> c.getName()).collect(Collectors.joining(", "))
            + "\n\n"
        );
        return seasonInfo.getCropsInSeason().stream()
            .map(crop -> crop.getCropInfo().powerCard)
            .collect(Collectors.toList());
    }

    public static List<AbstractCard> getCurrentCardPool() {
        List<AbstractCard> cardPool = new ArrayList<>();
        cardPool.addAll(AbstractDungeon.commonCardPool.group);
        cardPool.addAll(AbstractDungeon.uncommonCardPool.group);
        cardPool.addAll(AbstractDungeon.rareCardPool.group);
        return Collections.unmodifiableList(cardPool);
    }

    public static void addToCardPool(AbstractCard card) {
        switch (card.rarity) {
            case COMMON:
                logger.debug("Adding common crop power to pool:" + card.name);
                // TODO: check for and don't add duplicates
                if (AbstractDungeon.commonCardPool.group.stream().noneMatch(c -> c.cardID == card.cardID)) {
                    AbstractDungeon.commonCardPool.group.add(card);
                }
                break;
            case UNCOMMON:
                if (AbstractDungeon.uncommonCardPool.group.stream().noneMatch(c -> c.cardID == card.cardID)) {
                    AbstractDungeon.uncommonCardPool.group.add(card);
                    logger.debug("Adding uncommon crop power to pool:" + card.name);
                }
                break;
            case RARE:
                if (AbstractDungeon.rareCardPool.group.stream().noneMatch(c -> c.cardID == card.cardID)) {
                    AbstractDungeon.rareCardPool.group.add(card);
                    logger.debug("Adding rare crop power to pool:" + card.name);
                }
                break;
            default:
                logger.warn("Can't add card " + card.name + " to card pool: " + card.rarity + " is not a supported rarity");
                break;
        }
    }

    public static void removeUnusedCropPowerCardsFromPool() {
        List<AbstractCropPowerCard> seasonalCrops = getSeasonalCropCards();

        List<AbstractCropPowerCard> cardsToRemove = getCropCardList().stream()
            .filter(c -> !seasonalCrops.stream().anyMatch(c2 -> c.cardID == c2.cardID))
            .collect(Collectors.toList());

        logger.debug("removeUnusedCropPowerCardsFromPool called. Complete List: " + getCropCardList().stream().map(c -> c.name).collect(Collectors.joining(", ")));
        logger.debug("removeUnusedCropPowerCardsFromPool called. Seasonal Crops: " + getSeasonalCropCards().stream().map(c -> c.name).collect(Collectors.joining(", ")));
        logger.debug("removeUnusedCropPowerCardsFromPool called. Unseasonal Crops: " + cardsToRemove.stream().map(c -> c.name).collect(Collectors.joining(", ")));

        TheSimpletonMod.removeCropPowerCardsFromPool(cardsToRemove);
    }

    public static SeasonInfo getSeasonInfo() {
        return seasonInfo;
    }

    private static void removeCropPowerCardsFromPool(List<AbstractCropPowerCard> cardsToRemove) {
        logger.debug("removeCropPowerCardsFromPool called ===========================>>>>>>> # of cards: " + cardsToRemove.size());



        for (AbstractCropPowerCard card : cardsToRemove) {
            switch (card.rarity) {
                case COMMON:
                    logger.debug("Removing common crop power from pool:" + card.name);
                    AbstractDungeon.commonCardPool.group.removeIf(c -> c.cardID == card.cardID);
                    break;
                case UNCOMMON:
                    AbstractDungeon.uncommonCardPool.group.removeIf(c -> c.cardID == card.cardID);
                    logger.debug("Removing uncommon crop power from pool:" + card.name);
                    break;
                case RARE:
                    AbstractDungeon.rareCardPool.group.removeIf(c -> c.cardID == card.cardID);
                    logger.debug("Removing rare crop power from pool:" + card.name);
                    break;
                default:
                    logger.warn("Can't remove card " + card.name + " from card pool: " + card.rarity + " is not a supported rarity");
                    break;
            }
        }
    }

    public static List<AbstractCropPowerCard> getSeasonalCropCards() {
        logger.debug("getSeasonalCropCards called: " + SEASONAL_CROP_CARDS.stream()
            .distinct().map(c -> c.name).collect(Collectors.joining(", ")));
        return Collections.unmodifiableList(SEASONAL_CROP_CARDS.stream().distinct().collect(Collectors.toList()));
    }

    @Override
    public void receiveEditRelics() {
        logger.debug("TheSimpletonMod::receiveEditRelics called ===========================>>>>>>>");

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
        if (isPlayingAsSimpleton()) {
            logger.debug("TheSimpletonMod.receivePostCreateStartingDeck receivePostCreateStartingDeck called");

            if (playerClass != TheSimpletonCharEnum.THE_SIMPLETON) {
                return;
            }
            logger.debug("TheSimpletonMod.receivePostCreateStartingDeck initializing Season");

            initializeSeason();

            logger.debug("TheSimpletonMod.receivePostCreateStartingDeck adding seasonal cards to card pool");

            for (AbstractCropPowerCard card : getSeasonalCropCards()) {
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
        logger.debug("TheSimpletonMod.handleUseCard called for card: " + card.name);

        AbstractCrop.getActiveCrops().forEach(crop -> {
            logger.debug("TheSimpletonMod.handleUseCard triggering: " + crop.getName() + " for " + card.name);
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
                logger.debug("handleEmptyDrawShuffleTriggeredCardsAfter :: Triggering willBeShuffledTrigger for " + c.name);
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
                logger.debug("handleOtherShuffleTriggeredCardsAfter :: Triggering willBeShuffledTrigger for " + c.name);
                ((ShuffleTriggeredCard) c).willBeShuffledTrigger();
            });
    }

    public static String cardListToString(List<AbstractCard> list) {
        return list.stream().map(c -> c.name).collect(Collectors.joining(", "));
    }

    private void loadConfigData() {
        try {
            SpireConfig config = new SpireConfig(MOD_NAME, CONFIG_NAME, theSimpletonProperties);
            config.load();
            Arrays.stream(TheSimpletonCharEnum.Theme.values())
                    .forEach(theme -> currentTheme.update(theme, config.getBool(theme.toString())));
        } catch (Exception e) {
            e.printStackTrace();
            saveConfigData();
        }
    }

    private void saveConfigData() {
        try {
            SpireConfig config = new SpireConfig(MOD_NAME, CONFIG_NAME, theSimpletonProperties);
            Arrays.stream(TheSimpletonCharEnum.Theme.values())
                    .forEach(theme -> config.setBool(theme.toString(), false));
            currentTheme.getCurrentTheme().ifPresent(theme -> config.setBool(theme.toString(), true));
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
            BaseMod.addKeyword(v.PROPER_NAME, v.NAMES, v.DESCRIPTION);
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
        logger.debug("TheSimpletonMod::loadTexture loading texture: " + path);

        HashMap<String, Texture> imgMap = getImgMap();
        if (!imgMap.containsKey(path)) {
            Texture img = ImageMaster.loadImage(path);
            logger.debug("TheSimpletonMod::adding texture to map: " + img);
            imgMap.put(path, img);
        } else {
            logger.debug("TheSimpletonMod::loadTexture already in imgMap: " + path);

        }
        return imgMap.get(path);
    }
}
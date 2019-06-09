package thesimpleton;

import basemod.*;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PaperCrane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thesimpleton.cards.ShuffleTriggeredCard;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.cards.attack.*;
import thesimpleton.cards.curse.Nettles;
import thesimpleton.cards.power.*;
import thesimpleton.cards.power.crop.*;
import thesimpleton.cards.skill.*;
import thesimpleton.characters.TheSimpletonCharacter;
import thesimpleton.crops.AbstractCrop;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.enums.TheSimpletonCharEnum;
import thesimpleton.potions.AbundancePotion;
import thesimpleton.relics.*;
import thesimpleton.relics.seasons.AbstractSeasonRelic;
import thesimpleton.relics.seasons.AutumnSeasonRelic;
import thesimpleton.seasons.Season;
import thesimpleton.seasons.SeasonInfo;
import thesimpleton.ui.seasons.SeasonScreen;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


@SpireInitializer
public class TheSimpletonMod implements EditCardsSubscriber, EditCharactersSubscriber, EditRelicsSubscriber,
        EditStringsSubscriber, EditKeywordsSubscriber, PostInitializeSubscriber, PostCreateStartingDeckSubscriber,
        PostCreateStartingRelicsSubscriber, PostDungeonInitializeSubscriber, StartActSubscriber  {
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

    private Map<String, Keyword> keywords;
    private ThemeState currentTheme;
    private static final List<AbstractCropPowerCard> SEASONAL_CROP_CARDS = new ArrayList<>();
    private static final  List<AbstractCard> cardPoolFromSave = new ArrayList<>();

    private static AutumnSeasonRelic autumnSeasonRelic;
    private static AbstractSeasonRelic seasonRelic;

    @Override
    public void receiveStartAct() {
        logger.debug("TheSimpletonMod::receiveStartAct receiveStartAct called ===========================>>>>>>>");
        logger.debug("TheSimpletonMod::receiveStartAct Doing nothing though.");

        // TODO: change current screen state rather than forcing update here
//
//        currentTheme.update(TheSimpletonCharEnum.Theme.SEASON_THEME, true);
//        seasonScreen.open();
        // when closed, call currentTheme.update(TheSimpletonCharEnum.Theme.SEASON_THEME, false); // I think?
    }

    @Override
    public void receivePostDungeonInitialize() {
        logger.debug("TheSimpletonMod::receivePostDungeonInitialize receivePostDungeonInitialize called ===========================>>>>>>>");
//        initializeSeason();
        seasonScreen.reset();
    }
//
//    @Override
//    public void receivePreStartGame() {
//        loadCardPool();
//    }

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

    @Override
    public void receivePostInitialize() {
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

        BaseMod.registerModBadge(
                badgeTexture, "The Hayseed", "jwoolley",
                "Adds a new character to the game - The Hayseed", modPanel);

        BaseMod.addPotion(
            AbundancePotion.class, AbundancePotion.BASE_COLOR, AbundancePotion.HYBRID_COLOR,
                AbundancePotion.SPOTS_COLOR, AbundancePotion.POTION_ID, TheSimpletonCharEnum.THE_SIMPLETON);

        HashMap<String, Sfx> reflectedMap = getSoundsMap();
        reflectedMap.put("ATTACK_SCYTHE_1",
            new Sfx("TheSimpletonMod/sounds/TheSimpleton_AttackScythe1.ogg"));

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
        cards.add(new CullingStrike());
        cards.add(new DoubleBarrel());
        cards.add(new Fertilaser());
        cards.add(new FlashPasteurize());
        cards.add(new HitTheSack());
        cards.add(new PestManagement());
        cards.add(new RootDown());
        cards.add(new SaltTheEarth());
        cards.add(new SlashAndBurn());
        cards.add(new Sunchoke());

        // Skill (20)
        cards.add(new Aerate());
        cards.add(new ControlledBurn());
        cards.add(new CropDiversity());
        cards.add(new CropRotation());
        cards.add(new DesperatePlunge());
        cards.add(new DigIn());
        cards.add(new FanTheFlames());
        cards.add(new Ferment());
        cards.add(new GoToMarket());
        cards.add(new Mulch());
        cards.add(new OnionBloom());
        cards.add(new ProtectiveShell());
        cards.add(new Pruning());
        cards.add(new ResearchGrant());
        cards.add(new Rototilling());
        cards.add(new SoilSample());
        cards.add(new SpiceUp());
        cards.add(new StockTheCellar());
        cards.add(new Swelter());
        cards.add(new TillTheField());
        cards.add(new ToughenUp());
        cards.add(new VineRipen());

        // Power (6)
        cards.add(new BirdFeeder());
        cards.add(new Biorefinement());
        cards.add(new Fecundity());
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
        cards.add(new Corn());
        cards.add(new Squash());
        cards.add(new Mushrooms());
        cards.add(new Onions());
        cards.add(new Potatoes());
        cards.add(new Spinach());
        cards.add(new Turnips());

        return cards;
    }

    @Override
    public void receiveEditCards() {

//        initializeSeason(); // TODO: NOT SURE IF THIS NEEDS TO BE IN THIS HOOK OR CAN BE ELSEWHERE


        logger.debug("TheSimpletonMod::receiveEditCards called ===========================>>>>>>>");

        for(AbstractCard card : getBaseCardList()) {
            BaseMod.addCard(card);
        }

        for(AbstractCard card : getCropCardList()) {
            BaseMod.addCard(card);
        }

//        // Basic (4)
//        BaseMod.addCard(new Strike_TheSimpleton());
//        BaseMod.addCard(new Defend_TheSimpleton());
//        BaseMod.addCard(new Haymaker());
//        BaseMod.addCard(new ReapAndSow());
//
//        // Attack (11)
//        BaseMod.addCard(new Barnstorm());
//        BaseMod.addCard(new CullingStrike());
//        BaseMod.addCard(new DoubleBarrel());
//        BaseMod.addCard(new Fertilaser());
//        BaseMod.addCard(new FlashPasteurize());
//        BaseMod.addCard(new HitTheSack());
//        BaseMod.addCard(new PestManagement());
//        BaseMod.addCard(new RootDown());
//        BaseMod.addCard(new SaltTheEarth());
//        BaseMod.addCard(new SlashAndBurn());
//        BaseMod.addCard(new Sunchoke());
//
//        // Skill (20)
//        BaseMod.addCard(new Aerate());
//        BaseMod.addCard(new ControlledBurn());
//        BaseMod.addCard(new CropDiversity());
//        BaseMod.addCard(new CropRotation());
//        BaseMod.addCard(new DesperatePlunge());
//        BaseMod.addCard(new DigIn());
//        BaseMod.addCard(new FanTheFlames());
//        BaseMod.addCard(new Ferment());
//        BaseMod.addCard(new GoToMarket());
//        BaseMod.addCard(new Mulch());
//        BaseMod.addCard(new OnionBloom());
//        BaseMod.addCard(new ProtectiveShell());
//        BaseMod.addCard(new Pruning());
//        BaseMod.addCard(new ResearchGrant());
//        BaseMod.addCard(new Rototilling());
//        BaseMod.addCard(new SoilSample());
//        BaseMod.addCard(new SpiceUp());
//        BaseMod.addCard(new StockTheCellar());
//        BaseMod.addCard(new Swelter());
//        BaseMod.addCard(new TillTheField());
//        BaseMod.addCard(new ToughenUp());
//        BaseMod.addCard(new VineRipen());
//
//        // Power (13)
//        BaseMod.addCard(new BirdFeeder());
//        BaseMod.addCard(new Biorefinement());
//        BaseMod.addCard(new Fecundity());
//        BaseMod.addCard(new Photosynthesis());
//        BaseMod.addCard(new ToughSkin());
//        BaseMod.addCard(new ResistantStrain());
//        BaseMod.addCard(new VolatileFumes());

//        BaseMod.addCard(new Artichokes());
//        BaseMod.addCard(new Asparagus());
//        BaseMod.addCard(new Chilis());
//        BaseMod.addCard(new Corn());
//        BaseMod.addCard(new Squash());
//        BaseMod.addCard(new Mushrooms());
//        BaseMod.addCard(new Onions());
//        BaseMod.addCard(new Potatoes());
//        BaseMod.addCard(new Spinach());
//        BaseMod.addCard(new Turnips());

        // omitting non-purchasable cards
//        BaseMod.addCard(new SpudMissile());
//        BaseMod.addCard(new GiantTurnip());
//        BaseMod.addCard(new Cultivate());

//        setSeasonalCrops();
        // Curse
//        BaseMod.addCard(new Nettles());
    }
//
//    private void setSeasonalCrops() {
//        Season season = Season.randomSeason();
//        SeasonInfo seasonInfo = new SeasonInfo(season, SeasonInfo.RANDOM_CROP_BY_RARITY_STRATEGY);
//
//        logger.debug("@@@@@DEBUG@@@@@ Generating season info ...");
//        logger.debug("SeasonInfo | "
//            + "season: " + seasonInfo.getSeason()
//            + " cropsInSeason: "
//            + seasonInfo.getCropsInSeason().stream().map(c -> c.getName()).collect(Collectors.joining(", "))
//            + "\n\n"
//        );
//        List<AbstractCropPowerCard> cropPowerCards = seasonInfo.getCropsInSeason().stream()
//            .map(crop -> crop.getCropInfo().powerCard)
//            .collect(Collectors.toList());
//
//        SEASONAL_CROP_CARDS.clear();
//        TheSimpletonMod.addCropPowerCardsToPool(cropPowerCards);
//    }

    private void initializeSeason() {
        logger.debug("@@@@@DEBUG@@@@@ TheSimpletonMod::initializeSeason Initializing Season ...");

        SeasonInfo seasonInfo = chooseRandomSeason();
        List<AbstractCropPowerCard> seasonalCropCards = chooseSeasonalCropCards(seasonInfo);

        setSeasonalCropCards(seasonalCropCards);

        setSeasonRelic(AbstractSeasonRelic.getSeasonRelic(seasonInfo.getSeason()));
        equipSeasonalRelic(seasonRelic);

//        List<AbstractCard> cardPool = new ArrayList<>();
//        cardPool.addAll(AbstractDungeon.commonCardPool.group);
//        cardPool.addAll(AbstractDungeon.uncommonCardPool.group);
//        cardPool.addAll(AbstractDungeon.rareCardPool.group);
//        cardPool.addAll(seasonalCropCards);

//        seasonRelic.setCardPool(cardPool);
//        seasonRelic.onEquip(cardPool);
    }

    public static void setSeasonalCropCards(List<AbstractCropPowerCard> cards) {
        logger.debug("@@@@@DEBUG@@@@@ Generating season info ...");
        SEASONAL_CROP_CARDS.clear();
        SEASONAL_CROP_CARDS.addAll(cards);
    }

    private void equipSeasonalRelic(AbstractSeasonRelic relic) {
        RelicLibrary.getRelic(relic.relicId).makeCopy().instantObtain(SimpletonUtil.getPlayer(), 0,false);
        AbstractDungeon.relicsToRemoveOnStart.add(relic.relicId);
    }

    private SeasonInfo chooseRandomSeason() {
        Season season = Season.randomSeason();
        return new SeasonInfo(season, SeasonInfo.RANDOM_CROP_BY_RARITY_STRATEGY);
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

    public static void removeCropPowerCardFromPool(AbstractCropPowerCard card) {
        logger.debug("TheSimpletonMod::removeCropPowerCardFromPool called ===========================>>>>>>>");
        BaseMod.removeCard(card.cardID, AbstractCardEnum.THE_SIMPLETON_BLUE);
        SEASONAL_CROP_CARDS.remove(card);
    }

    public static void removeCropPowerCardsFromPool(List<AbstractCropPowerCard> cards) {
        logger.debug("TheSimpletonMod::removeCropPowerCardsFromPool called ===========================>>>>>>>");

        logger.debug("@@@@@DEBUG@@@@@ ");
        logger.debug("Removing Power Cards: " + cards.stream().map(c -> c.name).collect(Collectors.joining(", ")));

        for (AbstractCropPowerCard card : cards) {
            BaseMod.removeCard(card.cardID, AbstractCardEnum.THE_SIMPLETON_BLUE);
        }

        SEASONAL_CROP_CARDS.removeAll(cards);
    }

    public static void addCropPowerCardsToPool(List<AbstractCropPowerCard> cardsToAdd) {
        logger.debug("addCropPowerCardsToPool called ===========================>>>>>>> # of cards: " + cardsToAdd.size());

        List<AbstractCropPowerCard> cardsToRemove = getCropCardList().stream()
            .filter(c -> !cardsToAdd.stream().anyMatch(c2 -> c.cardID == c2.cardID))
            .collect(Collectors.toList());

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
                    logger.warn("Can't add card " + card.name + " to card pool: " + card.rarity + " is not a supported rarity");
                    break;
            }

            List<AbstractCard> cardPool = new ArrayList<>();
            cardPool.addAll(AbstractDungeon.commonCardPool.group);
            cardPool.addAll(AbstractDungeon.uncommonCardPool.group);
            cardPool.addAll(AbstractDungeon.rareCardPool.group);

            logger.debug("addCropPowerCardsToPool :: cards remaining:");
            int index = 0;
            for (AbstractCard remainingCard : cardPool) {
                logger.debug(index++ + ") " + remainingCard.name + " [cardId: " + remainingCard.cardID + "]");
            }
//            switch (card.rarity) {
//                case COMMON:
//                    logger.debug("Adding common crop power to pool:" + card.name);
//                    AbstractDungeon.commonCardPool.group.add(card);
//                    break;
//                case UNCOMMON:
//                    AbstractDungeon.uncommonCardPool.group.add(card);
//                    logger.debug("Adding uncommon crop power to pool:" + card.name);
//                    break;
//                case RARE:
//                    AbstractDungeon.uncommonCardPool.group.add(card);
//                    logger.debug("Adding rare crop power to pool:" + card.name);
//                    AbstractDungeon.rareCardPool.group.add(card);
//                    break;
//                default:
//                    logger.warn("Can't add card " + card.name + " to card pool: " + card.rarity + " is not a supported rarity");
//                    break;
//            }
        }


    }

    public static void resetCardLibrary() {
        CardLibrary.resetForReload();
        CardLibrary.initialize();
    }

    public static List<AbstractCropPowerCard> getSeasonalCropCards() {
        logger.debug("getSeasonalCropCards called: " + SEASONAL_CROP_CARDS.stream().map(c -> c.name).collect(Collectors.joining(", ")));
        return Collections.unmodifiableList(SEASONAL_CROP_CARDS);
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
        BaseMod.addRelicToCustomPool(new PicklingJar(), AbstractCardEnum.THE_SIMPLETON_BLUE);
        BaseMod.addRelicToCustomPool(new PungentSoil(), AbstractCardEnum.THE_SIMPLETON_BLUE);
        BaseMod.addRelicToCustomPool(new SpudOfTheMartyr(), AbstractCardEnum.THE_SIMPLETON_BLUE);
        BaseMod.addRelicToCustomPool(new WoodChipper(), AbstractCardEnum.THE_SIMPLETON_BLUE);

        autumnSeasonRelic = new AutumnSeasonRelic();
        BaseMod.addRelicToCustomPool(autumnSeasonRelic, AbstractCardEnum.THE_SIMPLETON_BLUE);

        BaseMod.addRelicToCustomPool(new PaperCrane(), AbstractCardEnum.THE_SIMPLETON_BLUE);
    }

    @Override
    public void receivePostCreateStartingDeck(AbstractPlayer.PlayerClass playerClass, CardGroup group) {
        logger.debug("TheSimpletonMod.receivePostCreateStartingDeck receivePostCreateStartingDeck called");

        if (playerClass != TheSimpletonCharEnum.THE_SIMPLETON) {
            return;
        }
        logger.debug("TheSimpletonMod.receivePostCreateStartingDeck initializing Season");

        initializeSeason();

        logger.debug("TheSimpletonMod.receivePostCreateStartingDeck adding seasonal cards to card pool");

        TheSimpletonMod.addCropPowerCardsToPool(getSeasonalCropCards());

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

    public static void handleSaveBefore() {
        logger.debug("TheSimpletonMod.handleSaveBefore called");
        saveCardPool();
        theSimpletonCharacter.getCropUtil().resetForCombatEnd();
    }

    public static boolean playerHasSeasonRelic() {
        return getSeasonRelic() != null;
    }

    public static List<AbstractCard> getSaveCardPool() {
        if (seasonRelic == null || seasonRelic.getCardpool().isEmpty()) {
            return getSeasonRelic().getCardpool();
        }
       return seasonRelic.getCardpool();
    }

    public static AbstractSeasonRelic getSeasonRelic() {
        Optional<AbstractRelic> relicOptional =  SimpletonUtil.getPlayer().relics.stream()
            .filter(r -> r instanceof AbstractSeasonRelic)
            .findFirst();

        if (relicOptional.isPresent()) {
            seasonRelic = (AbstractSeasonRelic)relicOptional.get();
        }
        return seasonRelic;
    }

    public static void setSeasonRelic(AbstractSeasonRelic relic) {
        seasonRelic = relic;
    }

     private static void saveCardPool() {
        logger.debug("TheSimpletonMod.saveCardPool saving card pool. (CURRENTLY DOES NOTHING)");
//        CustomSavable<List<String>> cardpoolSave = new CardPoolSavable(cardPoolFromSave);
//
//        if (BaseMod.getSaveFields().get("simpletonCardPool") == null) {
//            BaseMod.addSaveField("simpletonCardPool", cardpoolSave);
//        }
//        cardpoolSave.onSave();
    }

    private static void loadCardPool() {
        logger.debug("TheSimpletonMod.loadCardPool loading card pool (CURRENTLY DOES NOTHING)");

//        if (!cardpoolSave.getCardpool().isEmpty()) {
//            logger.debug("TheSimpletonMod.loadCardPool found cards. Loading");
//
//            cardPoolFromSave.addAll(cardpoolSave.getCardpool());
//            CardCrawlGame.dungeon.initializeCardPools();
//        } else {
//            logger.debug("TheSimpletonMod.loadCardPool no cards found.");
//        }
    }

    public static void handleUseCard(AbstractCard card, UseCardAction action) {
        logger.debug("TheSimpletonMod.handleUseCard called for card: " + card.name);

        AbstractCrop.getActiveCrops().forEach(crop -> {
            logger.debug("TheSimpletonMod.handleUseCard triggering: " + crop.getName() + " for " + card.name);
            crop.onUseCard(card, action);
        });
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

    public static final SeasonScreen seasonScreen = new SeasonScreen();

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
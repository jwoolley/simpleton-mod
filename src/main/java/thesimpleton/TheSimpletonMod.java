package thesimpleton;

import basemod.BaseMod;
import basemod.ModLabel;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thesimpleton.cards.attack.*;
import thesimpleton.cards.power.*;
import thesimpleton.cards.power.crop.*;
import thesimpleton.cards.skill.*;
import thesimpleton.characters.TheSimpletonCharacter;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.enums.TheSimpletonCharEnum;
import thesimpleton.potions.DregsPotion;
import thesimpleton.relics.*;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;


@SpireInitializer
public class TheSimpletonMod implements EditCardsSubscriber, EditCharactersSubscriber, EditRelicsSubscriber,
        EditStringsSubscriber, EditKeywordsSubscriber, PostInitializeSubscriber, PostCreateStartingDeckSubscriber,
        PostCreateStartingRelicsSubscriber {
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

    private static TheSimpletonCharacter theSimpletonCharacter;
    private static Properties theSimpletonProperties = new Properties();

    private Map<String, Keyword> keywords;
    private ThemeState currentTheme;

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
        loadConfigData();
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
                .forEach(theme ->
                        modPanel.addUIElement(new ModLabeledToggleButton(
                                CardCrawlGame.languagePack.getUIString(theme.toString()).TEXT[0],
                                350.0f, 650.0f - 50.f * theme.ordinal(), Settings.CREAM_COLOR,
                                FontHelper.charDescFont, currentTheme.isCurrentTheme(theme), modPanel,
                                label -> {},
                                button -> {
                                    currentTheme.update(theme, button.enabled);
                                    saveConfigData();
                                })));

        BaseMod.registerModBadge(
                badgeTexture, "The Hayseed", "jwoolley",
                "Adds a new character to the game - The Hayseed", modPanel);

        BaseMod.addPotion(
                DregsPotion.class, Color.BLACK, Color.DARK_GRAY, Color.GRAY, DregsPotion.POTION_ID,
                TheSimpletonCharEnum.THE_SIMPLETON);
    }

    @Override
    public void receiveEditCharacters() {
        theSimpletonCharacter = new TheSimpletonCharacter("The Hayseed");
        BaseMod.addCharacter(
            theSimpletonCharacter, getResourcePath("charSelect/button.png"), getResourcePath("charSelect/portrait.png"),
                TheSimpletonCharEnum.THE_SIMPLETON);
    }

    @Override
    public void receiveEditCards() {
        // Basic (4)
        BaseMod.addCard(new Strike_TheSimpleton());
        BaseMod.addCard(new Defend_TheSimpleton());
        BaseMod.addCard(new CleanUpWorkshop());
        BaseMod.addCard(new Haymaker());
        BaseMod.addCard(new ReapAndSow());

        // Attack (22)
        BaseMod.addCard(new Fertilaser());
        BaseMod.addCard(new RootDown());
        BaseMod.addCard(new CullingStrike());

        // Skill (38)
        BaseMod.addCard(new CropRotation());
        BaseMod.addCard(new OnionBloom());
        BaseMod.addCard(new Aerate());

        // Power (11)
        BaseMod.addCard(new Biorefinement());
        BaseMod.addCard(new Tilling());
        BaseMod.addCard(new ToughSkin());
        BaseMod.addCard(new Corn());
        BaseMod.addCard(new Onions());
        BaseMod.addCard(new Potatoes());
        BaseMod.addCard(new Spinach());
        BaseMod.addCard(new Turnips());

        // omitting non-purchasable cards
//        BaseMod.addCard(new SpudMissile());
//        BaseMod.addCard(new GiantTurnip());
//        BaseMod.addCard(new Harvest());
//        BaseMod.addCard(new RootOut());

//        BaseMod.addCard(new FuryStrike());
//        BaseMod.addCard(new Mangle());
//        BaseMod.addCard(new RavingStaff());
//        BaseMod.addCard(new SoulCrush());
//        BaseMod.addCard(new SoulReap());
//        BaseMod.addCard(new Stigma());
//        BaseMod.addCard(new StunningStrike());
//        BaseMod.addCard(new SurpriseAttack());
//        BaseMod.addCard(new TacticalStrike());
//        BaseMod.addCard(new VitalStrike());
//
//
//        BaseMod.addCard(new BloodBarrier());
//        BaseMod.addCard(new CircleOfAbyss());
//        BaseMod.addCard(new CircleOfBlood());
//        BaseMod.addCard(new CircleOfCorruption());
//        BaseMod.addCard(new CircleOfFlame());
//        BaseMod.addCard(new CircleOfFocus());
//        BaseMod.addCard(new CircleOfWindfury());
//        BaseMod.addCard(new DoubleShield());
//        BaseMod.addCard(new NeowsMight());
//        BaseMod.addCard(new Obscuration());
//        BaseMod.addCard(new ParallelWorld());
//        BaseMod.addCard(new SpreadPlague());
//        BaseMod.addCard(new Stretching());
//        BaseMod.addCard(new StingEye());



//        // Curse
//        BaseMod.addCard(new Dregs());
    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelicToCustomPool(new RedolentSoil(), AbstractCardEnum.THE_SIMPLETON_BLUE);
        BaseMod.addRelicToCustomPool(new TheHarvester(), AbstractCardEnum.THE_SIMPLETON_BLUE);
        BaseMod.addRelicToCustomPool(new SpudOfTheMartyr(), AbstractCardEnum.THE_SIMPLETON_BLUE);
        BaseMod.addRelicToCustomPool(new BlackMagicAdvanced(), AbstractCardEnum.THE_SIMPLETON_BLUE);
        BaseMod.addRelicToCustomPool(new BloodyHarpoon(), AbstractCardEnum.THE_SIMPLETON_BLUE);
        BaseMod.addRelicToCustomPool(new CrystalBall(), AbstractCardEnum.THE_SIMPLETON_BLUE);
        BaseMod.addRelicToCustomPool(new DemonicMark(), AbstractCardEnum.THE_SIMPLETON_BLUE);
        BaseMod.addRelicToCustomPool(new FourLeafCloverCharm(), AbstractCardEnum.THE_SIMPLETON_BLUE);
        BaseMod.addRelicToCustomPool(new MagicCandle(), AbstractCardEnum.THE_SIMPLETON_BLUE);
        BaseMod.addRelicToCustomPool(new PinkPellets(), AbstractCardEnum.THE_SIMPLETON_BLUE);
        BaseMod.addRelicToCustomPool(new OminousMark(), AbstractCardEnum.THE_SIMPLETON_BLUE);
        BaseMod.addRelicToCustomPool(new SoulVessel(), AbstractCardEnum.THE_SIMPLETON_BLUE);
        BaseMod.addRelicToCustomPool(new Tack(), AbstractCardEnum.THE_SIMPLETON_BLUE);
    }

    @Override
    public void receivePostCreateStartingDeck(AbstractPlayer.PlayerClass playerClass, CardGroup group) {
        if (playerClass != TheSimpletonCharEnum.THE_SIMPLETON) {
            return;
        }
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
}
package thesimpleton.characters;

import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import thesimpleton.cards.attack.Haymaker;
import thesimpleton.cards.attack.Strike_TheSimpleton;
import thesimpleton.cards.skill.CleanUpWorkshop;
import thesimpleton.cards.skill.Defend_TheSimpleton;
import thesimpleton.cards.skill.ReapAndSow;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.enums.TheSimpletonCharEnum;
import thesimpleton.relics.SpudOfTheMartyr;
import thesimpleton.relics.TheHarvester;
import thesimpleton.utilities.Trigger;
import thesimpleton.utilities.TriggerManager;

import java.util.ArrayList;
import java.util.List;

import static thesimpleton.TheSimpletonMod.getResourcePath;

public class TheSimpletonCharacter extends CustomPlayer {
    public static final Color CARD_RENDER_COLOR = new Color(0.1F, 0.4F, 0.9F, 1.0F);

    public static final int ENERGY_PER_TURN = 3;
    public static final String SHOULDER_2 = getResourcePath("char/shoulder2.png"); // campfire pose
    public static final String SHOULDER_1 = getResourcePath("char/shoulder.png"); // another campfire pose
    public static final String CORPSE = getResourcePath("char/corpse.png");
    public static final String CHAR_IMAGE = getResourcePath("char/thesimpleton.png");

    private static final CharacterStrings charStrings;
    public static final String NAME;
    public static final String DESCRIPTION;

    private final TriggerManager precombatTriggers = new TriggerManager();
    private final TriggerManager endOfTurnTriggers = new TriggerManager();

    public static final String[] orbTextures = {
            getResourcePath("char/orb/layer1.png"),
            getResourcePath("char/orb/layer2.png"),
            getResourcePath("char/orb/layer3.png"),
            getResourcePath("char/orb/layer4.png"),
            getResourcePath("char/orb/layer5.png"),
            getResourcePath("char/orb/layer6.png"),
            getResourcePath("char/orb/layer1d.png"),
            getResourcePath("char/orb/layer2d.png"),
            getResourcePath("char/orb/layer3d.png"),
            getResourcePath("char/orb/layer4d.png"),
            getResourcePath("char/orb/layer5d.png"),
    };

    public TheSimpletonCharacter(String name) {
        super(name, TheSimpletonCharEnum.THE_SIMPLETON, orbTextures, getResourcePath("char/orb/vfx.png"), null, (String) null);

        this.initializeClass(CHAR_IMAGE, SHOULDER_2, SHOULDER_1, CORPSE, getLoadout(),
                20.0F, -10.0F, 220.0F, 290.0F,
                new EnergyManager(ENERGY_PER_TURN));
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<String>();

        retVal.add(Strike_TheSimpleton.ID);
        retVal.add(Strike_TheSimpleton.ID);
        retVal.add(Strike_TheSimpleton.ID);
        retVal.add(Strike_TheSimpleton.ID);
        retVal.add(Defend_TheSimpleton.ID);
        retVal.add(Defend_TheSimpleton.ID);
        retVal.add(Defend_TheSimpleton.ID);
        retVal.add(Defend_TheSimpleton.ID);
        retVal.add(Haymaker.ID);
        retVal.add(ReapAndSow.ID);

        return retVal;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<String>();

        retVal.add(SpudOfTheMartyr.ID);
        retVal.add(TheHarvester.ID);

        /* for testing
        retVal.add(BlackMagicAdvanced.ID);
        retVal.add(BloodyHarpoon.ID);
        retVal.add(CrystalBall.ID);
        retVal.add(DemonicMark.ID);
        retVal.add(FourLeafCloverCharm.ID);
        retVal.add(MagicCandle.ID);
        retVal.add(OminousMark.ID);
        retVal.add(PinkPellets.ID);
        retVal.add(SoulVessel.ID);
        retVal.add(Tack.ID);
        */

        retVal.forEach(id -> UnlockTracker.markRelicAsSeen(id));
        return retVal;
    }

    public static final int STARTING_HP = 75;
    public static final int MAX_HP = 75;
    public static final int ORB_SLOTS = 0;
    public static final int STARTING_GOLD = 99;
    public static final int HAND_SIZE = 5;

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAME, DESCRIPTION,
                STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, HAND_SIZE,
                this, getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        return NAME;
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return AbstractCardEnum.THE_SIMPLETON_BLUE;
    }

    @Override
    public Color getCardRenderColor() {
        return CARD_RENDER_COLOR;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        if (AbstractDungeon.cardRng.randomBoolean()) {
            return new Haymaker();
        } else {
            return new CleanUpWorkshop();
        }
    }

    @Override
    public Color getCardTrailColor() {
        return CARD_RENDER_COLOR.cpy();
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 4;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontGreen;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("ATTACK_FIRE", MathUtils.random(-0.2F, 0.2F));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, true);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_FIRE";
    }

    @Override
    public String getLocalizedCharacterName() {
        return NAME;
    }

    @Override
    public AbstractPlayer newInstance() {
        return new TheSimpletonCharacter(NAME);
    }

    @Override
    public String getSpireHeartText() {
        return charStrings.TEXT[1];
    }

    @Override
    public Color getSlashAttackColor() {
         return Color.TEAL;
    }

    @Override
    public void applyPreCombatLogic() {
        super.applyPreCombatLogic();
        this.precombatTriggers.triggerAll();
    }

    @Override
    public void applyEndOfTurnTriggers() {
        super.applyEndOfTurnTriggers();
        this.endOfTurnTriggers.triggerAll();
    }

    public void addPrecombatTrigger(Trigger trigger) {
        this.precombatTriggers.addTrigger(trigger);
    }

    public void addEndOfTurnTrigger(Trigger trigger) {
        this.endOfTurnTriggers.addTrigger(trigger);
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.BLUNT_HEAVY, AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY, AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.FIRE, AbstractGameAction.AttackEffect.BLUNT_HEAVY};
    }

    @Override
    public String getVampireText() {
        return com.megacrit.cardcrawl.events.city.Vampires.DESCRIPTIONS[5];
    }

    @Override
    public List<CutscenePanel> getCutscenePanels() {
        List<CutscenePanel> panels = new ArrayList();
        panels.add(new CutscenePanel(getResourcePath("scenes/thesimpleton1.png"), "ATTACK_DEFECT_BEAM"));
        panels.add(new CutscenePanel(getResourcePath("scenes/thesimpleton2.png")));
        panels.add(new CutscenePanel(getResourcePath("scenes/thesimpleton3.png")));
        return panels;
    }

//    @Override shuffle

    static {
        charStrings = CardCrawlGame.languagePack.getCharacterString("TheSimpleton");
        NAME = charStrings.NAMES[0];
        DESCRIPTION = charStrings.TEXT[0];

    }
}
package thesimpleton.characters;

import basemod.abstracts.CustomPlayer;
import basemod.interfaces.StartGameSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.attack.Haymaker;
import thesimpleton.cards.attack.PestManagement;
import thesimpleton.cards.attack.ReapAndSow;
import thesimpleton.cards.attack.Strike_TheSimpleton;
import thesimpleton.cards.skill.Defend_TheSimpleton;
import thesimpleton.cards.skill.Rototilling;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.enums.TheSimpletonCharEnum;
import thesimpleton.orbs.AbstractCropOrb;
import thesimpleton.relics.PungentSoil;
import thesimpleton.relics.SpudOfTheInnocent;
import thesimpleton.relics.TheHarvester;
import thesimpleton.relics.seasons.PlaceholderSeasonRelic;
import thesimpleton.utilities.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static thesimpleton.TheSimpletonMod.getResourcePath;

public class TheSimpletonCharacter extends CustomPlayer implements StartGameSubscriber {
    public static final Color CARD_RENDER_COLOR = new Color(0.1F, 0.4F, 0.9F, 1.0F);

    public static final int ENERGY_PER_TURN = 3;
    public static final String SHOULDER_2 = getResourcePath("char/shoulder2.png"); // campfire pose
    public static final String SHOULDER_1 = getResourcePath("char/shoulder.png"); // another campfire pose
    public static final String CORPSE = getResourcePath("char/corpse.png");
    public static final String CHAR_IMAGE = getResourcePath("char/thesimpleton.png");

    private static final CharacterStrings charStrings;
    public static final String NAME;
    public static final String DESCRIPTION;

    private static final List<TriggerListener> staticPrecombatTriggerListeners = new ArrayList<>();
    private static final List<TriggerListener> staticPrecombatPredrawTriggerListeners = new ArrayList<>();
    private static final List<TriggerListener> staticStartOfTurnTriggerListeners = new ArrayList<>();
    private static final List<TriggerListener> staticEndOfTurnTriggerListeners= new ArrayList<>();
    private static final List<TriggerListener> staticEndOfCombatTriggerListeners = new ArrayList<>();

    private final TriggerManager precombatTriggers;
    private final TriggerManager precombatPredrawTriggers;
    private final TriggerManager startOfTurnTriggers;
    private final TriggerManager endOfTurnTriggers;
    private final TriggerManager endOfCombatTriggers;
    private CropUtil cropUtil;

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

        precombatTriggers = new TriggerManager("PrecombatTriggers", staticPrecombatTriggerListeners);
        precombatPredrawTriggers = new TriggerManager("PrecombatPredrawTriggers", staticPrecombatPredrawTriggerListeners);
        startOfTurnTriggers = new TriggerManager("StartOfTurnTriggers", staticStartOfTurnTriggerListeners);
        endOfTurnTriggers = new TriggerManager("EndOfTurnTriggers", staticEndOfTurnTriggerListeners);
        endOfCombatTriggers = new TriggerManager("EndOfCombatTriggers", staticEndOfCombatTriggerListeners);
        List<AbstractCard> cards = CardLibrary.getAllCards();


        initializeOrbSlotLocations();
    }


    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();

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

        retVal.add(PlaceholderSeasonRelic.ID);

        retVal.add(SpudOfTheInnocent.ID);
        retVal.add(PungentSoil.ID);
        retVal.add(TheHarvester.ID);

        retVal.stream().filter(id -> id != PlaceholderSeasonRelic.ID).forEach(id -> UnlockTracker.markRelicAsSeen(id));

        return retVal;
    }

    public static final int STARTING_HP = 75;
    public static final int MAX_HP = 75;
    public static final int ORB_SLOTS = 3;
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
            return new PestManagement();
        } else {
            return new Rototilling();
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
        CardCrawlGame.sound.playA("RELIC_DROP_FLAT", MathUtils.random(-0.2F, 0.2F));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, true);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "RELIC_DROP_FLAT";
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
    public void applyStartOfCombatLogic() {
        super.applyStartOfCombatLogic();
        TheSimpletonMod.logger.debug("CropUtil: Applying start of combat triggers");
        this.precombatTriggers.triggerAll();
    }

    @Override
    public void applyStartOfCombatPreDrawLogic() {
        super.applyStartOfCombatPreDrawLogic();
        super.applyStartOfTurnCards();

        AbstractPlayer player = AbstractDungeon.player;
        TheSimpletonMod.logger.debug("TheSimpletonCharacter::applyStartOfCombatPreDrawLogic applying precombat-predraw triggers");
        this.precombatPredrawTriggers.triggerAll();
    }

    @Override
    public void applyStartOfTurnCards() {
        super.applyStartOfTurnCards();
        TheSimpletonMod.logger.debug("TheSimpletonCharacter::applyStartOfTurnCards: Applying start of turn triggers");
        this.startOfTurnTriggers.triggerAll();
    }

    @Override
    public void applyEndOfTurnTriggers() {
        super.applyEndOfTurnTriggers();
        TheSimpletonMod.logger.debug("TheSimpletonCharacter::applyEndOfTurnTriggers: Applying end of turn triggers");
        this.endOfTurnTriggers.triggerAll();
    }


    private void resetTriggers() {
        precombatPredrawTriggers.clear(t -> t instanceof CombatLifecycleTriggerListener);
        precombatTriggers.clear(t -> t instanceof CombatLifecycleTriggerListener);
        startOfTurnTriggers.clear(t -> t instanceof CombatLifecycleTriggerListener);
        endOfTurnTriggers.clear(t -> t instanceof CombatLifecycleTriggerListener);
        endOfCombatTriggers.clear(t -> t instanceof CombatLifecycleTriggerListener);
    }

    @Override
    public void onVictory() {
        super.onVictory();
        TheSimpletonMod.logger.debug("TheSimpletonCharacter::onVictory: Applying end of combat triggers");
        this.endOfCombatTriggers.triggerAll();
        TheSimpletonMod.logger.debug("TheSimpletonCharacter::onVictory: Resetting triggers");
        resetTriggers();
    }

    @Override
    public void receiveStartGame() {
        TheSimpletonMod.logger.debug("TheSimpletonCharacter::receiveStartGame: Resetting triggers");
        resetTriggers();
    }

//    @Override
//    public void combatUpdate() {
//        super.combatUpdate();
//        AbstractPlayer player = AbstractDungeon.player;
//        player.masterDeck.group.forEach(card -> {
//            if (card instanceof AbstractCropTriggerCard) {
//                ((AbstractCropTriggerCard) card).getTriggerListener().getTrigger().trigger();
//            }
//        });
//    }


    public CropUtil getCropUtil() {
        if (cropUtil == null) {
            cropUtil = new CropUtil();
        }
        return cropUtil;
    }

    // TODO: make this reusable

    private void addToStartOfTurnTriggerListener(TriggerListener listener) { this.startOfTurnTriggers.addTriggerListener(listener);  }

    private void addToPrecombatTriggerListener(TriggerListener listener) { this.precombatTriggers.addTriggerListener(listener);  }

    private void addToPrecombatPredrawTriggerListener(TriggerListener listener) { this.precombatPredrawTriggers.addTriggerListener(listener);  }

    private void addToEndOfTurnTriggerListener(TriggerListener listener) { this.endOfTurnTriggers.addTriggerListener(listener);  }

    private void addToEndOfCombatTriggerListener(TriggerListener listener) { this.endOfCombatTriggers.addTriggerListener(listener);  }

    public static void addStartOfTurnTrigger(Trigger trigger) {
        addStartOfTurnTriggerListener(() -> trigger);
    }

    public static void addStartOfTurnTriggerListener(TriggerListener listener) {
        AbstractPlayer player = AbstractDungeon.player;
        if (player == null) {
            staticStartOfTurnTriggerListeners.add(listener);
        } else {
            ((TheSimpletonCharacter)player).addToStartOfTurnTriggerListener(listener);
        }
    }

    public static void addPrecombatTrigger(Trigger trigger) {
        addPrecombatTriggerListener(() -> trigger);
    }

    public static void addPrecombatTriggerListener(TriggerListener listener) {
        AbstractPlayer player = AbstractDungeon.player;
        if (player == null) {
            staticPrecombatTriggerListeners.add(listener);
        } else {
            ((TheSimpletonCharacter)player).addToPrecombatTriggerListener(listener);
        }
    }

    public static void addPrecombatPredrawTrigger(Trigger trigger) {
        addPrecombatPredrawTriggerListener(() -> trigger);
    }

    public static void addPrecombatPredrawTriggerListener(TriggerListener listener) {
        AbstractPlayer player = AbstractDungeon.player;
        if (player == null) {
            TheSimpletonMod.logger.debug("CropUtil: Preregistering trigger");
            staticPrecombatPredrawTriggerListeners.add(listener);
        } else {
            ((TheSimpletonCharacter)player).addToPrecombatPredrawTriggerListener(listener);
        }
    }

    public static void addEndOfTurnTrigger(Trigger trigger) {
        addEndOfTurnTriggerListener(() -> trigger);
    }

    public static void addEndOfTurnTriggerListener(TriggerListener listener) {

        AbstractPlayer player = AbstractDungeon.player;
        if (player == null) {
            staticStartOfTurnTriggerListeners.add(listener);
        } else {
            ((TheSimpletonCharacter)player).addToEndOfTurnTriggerListener(listener);
        }
    }

    public static void addEndOfCombatTrigger(Trigger trigger) {
        addEndOfCombatTriggerListener(() -> trigger);
    }
    public static void addEndOfCombatTriggerListener(TriggerListener listener) {

        AbstractPlayer player = AbstractDungeon.player;
        if (player == null) {
            staticStartOfTurnTriggerListeners.add(listener);
        } else {
            ((TheSimpletonCharacter)player).addToEndOfCombatTriggerListener(listener);
        }
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

    // ORB LOGIC

    public float[] orbPositionsX = {0,0,0,0,0,0,0,0,0,0};

    public float[] orbPositionsY = {0,0,0,0,0,0,0,0,0,0};

    public float xStartOffset = (float) Settings.WIDTH * 0.23F;
    private static float xSpaceBetweenSlots = 90 * Settings.scale;
    private static float xSpaceBottomAlternatingOffset = 0 * Settings.scale;

    private static float yStartOffset = AbstractDungeon.floorY + (100 * Settings.scale);

    private static float ySpaceBottomAlternatingOffset = -100 * Settings.scale;
    private static float ySpaceAlternatingOffset = -50 * Settings.scale;

    public void initializeOrbSlotLocations() {
        orbPositionsX[0] = xStartOffset + (xSpaceBetweenSlots * 1);
        orbPositionsX[1] = xStartOffset + (xSpaceBetweenSlots * 1) + xSpaceBottomAlternatingOffset;
        orbPositionsX[2] = xStartOffset + (xSpaceBetweenSlots * 2);
        orbPositionsX[3] = xStartOffset + (xSpaceBetweenSlots * 2) + xSpaceBottomAlternatingOffset;
        orbPositionsX[4] = xStartOffset + (xSpaceBetweenSlots * 3);
        orbPositionsX[5] = xStartOffset + (xSpaceBetweenSlots * 3) + xSpaceBottomAlternatingOffset;
        orbPositionsX[6] = xStartOffset + (xSpaceBetweenSlots * 4);
        orbPositionsX[7] = xStartOffset + (xSpaceBetweenSlots * 4) + xSpaceBottomAlternatingOffset;
        orbPositionsX[8] = xStartOffset + (xSpaceBetweenSlots * 5);
        orbPositionsX[9] = xStartOffset + (xSpaceBetweenSlots * 5) + xSpaceBottomAlternatingOffset;

        orbPositionsY[0] = yStartOffset;
        orbPositionsY[1] = yStartOffset + ySpaceBottomAlternatingOffset;
        orbPositionsY[2] = yStartOffset + ySpaceAlternatingOffset;
        orbPositionsY[3] = yStartOffset + ySpaceBottomAlternatingOffset + ySpaceAlternatingOffset;
        orbPositionsY[4] = yStartOffset;
        orbPositionsY[5] = yStartOffset + ySpaceBottomAlternatingOffset;
        orbPositionsY[6] = yStartOffset + ySpaceAlternatingOffset;
        orbPositionsY[7] = yStartOffset + ySpaceBottomAlternatingOffset + ySpaceAlternatingOffset;
        orbPositionsY[8] = yStartOffset;
        orbPositionsY[9] = yStartOffset + ySpaceBottomAlternatingOffset;
    }

    public void removeOrb(AbstractOrb orb) {
        if ((!this.orbs.isEmpty()) && this.orbs.stream().anyMatch(o -> o.ID == orb.ID)) {

            for (int i = 0; i < this.orbs.size(); i++) {
                AbstractOrb o = this.orbs.get(i);
                TheSimpletonMod.logger.debug("TheSimpletonCharacter.removeOrb :: orbs[" + i + "]: " + o.name + "; amount: " + o.passiveAmount);
            }

            Optional<AbstractOrb> orbOptional  = this.orbs.stream().filter(o -> {
                TheSimpletonMod.logger.debug("TheSimpletonCharacter.removeOrb :: finding : " + orb.name + ". next orb:" + o.name);
                return o instanceof AbstractCropOrb && o.ID == orb.ID;
            }).findFirst();

            if (orbOptional.isPresent()) {
                final AbstractCropOrb targetOrb = (AbstractCropOrb) orbOptional.get();

                final int index = this.orbs.indexOf(targetOrb);

                AbstractOrb orbSlot = new EmptyOrbSlot(((AbstractOrb) this.orbs.get(index)).cX, ((AbstractOrb) this.orbs.get(index)).cY);
                for (int i = index + 1; i < this.orbs.size(); i++) {
                    Collections.swap(this.orbs, i, i - 1);
                }
                this.orbs.set(this.orbs.size() - 1, orbSlot);
                for (int i = 0; i < this.orbs.size(); i++) {
                    ((AbstractOrb) this.orbs.get(i)).setSlot(i, this.maxOrbs);
                }
            }
        }
    }

//    @Override
//    public ArrayList<AbstractCard> getCardPool(ArrayList<AbstractCard> tmpPool) {
//        TheSimpletonMod.logger.debug("TheSimpletonCharacter::getSaveCardPool called");
//        List<AbstractCard> cardPoolFromSave = TheSimpletonMod.getSaveCardPool();
//        if (!cardPoolFromSave.isEmpty()) {
//            ArrayList<AbstractCard> finalPool = new ArrayList<>();
//            TheSimpletonMod.logger.debug("TheSimpletonCharacter::getSaveCardPool using save data");
//            finalPool.addAll(cardPoolFromSave.stream().distinct().filter(tmpPool::contains).collect(Collectors.toSet()));
//            return finalPool;
//        } else {
//            TheSimpletonMod.logger.debug("TheSimpletonCharacter::getSaveCardPool no save data found. Using default pool");
//            ArrayList<AbstractCard> finalPool = super.getCardPool(tmpPool);
//            TheSimpletonMod.getSaveCardPool().addAll(finalPool);
//            return finalPool;
//        }
//    }

//    @Override shuffle

    static {
        charStrings = CardCrawlGame.languagePack.getCharacterString("TheSimpleton");
        NAME = charStrings.NAMES[0];
        DESCRIPTION = charStrings.TEXT[0];
    }
}
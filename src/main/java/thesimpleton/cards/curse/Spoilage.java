package thesimpleton.cards.curse;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.cards.PutSpecifiedCardOnDeckAction;
import thesimpleton.cards.interfaces.IHasCustomGlowCondition;
import thesimpleton.utilities.ModLogger;
import thesimpleton.utilities.SimpletonColorUtil;

import java.util.Iterator;
import java.util.stream.Collectors;

public class Spoilage extends CustomCard implements SeasonalCurse, IHasCustomGlowCondition {
  private static ModLogger logger = TheSimpletonMod.traceLogger;
  public static final String ID = TheSimpletonMod.makeID("Spoilage");
  private static final CardStrings cardStrings;
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "cards/spoilage.png";

  private static final CardType TYPE = CardType.CURSE;
  private static final CardRarity RARITY = CardRarity.CURSE;
  private static final CardTarget TARGET = CardTarget.NONE;
  private static Color GLOW_COLOR = Color.CHARTREUSE.cpy();

  private static final int CARD_THRESHOLD = 3;

  private static final int COST = -2;

  private int numOtherCardsInHand = 0;

  public Spoilage() {
    super(ID, NAME,
        TheSimpletonMod.getImageResourcePath(IMG_PATH), COST, getDescription(false), TYPE, CardColor.CURSE, RARITY,
        TARGET);

    this.baseMagicNumber = this.magicNumber = CARD_THRESHOLD;
  }

  public void triggerOnEndOfPlayerTurn() {
    logger.trace("Spoilage::triggerOnEndOPlayerTurn called");
    if (getNumOtherCardsInHandAtEndOfTurn() >= CARD_THRESHOLD) {
      CardCrawlGame.sound.playAV("MONSTER_SLIME_ATTACK", 0.75F, 1.15F);
      this.superFlash(GLOW_COLOR.cpy());
      AbstractDungeon.actionManager.addToTop(new PutSpecifiedCardOnDeckAction(this));
    }
  }

  @Override
  public void upgrade() {

  }


  public void use(AbstractPlayer p, AbstractMonster m) {  }

  protected void updateDescription(boolean willTrigger) {
    this.rawDescription = getDescription(willTrigger);
    this.initializeDescription();
  }

  public AbstractCard makeCopy() { return new Spoilage(); }

  public static String getDescription(boolean willTrigger) {
    return DESCRIPTION + (willTrigger ? EXTENDED_DESCRIPTION[0] : "");
  }

  public void update() {
    super.update();
    if (AbstractDungeon.player != null && AbstractDungeon.player.hand != null) {
      int latestNumOtherCardsInHand = getNumOtherCardsInHandNow();
      if (latestNumOtherCardsInHand != numOtherCardsInHand) {
        numOtherCardsInHand = latestNumOtherCardsInHand;
        updateDescription(  AbstractDungeon.player.hand.contains(this) && latestNumOtherCardsInHand >= CARD_THRESHOLD);
      }
    }
  }

  public int getNumOtherCardsInHandNow() {
    if (AbstractDungeon.player != null && AbstractDungeon.player.hand != null) {
      return AbstractDungeon.player.hand.group.stream().filter(c -> c != this).collect(Collectors.toList()).size();
    } else {
      return 0;
    }
  }

  public int getNumOtherCardsInHandAtEndOfTurn() {
    // retained cards are placed in limbo during the DiscardAtEndOfTurnAction
    //  (and restored via adding RestoreRetainedCardsAction to top of action queue)
    //  which is when we're counting cards for the end of turn trigger
    int numRetainedCardsInLimbo = 0;
    for (Iterator<AbstractCard> c = AbstractDungeon.player.limbo.group.iterator(); c.hasNext();) {
      AbstractCard e = c.next();
      if (e.retain || e.selfRetain) {
        numRetainedCardsInLimbo++;
      }
    }

    return getNumOtherCardsInHandNow() + numRetainedCardsInLimbo;
  }

  @Override
  public void triggerOnGlowCheck() {
    if (AbstractDungeon.player.hand.contains(this) && shouldGlow()) {
      TheSimpletonMod.traceLogger.log("Spoilage.triggerOnGlowCheck() it's Glow Time");
      this.glowColor = GLOW_COLOR.cpy();
    } else {
      this.glowColor = SimpletonColorUtil.INVISIBLE_GLOW_COLOR.cpy();
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }

  @Override
  public boolean shouldGlow() {
    return  getNumOtherCardsInHandNow() >= CARD_THRESHOLD;
  }
}

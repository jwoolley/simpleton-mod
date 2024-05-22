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
import thesimpleton.utilities.ModLogger;

import java.util.stream.Collectors;

public class Spoilage extends CustomCard implements SeasonalCurse {
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
    if (getNumOtherCardsInHand() >= CARD_THRESHOLD) {
      CardCrawlGame.sound.playAV("MONSTER_SLIME_ATTACK", 0.75F, 1.2F);
      this.superFlash(Color.CHARTREUSE.cpy());
      AbstractDungeon.player.hand.moveToDeck(this, false);
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
    int latestNumOtherCardsInHand = getNumOtherCardsInHand();
    if (latestNumOtherCardsInHand != numOtherCardsInHand) {
      numOtherCardsInHand = latestNumOtherCardsInHand;
      updateDescription(latestNumOtherCardsInHand >= CARD_THRESHOLD);
    }
  }

  public int getNumOtherCardsInHand() {
    return AbstractDungeon.player.hand.group.stream().filter(c -> c != this).collect(Collectors.toList()).size();
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}

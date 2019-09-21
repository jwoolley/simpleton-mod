package thesimpleton.cards.curse;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.SetDontTriggerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;

public class Spoilage extends CustomCard implements SeasonalCurse {
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

  boolean willTrigger = false;

  public Spoilage() {
    super(ID, NAME,
        TheSimpletonMod.getResourcePath(IMG_PATH), COST, getDescription(false), TYPE, CardColor.CURSE, RARITY,
        TARGET);

    this.baseMagicNumber = this.magicNumber = CARD_THRESHOLD;
  }

  public void triggerOnOtherCardPlayed(AbstractCard c) {
    if (AbstractDungeon.player.cardsPlayedThisTurn >= this.magicNumber) {
      if (!this.willTrigger) {
        this.willTrigger = true;
        this.flash(Color.CHARTREUSE.cpy());
        CardCrawlGame.sound.play("POWER_POISON");
        updateDescription(true);
      }
    }
  }

  @Override
  public void triggerOnGainEnergy(int e, boolean b) {
    TheSimpletonMod.logger.debug("Spoilage::triggerOnGainEnergy called");
    updateDescription(false);
  }


  @Override
  public void triggerOnEndOfTurnForPlayingCard() {

  }

  public void triggerOnEndOfPlayerTurn() {
    TheSimpletonMod.logger.debug("Spoilage::triggerOnEndOfTurnForPlayingCard called");

    if (this.willTrigger) {
      AbstractDungeon.player.hand.moveToDeck(this, false);
      this.willTrigger = false;
      updateDescription(false);
    }
  }

  public void use(AbstractPlayer p, AbstractMonster m) {  }

  protected void updateDescription(boolean willBeRetained) {
    this.rawDescription = getDescription(willBeRetained);
    this.initializeDescription();
  }

  public void triggerWhenDrawn() {
    AbstractDungeon.actionManager.addToBottom(new SetDontTriggerAction(this, false));
  }

  public AbstractCard makeCopy() { return new Spoilage(); }

  public static String getDescription(boolean willBeRetained) {
    return DESCRIPTION + (willBeRetained ? EXTENDED_DESCRIPTION[0] : "");
  }

  public void upgrade() {}

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}

package thesimpleton.actions.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import thesimpleton.TheSimpletonMod;
import thesimpleton.utilities.ModLogger;

public class GainCardAction extends AbstractGameAction {
  private static ModLogger logger = TheSimpletonMod.traceLogger;
  private static final ActionType ACTION_TYPE = ActionType.CARD_MANIPULATION;
  private static final float ACTION_DURATION = Settings.ACTION_DUR_XFAST;

  private final AbstractCard cardToGain;
  private final float cardX;
  private final float cardY;



  public GainCardAction(AbstractCard cardToGain) {
    this(cardToGain, 0.0F);
  }


  public GainCardAction(AbstractCard cardToGain, float cardX, float cardY) {
    this(cardToGain, cardX, cardY, 0.0F);
  }

  public GainCardAction(AbstractCard cardToGain, float waitDuration) {
    this(cardToGain,
        Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F * Settings.scale, Settings.HEIGHT / 2.0F,
        waitDuration);
  }

  public GainCardAction(AbstractCard cardToGain, float cardX, float cardY, float waitDuration) {
    logger.trace("GainCardAction::constuctor called");

    setValues(AbstractDungeon.player, AbstractDungeon.player);

    this.cardToGain = cardToGain.makeStatEquivalentCopy();
    this.actionType = ACTION_TYPE;
    this.duration = ACTION_DURATION ;
//    this.duration = ACTION_DURATION + waitDuration;
    this.cardX = cardX;
    this.cardY = cardY;
  }

  public void update() {
    logger.trace("GainCardAction::update duration: " + this.duration);
    if (this.duration <= 0) {
      logger.trace("GainCardAction::update taking action. duration: " + this.duration);
      AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(cardToGain, cardX, cardY));
      this.tickDuration();
      this.isDone = true;
    }
    this.tickDuration();
  }
}
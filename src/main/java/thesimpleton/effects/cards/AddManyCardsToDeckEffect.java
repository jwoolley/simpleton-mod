package thesimpleton.effects.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import thesimpleton.TheSimpletonMod;
import thesimpleton.events.SimpletonEventHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddManyCardsToDeckEffect extends AbstractGameEffect {
  private static final float ACTION_DURATION = Settings.ACTION_DUR_XFAST;
  private static final float DEFAULT_WAIT_DURATION = Settings.ACTION_DUR_MED;

  private final List<AbstractCard> cardsToGain;
  private final float waitBetweenDuration;

  private boolean tickedOnce = false;


  public AddManyCardsToDeckEffect(AbstractCard card1, AbstractCard card2, AbstractCard card3, AbstractCard card4,
                                  AbstractCard card5, AbstractCard card6, AbstractCard card7, AbstractCard card8) {
    this(card1, card2, card3, card4, card5, card6, card7, card8, DEFAULT_WAIT_DURATION);
  }

  public AddManyCardsToDeckEffect(AbstractCard card1, AbstractCard card2, AbstractCard card3, AbstractCard card4,
                                  AbstractCard card5, AbstractCard card6, AbstractCard card7, AbstractCard card8, float waitBetweenDuration) {

    TheSimpletonMod.traceLogger.trace("AddManyCardsToDeckEffect::constructor called");

    this.cardsToGain = new ArrayList<>(Arrays.asList(card1, card2, card3, card4, card5, card6, card7, card8));
    this.duration = ACTION_DURATION + waitBetweenDuration;
    this.waitBetweenDuration = waitBetweenDuration;
  }

  public void update() {
    TheSimpletonMod.traceLogger.trace("AddManyCardsToDeckEffect::update duration: " + this.duration);

    this.duration -= Gdx.graphics.getDeltaTime();
    if (!tickedOnce && duration <= waitBetweenDuration) {
      tickedOnce = true;
      SimpletonEventHelper.gainCards(cardsToGain.get(0), cardsToGain.get(1), cardsToGain.get(2), cardsToGain.get(3));
    } else if (this.duration <= 0.0F) {
      SimpletonEventHelper.gainCards(cardsToGain.get(4), cardsToGain.get(5), cardsToGain.get(6), cardsToGain.get(7));
      this.isDone = true;
      return;
    }
  }

  public void render(SpriteBatch sb) { }

  @Override
  public void dispose() { }
}
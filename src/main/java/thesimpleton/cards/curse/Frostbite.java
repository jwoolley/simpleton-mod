package thesimpleton.cards.curse;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;

import java.util.List;
import java.util.stream.Collectors;

public class Frostbite extends CustomCard implements SeasonalCurse {
  public static final String ID = TheSimpletonMod.makeID("Frostbite");
  private static final CardStrings cardStrings;
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/frostbite.png";

  private static final CardType TYPE = CardType.CURSE;
  private static final CardRarity RARITY = CardRarity.CURSE;
  private static final CardTarget TARGET = CardTarget.NONE;

  private static final int COST_INCREASE_AMOUNT = 1;

  private static final int COST = -2;

  public Frostbite() {
    super(ID, NAME, TheSimpletonMod.getImageResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, CardColor.CURSE, RARITY, TARGET);
  }

  @Override
  public void triggerOnOtherCardPlayed(AbstractCard card) {

  }

  @Override
  public void triggerOnEndOfPlayerTurn() {
    if (AbstractDungeon.player.hand.contains(this)) {
      List<AbstractCard> increasableCards = getIncreasableCards();

      if (increasableCards.size() > 0) {
        increaseCostOfRandomCardForThisCombat(increasableCards, COST_INCREASE_AMOUNT);
      }
    }
  }

  private List<AbstractCard> getIncreasableCards() {
    return AbstractDungeon.player.hand.group.stream()
      .filter(c -> (c.type != null && !c.type.equals(CardType.CURSE)) || AbstractDungeon.player.hasRelic("Blue Candle"))
      .filter(c ->  c.cost >= 0 && !this.equals(c)).collect(Collectors.toList());
  }

  // I guess this is how you do it
  public void use(AbstractPlayer p, AbstractMonster m) {
    if (p.hasRelic("Blue Candle")) {
      this.useBlueCandle(p);
    } else {
      AbstractDungeon.actionManager.addToBottom(new UseCardAction(this));
    }
  }

  private void increaseCostOfRandomCardForThisCombat(List<AbstractCard> increasableCards, int costIncrease) {
    AbstractCard card = increasableCards.get(AbstractDungeon.cardRng.random(increasableCards.size() - 1));

    card.modifyCostForCombat(card.cost + costIncrease);

    // TODO: reimplement as an action & wait a tick between power flash and card flash
    CardCrawlGame.sound.play("ICE_CLINK_1");
    CardCrawlGame.sound.play("ICE_CLINK_1");
    this.flash(Color.SKY.cpy());
    card.superFlash(Color.SKY.cpy());
  }

  public AbstractCard makeCopy() { return new Frostbite(); }

  public void upgrade() {}

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
  }
}
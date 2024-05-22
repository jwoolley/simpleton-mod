package thesimpleton.cards.curse;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.attack.GiantTurnip;
import thesimpleton.cards.status.Depletion;
import thesimpleton.cards.status.IceCube;
import thesimpleton.devtools.debugging.DebugLogger;

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

  private static final int COST = -2;

  private static final AbstractCard PREVIEW_CARD;

  public Frostbite() {
    super(ID, NAME, TheSimpletonMod.getImageResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, CardColor.CURSE, RARITY, TARGET);
    this.cardsToPreview = PREVIEW_CARD;
  }


//  @Override
//  public void triggerOnOtherCardPlayed(AbstractCard card) {
//    if (card.type != CardType.STATUS) {
//      CardCrawlGame.sound.play("ICE_CLINK_1");
//      CardCrawlGame.sound.play("ICE_CLINK_1");
//      this.superFlash(Color.SKY.cpy());
//      AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new IceCube(), 1));
//    }
//  }
  @Override
  public void triggerWhenDrawn() {
      CardCrawlGame.sound.play("ICE_CLINK_1");
      this.superFlash(Color.SKY.cpy());
      AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new IceCube(), 1));
  }

  // I guess this is how you do it
  public void use(AbstractPlayer p, AbstractMonster m) {
    if (p.hasRelic("Blue Candle")) {
      this.useBlueCandle(p);
    } else {
      AbstractDungeon.actionManager.addToBottom(new UseCardAction(this));
    }
  }

  public AbstractCard makeCopy() { return new Frostbite(); }

  public void upgrade() {}

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    PREVIEW_CARD = new IceCube();
  }
}
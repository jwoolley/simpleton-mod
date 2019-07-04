package thesimpleton.cards.curse;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.SlowPower;
import thesimpleton.TheSimpletonMod;
import thesimpleton.powers.LoseSlowPower;

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

  public Frostbite() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, CardColor.CURSE, RARITY, TARGET);
  }

  public void triggerWhenDrawn() {
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
            new SlowPower(AbstractDungeon.player, 0), 0));

    super.triggerWhenDrawn();
      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
              new LoseSlowPower(AbstractDungeon.player), 1));
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
  }
}
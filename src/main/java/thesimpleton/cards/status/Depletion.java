package thesimpleton.cards.status;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawReductionPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import thesimpleton.TheSimpletonMod;
import thesimpleton.powers.DeenergizedPower;

public class Depletion extends CustomCard {
  public static final String ID = "TheSimpletonMod:Depletion";
  private static final CardStrings cardStrings;
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "cards/depletion.png";

  private static final CardType TYPE = CardType.STATUS;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 0;

  private static final int REDUCTION_AMOUNT = 1;

  public Depletion() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, CardColor.COLORLESS, RARITY,
        TARGET);
    this.baseMagicNumber = this.magicNumber = REDUCTION_AMOUNT;
    this.exhaust = true;
  }

  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new DeenergizedPower(p, REDUCTION_AMOUNT)));

    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new DrawReductionPower(p, REDUCTION_AMOUNT)));
  }

  public void triggerWhenDrawn()
  {
    if ((AbstractDungeon.player.hasPower("Evolve")) && (!AbstractDungeon.player.hasPower("No Draw")))
    {
      AbstractDungeon.player.getPower("Evolve").flash();
      AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player,
          AbstractDungeon.player.getPower("Evolve").amount));
    }
  }

  public AbstractCard makeCopy() {
    return new Depletion();
  }

  public void upgrade() {
  }

  @Override
  public boolean canUse(AbstractPlayer p, AbstractMonster m) {
    return true;
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}

package thesimpleton.cards.curse;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.SetDontTriggerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
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

  private static final int NUM_CARDS = 2;
  private static final int COST_INCREASE = 2;

  private static final int COST = -2;

  public Spoilage() {
    super(ID, NAME,
        TheSimpletonMod.getResourcePath(IMG_PATH), COST, getDescription(NUM_CARDS), TYPE, CardColor.CURSE, RARITY,
        TARGET);

    this.baseMagicNumber = this.magicNumber = COST_INCREASE;
  }


  public void use(AbstractPlayer p, AbstractMonster m) {
    if (this.dontTriggerOnUseCard) {
      AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
          new VulnerablePower(AbstractDungeon.player, 1, true), 1));
    }
  }

  public void triggerWhenDrawn() {
    AbstractDungeon.actionManager.addToBottom(new SetDontTriggerAction(this, false));
  }

  public void triggerOnEndOfTurnForPlayingCard() {
    this.dontTriggerOnUseCard = true;
    AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
  }

  public AbstractCard makeCopy() { return new Spoilage(); }

  public String getDescription() {
    return getDescription(this.magicNumber);
  }

  public static String getDescription(int numStacks) {
    return DESCRIPTION
        + (numStacks == 1 ? EXTENDED_DESCRIPTION[0] : + numStacks + EXTENDED_DESCRIPTION[1])
        + EXTENDED_DESCRIPTION[2];
  }

  public void upgrade() {}

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}

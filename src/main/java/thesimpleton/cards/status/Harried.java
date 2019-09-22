package thesimpleton.cards.status;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.powers.DeenergizedPower;
import thesimpleton.powers.DrawDownPower;

public class Harried extends CustomCard {
  public static final String ID = "TheSimpletonMod:Harried";
  private static final CardStrings cardStrings;
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "cards/harried.png";

  private static final CardType TYPE = CardType.STATUS;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = -2;

  private static final int DAMAGE_AMOUNT = 1;

  public Harried() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, CardColor.COLORLESS, RARITY,
        TARGET);
    this.baseMagicNumber = this.magicNumber = DAMAGE_AMOUNT;
    this.isEthereal = true;
  }

  public void use(AbstractPlayer p, AbstractMonster m) { }

  public void triggerWhenDrawn() {
      AbstractDungeon.actionManager.addToBottom(new DamageAction(
            AbstractDungeon.player,
            new DamageInfo(AbstractDungeon.player, this.magicNumber, DamageInfo.DamageType.THORNS),
            AbstractGameAction.AttackEffect.SLASH_DIAGONAL
          ));
  }

  public AbstractCard makeCopy() {
    return new Harried();
  }

  public void upgrade() { }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}

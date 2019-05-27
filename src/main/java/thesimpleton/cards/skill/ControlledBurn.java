package thesimpleton.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.ApplyBurningAction;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.powers.BurningPower;

public class ControlledBurn extends CustomCard {
  public static final String ID = "TheSimpletonMod:ControlledBurn";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/controlledburn.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 1;
  private static final int NUM_STACKS = 14;
  private static final int UPGRADE_NUM_STACKS = 6;

  public ControlledBurn() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = NUM_STACKS;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    final boolean hadBurning = m.hasPower(BurningPower.POWER_ID);
    AbstractDungeon.actionManager.addToBottom(new ApplyBurningAction(m, p, this.magicNumber));
    if (!hadBurning) {
      this.exhaustOnUseOnce = true;
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new ControlledBurn();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      upgradeName();
      upgradeMagicNumber(UPGRADE_NUM_STACKS);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
  }
}
package thesimpleton.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import thesimpleton.TheSimpletonMod;
import thesimpleton.orbs.AbstractCropOrb;
import thesimpleton.powers.utils.Crop;

public class RootOut extends AbstractHarvestCard {
  public static final String ID = "TheSimpletonMod:RootOut";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/rootout.png";

  private static final CardStrings cardStrings;

  private static final AbstractCard.CardType TYPE = CardType.SKILL;
  private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
  private static final AbstractCard.CardTarget TARGET = CardTarget.ALL_ENEMY;

  private static final int COST = 1;
  private static final int VULNERABLE_DURATION = 2;
  private static final int VULNERABLE_DURATION_UPGRADE_BONUS = 3;

  public RootOut() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET,
        1, true, false);
    this.baseMagicNumber = this.magicNumber = VULNERABLE_DURATION;
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    for (final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(mo, p,
              new VulnerablePower(mo, this.magicNumber, false), this.magicNumber));
    }

    if (AbstractCropOrb.hasCropOrb(Crop.TURNIPS)) {
      AbstractCropOrb.getCropOrb(Crop.TURNIPS).getCrop().harvest(true, 1);
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new RootOut();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      upgradeName();
      upgradeMagicNumber(VULNERABLE_DURATION_UPGRADE_BONUS);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
  }
}
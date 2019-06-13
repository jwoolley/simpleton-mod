package thesimpleton.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ModifyBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.enums.AbstractCardEnum;

public class SeedCoat extends CustomCard {
  public static final String ID = "TheSimpletonMod:SeedCoat";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String UPGRADE_DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "cards/seedcoat.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 1;
  private static final int BLOCK = 12;
  private static final int BLOCK_REDUCTION = 6;

  private boolean hasBeenPlayed = false;

  public SeedCoat() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, getDescription(false, false), TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseBlock = this.block = BLOCK;
    this.baseMagicNumber = this.magicNumber = BLOCK_REDUCTION;
    this.isEthereal = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    if (hasBeenPlayed) {
      this.exhaust = true;
    }

    this.hasBeenPlayed = true;

    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));

    if (this.magicNumber >= this.block) {
      AbstractDungeon.actionManager.addToBottom(new ModifyBlockAction(this.uuid, -this.block));
    } else {
      AbstractDungeon.actionManager.addToBottom(new ModifyBlockAction(this.uuid, -this.magicNumber));
    }

    updateDescription();
  }

  @Override
  public AbstractCard makeCopy() {
    return new SeedCoat();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.isEthereal = false;
      this.upgradeName();
      updateDescription();
    }
  }

  protected void updateDescription() {
    this.rawDescription = getDescription(this.hasBeenPlayed, this.upgraded);
    this.initializeDescription();
  }

  private static String getDescription(boolean hasBeenPlayed, boolean isUpgraded) {
    if (hasBeenPlayed) {
      if (!isUpgraded) {
        return EXTENDED_DESCRIPTION[0];
      } else {
        return EXTENDED_DESCRIPTION[1];
      }
    } else {
      if (!isUpgraded) {
        return DESCRIPTION;
      } else {
        return UPGRADE_DESCRIPTION;
      }
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}

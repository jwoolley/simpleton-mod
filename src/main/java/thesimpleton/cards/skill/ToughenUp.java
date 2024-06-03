package thesimpleton.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.CropSpawnAction;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.orbs.AbstractCropOrb;
import thesimpleton.orbs.SquashCropOrb;
import thesimpleton.crops.Crop;

public class ToughenUp extends CustomCard {
  public static final String ID = "TheSimpletonMod:ToughenUp";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/toughenup.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 1;
  private static final int BLOCK = 8;
  private static final int SQUASH_AMOUNT = 1;
  private static final int UPGRADE_SQUASH_AMOUNT = 1;

  public ToughenUp() {
    super(ID, NAME, TheSimpletonMod.getImageResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseBlock = this.block = BLOCK;
    this.baseMagicNumber = this.magicNumber = SQUASH_AMOUNT;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));

    if (AbstractCropOrb.hasCropOrb(Crop.SQUASH)) {
      AbstractDungeon.actionManager.addToBottom(new CropSpawnAction(new SquashCropOrb(), this.magicNumber, true));
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new ToughenUp();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(UPGRADE_SQUASH_AMOUNT);
    }
  }

  public void triggerOnGlowCheck() {
    if (AbstractCropOrb.hasCropOrb(Crop.SQUASH)) {
      this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
    } else {
      this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
  }
}

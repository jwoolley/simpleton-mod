package thesimpleton.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.characters.TheSimpletonCharacter;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.powers.AbstractCropPower;

public class Aerate extends CustomCard {
  public static final String ID = "TheSimpletonMod:Aerate";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "cards/aerate.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 2;
  private static final int BLOCK = 9;
  private static final int UPGRADE_BLOCK_AMOUNT = 4;
  private static final int CROP_INCREASE_AMOUNT = 2;

  public Aerate() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, getDescription(false), TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseBlock = this.block = BLOCK;
    this.baseMagicNumber = this.magicNumber = CROP_INCREASE_AMOUNT;

    Logger logger = TheSimpletonMod.logger;
    if (AbstractDungeon.isPlayerInDungeon()) {
      ((TheSimpletonCharacter) AbstractDungeon.player).getCropUtil().addCropTickedTrigger(() -> {
        logger.debug(("Real-time crop tick trigger triggered"));
        updateDescription();
      });
    } else {
      logger.debug(("AbstractDungeon.player is not currently defined. Registering precombatpredrawtrigger."));
      TheSimpletonCharacter.addPrecombatPredrawTrigger(() -> {
        ((TheSimpletonCharacter) AbstractDungeon.player).getCropUtil().addCropTickedTrigger(() -> {
          logger.debug(("Preloaded crop tick trigger triggered"));
          updateDescription();
        });
      });
    }

    if (AbstractDungeon.isPlayerInDungeon()) {
      updateDescription();
    }
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));

    if (AbstractCropPower.playerHasAnyActiveCropPowers()) {
      AbstractCropPower.getNewestPower().stackPower(this.magicNumber);
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new Aerate();
  }

  private void updateDescription() {
    this.rawDescription = getDescription(true);
    this.initializeDescription();
  }

  private boolean didRegisterDrawnTrigger = false;
  @Override
  public void triggerWhenDrawn() {
    TheSimpletonMod.logger.debug(("Aerate::triggerWhenDrawn"));

    if (!didRegisterDrawnTrigger) {
      TheSimpletonMod.logger.debug(("Aerate::triggerWhenDrawn registering trigger"));
      ((TheSimpletonCharacter) AbstractDungeon.player).getCropUtil().addCropTickedTrigger(() -> {
        TheSimpletonMod.logger.debug(("Aerate::triggerWhenDrawn Trigger-when-drawn crop tick trigger triggered"));
        updateDescription();
      });
      didRegisterDrawnTrigger = true;
    }

    TheSimpletonMod.logger.debug(("Aerate::triggerWhenDrawn  Updating description"));
    this.updateDescription();
  }

  private static String getDescription(boolean checkCropValue) {
    String description = DESCRIPTION;
    AbstractCropPower crop;

    if (!checkCropValue || ((crop = AbstractCropPower.getNewestPower())) == null) {
      description += EXTENDED_DESCRIPTION[0];
    } else {
      description += EXTENDED_DESCRIPTION[1] + crop.name + EXTENDED_DESCRIPTION[2];
    }
    return description;
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeBlock(UPGRADE_BLOCK_AMOUNT);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}

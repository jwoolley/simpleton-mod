package thesimpleton.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.PruningAction;
import thesimpleton.characters.TheSimpletonCharacter;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.powers.AbstractCropPower;

public class Pruning extends CustomCard {
  public static final String ID = "TheSimpletonMod:Pruning";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "cards/pruning.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 0;

  private static final int NUM_STACKS_TO_HARVEST = 1;
  private static final int NUM_STACKS_TO_GAIN = 2;
  private static final int NUM_STACKS_TO_GAIN_UPGRADE = 1;

  public Pruning() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, getDescription(false), TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = NUM_STACKS_TO_GAIN;

    if (AbstractDungeon.isPlayerInDungeon()) {
      ((TheSimpletonCharacter) AbstractDungeon.player).getCropUtil().addCropTickedTrigger(() -> {
        updateDescription();
      });
    } else {
      TheSimpletonCharacter.addPrecombatPredrawTrigger(() -> {
        ((TheSimpletonCharacter) AbstractDungeon.player).getCropUtil().addCropTickedTrigger(() -> {
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
    AbstractDungeon.actionManager.addToBottom(new PruningAction(p, NUM_STACKS_TO_HARVEST, NUM_STACKS_TO_GAIN));
  }

  @Override
  public AbstractCard makeCopy() {
    return new Pruning();
  }

  private void updateDescription() {
    this.rawDescription = getDescription(true);
    this.initializeDescription();
  }

  private boolean didRegisterDrawnTrigger = false;
  @Override
  public void triggerWhenDrawn() {
    TheSimpletonMod.logger.debug(("Pruning::triggerWhenDrawn"));

    if (!didRegisterDrawnTrigger) {
      TheSimpletonMod.logger.debug(("Pruning::triggerWhenDrawn registering trigger"));
      ((TheSimpletonCharacter) AbstractDungeon.player).getCropUtil().addCropTickedTrigger(() -> {
        TheSimpletonMod.logger.debug(("Pruning::triggerWhenDrawn Trigger-when-drawn crop tick trigger triggered"));
        updateDescription();
      });
      didRegisterDrawnTrigger = true;
    }

    TheSimpletonMod.logger.debug(("Pruning::triggerWhenDrawn  Updating description"));
    this.updateDescription();
  }

  private static String getDescription(boolean checkCropValue) {
    String description = DESCRIPTION + NUM_STACKS_TO_HARVEST + EXTENDED_DESCRIPTION[0];
    AbstractCropPower crop;

    if (!checkCropValue || ((crop = AbstractCropPower.getOldestPower())) == null) {
      description += EXTENDED_DESCRIPTION[1];
    } else {
      description += EXTENDED_DESCRIPTION[2] + crop.name + EXTENDED_DESCRIPTION[3];
    }
    return description;
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(NUM_STACKS_TO_GAIN_UPGRADE);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}

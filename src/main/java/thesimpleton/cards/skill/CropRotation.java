package thesimpleton.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.CurseUtil;
import thesimpleton.cards.TheSimpletonCardTags;
import thesimpleton.powers.AbstractCropPower;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class CropRotation extends AbstractHarvestCard {
  public static final String ID = "TheSimpletonMod:CropRotation";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String UPGRADE_DESCRIPTION;
  public static final String IMG_PATH = "cards/croprotation.png";

  private static final CardStrings cardStrings;

  private static final AbstractCard.CardType TYPE = CardType.SKILL;
  private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
  private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 0;

  private static final int NUM_CROPS = 1;
  private static final int NUM_CROPS_UPGRADE_BONUS = 1;

  public CropRotation() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH),
        COST, DESCRIPTION, TYPE, RARITY, TARGET, NUM_CROPS, false, false);
    this.baseMagicNumber = this.magicNumber = NUM_CROPS;
    this.tags.add(TheSimpletonCardTags.HARVEST);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    //TODO: exhaust?
    //TODO: allow splits between two powers for upgrades (requires calculation for harvest action)
    //TODO: add "fizzle" effect if there are no stacks to harvest

    // harvest existing stacks
    final ArrayList<AbstractPower> activePowers =  new ArrayList<>(p.powers);
    Collections.shuffle(activePowers);
    Optional<AbstractPower> oldPower = activePowers.stream()
        .filter(pow -> pow instanceof AbstractCropPower && !((AbstractCropPower) pow).finished)
        .findFirst();

    if (oldPower.isPresent()) {
      ((AbstractCropPower) oldPower.get()).harvest(false, this.magicNumber);
    }

    // add new stacks
    final AbstractCropPower newCrop = CurseUtil.getRandomCropPower(p, this.magicNumber);

    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, newCrop, this.magicNumber));
  }

  @Override
  public AbstractCard makeCopy() {
    return new CropRotation();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      upgradeName();
      upgradeMagicNumber(NUM_CROPS_UPGRADE_BONUS);
      this.rawDescription = UPGRADE_DESCRIPTION;
      this.initializeDescription();
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
  }
}
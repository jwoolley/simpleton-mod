package thesimpleton.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.TheSimpletonCardTags;
import thesimpleton.powers.AbstractCropPower;
import thesimpleton.powers.PlantTurnipPower;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class RootOut extends AbstractHarvestCard {
  public static final String ID = "TheSimpletonMod:RootOut";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/rootout.png";

  private static final CardStrings cardStrings;

  private static final AbstractCard.CardType TYPE = CardType.SKILL;
  private static final AbstractCard.CardRarity RARITY = CardRarity.COMMON;
  private static final AbstractCard.CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 1;
  private static final int STRENGTH_BONUS = 1;
  private static final int UPGRADED_STRENGTH_BONUS = 4;

  public RootOut() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET,
        1, true, false);
    this.baseMagicNumber = this.magicNumber = STRENGTH_BONUS;
    this.exhaust = true;
    this.tags.add(TheSimpletonCardTags.HARVEST);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new LoseStrengthPower(p, this.magicNumber), this.magicNumber));

    // harvest turnip stacks
    final ArrayList<AbstractPower> activePowers = new ArrayList<>(p.powers);
    Collections.shuffle(activePowers);
    Optional<AbstractPower> turnipPower = activePowers.stream()
        .filter(pow -> pow instanceof PlantTurnipPower && !((AbstractCropPower) pow).finished)
        .findFirst();

    if (turnipPower.isPresent()) {
      ((AbstractCropPower) turnipPower.get()).harvest(true, 1);
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
      upgradeMagicNumber(UPGRADED_STRENGTH_BONUS);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
  }
}
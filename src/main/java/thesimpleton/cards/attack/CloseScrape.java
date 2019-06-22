package thesimpleton.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.interfaces.AbstractDynamicCropOrbHighlighterCard;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.orbs.AbstractCropOrb;

public class CloseScrape extends AbstractDynamicCropOrbHighlighterCard {
    public static final String ID = "TheSimpletonMod:CloseScrape";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "cards/closescrape.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int DAMAGE = 9;
    private static final int UPGRADE_DAMAGE_AMOUNT = 3;
    private static final int NUM_CROP_STACKS_TO_PLANT = 1;

    public CloseScrape() {
      super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, getDescription(false), TYPE,
          AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
      this.baseDamage = this.damage = DAMAGE;
      this.baseMagicNumber = this.magicNumber = NUM_CROP_STACKS_TO_PLANT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
      AbstractDungeon.actionManager.addToBottom(
          new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
              AbstractGameAction.AttackEffect.SLASH_DIAGONAL));

      if (AbstractCropOrb.playerHasAnyCropOrbs()) {
        AbstractCropOrb.getOldestCropOrb().getCrop().stackOrb(this.magicNumber, true);
      }
    }

    @Override
    public AbstractCard makeCopy() {
      return new CloseScrape();
    }

    @Override
    public void upgrade() {
      if (!this.upgraded) {
        this.upgradeName();
        this.upgradeDamage(UPGRADE_DAMAGE_AMOUNT);
      }
    }

  public void updateDescription(boolean extendedDescription) {
    this.rawDescription = getDescription(extendedDescription);
    this.initializeDescription();
  }

  private static String getDescription(boolean extendedDescription) {
    String description = DESCRIPTION;

    if (extendedDescription)
      if (AbstractCropOrb.playerHasAnyCropOrbs()) {
        AbstractCropOrb cropOrb = AbstractCropOrb.getOldestCropOrb();
        description += EXTENDED_DESCRIPTION[2] + cropOrb.name + EXTENDED_DESCRIPTION[3];
      } else {
        description += EXTENDED_DESCRIPTION[1];
      }

    description += EXTENDED_DESCRIPTION[0];
    return description;
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }

  @Override
  public AbstractCropOrb findCropOrb() {
    return AbstractCropOrb.getOldestCropOrb();
  }
}

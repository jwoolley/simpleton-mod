package thesimpleton.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.powers.AbstractCropPower;

public class Abundance extends CustomCard {
  public static final String ID = "TheSimpletonMod:Abundance";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/abundance.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 1;
  private static final int UPGRADED_COST = 0;
  private static final int CARDS_PER_CROP_TYPE = 1;

  public Abundance() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = CARDS_PER_CROP_TYPE;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    final int numCropTypes = AbstractCropPower.getNumberActiveCropTypes();

    if (numCropTypes > 0) {
      AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, AbstractCropPower.getNumberActiveCropTypes()));
    } else {
      AbstractDungeon.actionManager.addToBottom(new SFXAction("CARD_DRAW_8"));
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new Abundance();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeBaseCost(UPGRADED_COST);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
  }
}

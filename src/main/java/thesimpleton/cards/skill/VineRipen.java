package thesimpleton.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.CropSpawnAction;
import thesimpleton.actions.VineRipenThresholdAction;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.orbs.SquashCropOrb;

public class VineRipen extends CustomCard {
  public static final String ID = "TheSimpletonMod:VineRipen";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/vineripen.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 2;
  private static final int UPGRADED_COST = 1;
  private static final int SQUASH_STACKS = 2;

  public VineRipen() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = SQUASH_STACKS;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new CropSpawnAction(new SquashCropOrb(this.magicNumber),true));
    AbstractDungeon.actionManager.addToBottom(new VineRipenThresholdAction());
  }

  @Override
  public AbstractCard makeCopy() {
    return new VineRipen();
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
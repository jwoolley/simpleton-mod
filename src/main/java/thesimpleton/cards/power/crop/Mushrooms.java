package thesimpleton.cards.power.crop;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.CropSpawnAction;
import thesimpleton.cards.TheSimpletonCardTags;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.orbs.MushroomCrop;
import thesimpleton.actions.ApplyCropAction;
import thesimpleton.powers.PlantMushroomPower;

public class Mushrooms extends AbstractCropPowerCard {
  public static final String ID = "TheSimpletonMod:Mushrooms";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/mushrooms.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.POWER;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 2;
  private static final int UPGRADED_COST = 1;
  private static final int CROP_STACKS = 2;

  public Mushrooms() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = CROP_STACKS;
    this.tags.add(TheSimpletonCardTags.CROP_POWER);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
//    AbstractDungeon.actionManager.addToBottom(
//        new ApplyCropAction(p, p, new PlantMushroomPower(p, this.magicNumber, true), this.magicNumber, true));
    AbstractDungeon.actionManager.addToBottom(new CropSpawnAction(new MushroomCrop(this.magicNumber)));
  }

  @Override
  public AbstractCard makeCopy() {
    return new Mushrooms();
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
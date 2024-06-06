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
import thesimpleton.cards.attack.Gnawberry;
import thesimpleton.cards.status.IceCube;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.orbs.StrawberryCropOrb;

public class Strawberries extends AbstractCropPowerCard {
  public static final String ID = "TheSimpletonMod:Strawberries";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/strawberries.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.POWER;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 1;
  private static final int CROP_STACKS = 2;
  private static final int UPGRADE_CROP_STACKS = 1;

  private static final AbstractCard PREVIEW_CARD;

  public Strawberries() {
    super(ID, NAME, TheSimpletonMod.getImageResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = CROP_STACKS;
    this.tags.add(TheSimpletonCardTags.CROP_POWER);
    this.cardsToPreview = PREVIEW_CARD;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new CropSpawnAction(new StrawberryCropOrb(), this.magicNumber, true));
  }

  @Override
  public AbstractCard makeCopy() {
    return new Strawberries();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(UPGRADE_CROP_STACKS);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    PREVIEW_CARD = new Gnawberry();
  }
}
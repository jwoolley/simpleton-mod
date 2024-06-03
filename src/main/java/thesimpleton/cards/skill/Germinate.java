package thesimpleton.cards.skill;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.GerminateAction;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.cards.interfaces.AbstractDynamicCropOrbHighlighterCard;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.orbs.AbstractCropOrb;

public class Germinate extends AbstractDynamicCropOrbHighlighterCard {
  public static final String ID = "TheSimpletonMod:Germinate";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "cards/germinate.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 1;
  private static final int CROP_AMOUNT = 2;

  private static final int BLOCK_AMOUNT = 14;
  private static final int BLOCK_UPGRADE_AMOUNT = 4;


  public Germinate() {
    super(ID, NAME, TheSimpletonMod.getImageResourcePath(IMG_PATH), COST, getDescription(false), TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = CROP_AMOUNT;
    this.baseBlock = this.block = BLOCK_AMOUNT;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new GerminateAction(this.magicNumber, this.block));
  }

  @Override
  public AbstractCard makeCopy() {
    return new Germinate();
  }

  public void updateDescription(boolean extendedDescription) {
    this.rawDescription = getDescription(extendedDescription);
    this.initializeDescription();
  }

  @Override
  public void triggerOnGlowCheck() {
    this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();

    if (AbstractCropOrb.playerHasAnyCropOrbs()) {
      final AbstractCropOrb newestCropOrb = AbstractCropOrb.getNewestCropOrb();
      if (newestCropOrb.isMature(true)) {
        this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
      }
    }
  }

  private static String getDescription(boolean extendedDescription) {
    String description = DESCRIPTION;

    if (extendedDescription) {
      if (AbstractCropOrb.playerHasAnyCropOrbs()) {
        AbstractCropOrb newestCropOrb = AbstractCropOrb.getNewestCropOrb();
        description += EXTENDED_DESCRIPTION[2] + newestCropOrb.name + (newestCropOrb.isMature(true) ? EXTENDED_DESCRIPTION[3] : EXTENDED_DESCRIPTION[4]);
      } else {
        description += EXTENDED_DESCRIPTION[1];
      }
      description += EXTENDED_DESCRIPTION[0];
    }

    return description;
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeBlock(BLOCK_UPGRADE_AMOUNT);
      this.initializeDescription();
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }

  @Override
  public AbstractCropOrb findCropOrb() {
    return AbstractCropOrb.getNewestCropOrb();
  }
}

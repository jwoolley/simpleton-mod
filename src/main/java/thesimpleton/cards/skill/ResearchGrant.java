package thesimpleton.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.lwjgl.opengl.EXTAbgr;
import thesimpleton.TheSimpletonMod;
import thesimpleton.enums.AbstractCardEnum;

public class ResearchGrant extends CustomCard {
  public static final String ID = "TheSimpletonMod:ResearchGrant";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "cards/researchgrant.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 1;
  private static final int BLOCK = 16;
  private static final int BLOCK_UPGRADE = 4;
  private static final int NUM_CARD_COPIES = 2;
  private static final int NUM_CARD_COPIES_UPGRADE = 1;

  private static final AbstractCard PREVIEW_CARD;

  public ResearchGrant() {
    super(ID, NAME, TheSimpletonMod.getImageResourcePath(IMG_PATH), COST, getDescription(NUM_CARD_COPIES), TYPE,
        AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseBlock = this.block = BLOCK;
    this.baseMagicNumber = this.magicNumber = NUM_CARD_COPIES;
    this.exhaust = true;
    this.cardsToPreview = PREVIEW_CARD;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new SoilSample(), this.magicNumber));
  }

  @Override
  public AbstractCard makeCopy() {
    return new ResearchGrant();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeBlock(BLOCK_UPGRADE);
      this.upgradeMagicNumber(NUM_CARD_COPIES_UPGRADE);
      this.rawDescription = getDescription(this.magicNumber);
      this.initializeDescription();
    }
  }

  private static String getDescription(int numSoilSamples) {
    return DESCRIPTION
        + (numSoilSamples == 1 ? EXTENDED_DESCRIPTION[0] : EXTENDED_DESCRIPTION[1])
        + EXTENDED_DESCRIPTION[2];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    PREVIEW_CARD = new SoilSample();
  }
}
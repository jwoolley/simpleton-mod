package thesimpleton.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.ModifyBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.status.Depletion;
import thesimpleton.enums.AbstractCardEnum;

public class SeedCoat extends CustomCard {
  public static final String ID = "TheSimpletonMod:SeedCoat";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String UPGRADE_DESCRIPTION;
  public static final String IMG_PATH = "cards/seedcoat.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 1;
  private static final int BLOCK = 9;
  private static final int UPGRADE_BLOCK_AMOUNT =  3;
  private static final AbstractCard PREVIEW_CARD;
  private static final AbstractCard UPGRADED_PREVIEW_CARD;

  public SeedCoat() {
    super(ID, NAME, TheSimpletonMod.getImageResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseBlock = this.block = BLOCK;
    this.exhaust = true;
    this.cardsToPreview = PREVIEW_CARD;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));

    AbstractCard cardForDiscardPile = new ShellFragment();
    if (this.upgraded) {
      cardForDiscardPile.upgrade();
    }

    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(cardForDiscardPile, 3));
  }

  @Override
  public AbstractCard makeCopy() {
    return new SeedCoat();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeBlock(UPGRADE_BLOCK_AMOUNT);
      this.cardsToPreview = UPGRADED_PREVIEW_CARD;
      updateDescription();
    }
  }

  protected void updateDescription() {
    this.rawDescription = UPGRADE_DESCRIPTION;
    this.initializeDescription();
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    PREVIEW_CARD = new ShellFragment();
    UPGRADED_PREVIEW_CARD = new ShellFragment();
    UPGRADED_PREVIEW_CARD.upgrade();
  }
}

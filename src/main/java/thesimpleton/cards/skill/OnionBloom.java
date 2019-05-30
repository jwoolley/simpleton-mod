package thesimpleton.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.CropSpawnAction;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.orbs.OnionCropOrb;

public class OnionBloom extends CustomCard {
  public static final String ID = "TheSimpletonMod:OnionBloom";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String UPGRADE_DESCRIPTION;
  public static final String IMG_PATH = "cards/onionbloom.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.SELF;

  private static final int COST = 0;
  private static final int DRAW_AMOUNT = 1;
  private static final int DISCARD_AMOUNT = 1;
  private static final int ONION_STACKS = 1;

  private boolean mustDiscard;

  public OnionBloom() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = ONION_STACKS;
    this.mustDiscard = true;
    this.isInnate = true;
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, DRAW_AMOUNT));

    if (this.mustDiscard) {
      AbstractDungeon.actionManager.addToBottom(new DiscardAction(p, p, DISCARD_AMOUNT, false));
    }

    AbstractDungeon.actionManager.addToBottom(new CropSpawnAction(new OnionCropOrb(this.magicNumber), true));
  }

  @Override
  public AbstractCard makeCopy() {
    return new OnionBloom();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      // this.mustDiscard = false;
      this.exhaust = false;
      this.rawDescription = UPGRADE_DESCRIPTION;
      initializeDescription();
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
  }
}
package thesimpleton.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.AbstractCardWithPreviewCard;
import thesimpleton.cards.status.Depletion;
import thesimpleton.enums.AbstractCardEnum;

public class SaltTheEarth extends AbstractCardWithPreviewCard {
  public static final String ID = "TheSimpletonMod:SaltTheEarth";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/salttheearth.png";

  private static final CardStrings cardStrings;

  private static final AbstractCard.CardType TYPE = CardType.ATTACK;
  private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
  private static final AbstractCard.CardTarget TARGET = CardTarget.ALL_ENEMY;

  private static final int COST = 2;
  private static final int DAMAGE = 24;
  private static final int DAMAGE_UPGRADE = 8;
  private static final int STATUS_COPIES = 2;

  private static final AbstractCard PREVIEW_CARD;

  public SaltTheEarth() {
    super(ID, NAME, TheSimpletonMod.getImageResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = STATUS_COPIES;
    this.isMultiDamage = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn,
            AbstractGameAction.AttackEffect.POISON));

    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Depletion(), this.magicNumber));
  }

  @Override
  public AbstractCard makeCopy() {
    return new SaltTheEarth();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      upgradeName();
      upgradeDamage(DAMAGE_UPGRADE);
      initializeDescription();
    }
  }


  @Override
  public AbstractCard getPreviewCard() {
    return PREVIEW_CARD;
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    PREVIEW_CARD = new Depletion();

  }
}
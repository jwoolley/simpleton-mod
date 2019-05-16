package thesimpleton.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.HitTheSackAction;
import thesimpleton.enums.AbstractCardEnum;

public class HitTheSack extends CustomCard {
  public static final String ID = "TheSimpletonMod:HitTheSack";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String UPGRADE_DESCRIPTION;
  public static final String IMG_PATH = "cards/hitthesack.png";

  private static final CardStrings cardStrings;

  private static final AbstractCard.CardType TYPE = CardType.ATTACK;
  private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
  private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = -1;
  private static final int DAMAGE_PER_STACK = 7;
  private static final int POTATOES_STACK_AMOUNT = 1;

  public HitTheSack() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE_PER_STACK;
    this.baseMagicNumber = this.magicNumber = POTATOES_STACK_AMOUNT;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    if (this.energyOnUse < EnergyPanel.totalCount) {
      this.energyOnUse = EnergyPanel.totalCount;
    }
    AbstractDungeon.actionManager.addToBottom(
        new HitTheSackAction(p, m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
          this.magicNumber, this.upgraded, this.freeToPlayOnce, this.energyOnUse));
  }

  @Override
  public AbstractCard makeCopy() {
    return new HitTheSack();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      upgradeName();
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
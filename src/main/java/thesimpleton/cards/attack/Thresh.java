package thesimpleton.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.HarvestTriggeredCard;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.enums.AbstractCardEnum;

public class Thresh extends CustomCard implements HarvestTriggeredCard {
  public static final String ID = "TheSimpletonMod:Thresh";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String IMG_PATH = "cards/thresh.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 3;
  private static final int DAMAGE = 22;
  private static final int UPGRADE_DAMAGE_AMOUNT = 6;
  private static final int COST_DECREASE_PER_HARVEST = 1;

  public Thresh() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = COST_DECREASE_PER_HARVEST;
  }

  public void use(AbstractPlayer p, AbstractMonster m) {
    //STS_SFX_DefectBeam_v1.ogg
//    AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_DEFECT_BEAM"));
//
//    AbstractGameEffect effect = new MindblastEffect(
//        m.hb.cX, m.hb.cY, false);
//
//    AbstractDungeon.actionManager.addToBottom(new VFXAction(p, effect, 0.2F));
//    AbstractDungeon.actionManager.addToBottom(
//        new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
//            AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
//
//    AbstractCropOrb.getActiveCropOrbs()
//        .forEach(orb -> orb.getCrop().stackOrb(this.magicNumber, true));

    AbstractDungeon.actionManager.addToBottom(
        new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
            AbstractGameAction.AttackEffect.SLASH_VERTICAL));
  }

  @Override
  public AbstractCard makeCopy() {
    AbstractCard tmp = new Thresh();
    if (AbstractDungeon.getCurrMapNode() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT &&
        SimpletonUtil.hasHarvestedThisTurn()) {
      tmp.updateCost(-SimpletonUtil.getNumTimesHarvestedThisTurn() * COST_DECREASE_PER_HARVEST);
    }
    return tmp;
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeDamage(UPGRADE_DAMAGE_AMOUNT);
    }
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
  }

  @Override
  public void harvestedTrigger() {
    this.setCostForTurn(this.costForTurn - COST_DECREASE_PER_HARVEST);
  }
}

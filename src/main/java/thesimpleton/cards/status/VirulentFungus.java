package thesimpleton.cards.status;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import thesimpleton.TheSimpletonMod;

public class VirulentFungus extends CustomCard {
  public static final String ID = "TheSimpletonMod:VirulentFungus";
  private static final CardStrings cardStrings;
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "cards/virulentfungus.png";

  private static final CardType TYPE = CardType.STATUS;
  private static final CardRarity RARITY = CardRarity.SPECIAL;
  private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

  private static final int COST = 1;

  private static final int REGEN_AMOUNT = 1;
  private static final int BUFF_AMOUNT = 3;
  private static final int POISON_AMOUNT = 3;

  public VirulentFungus() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, getDescription(), TYPE, CardColor.COLORLESS, RARITY,
        TARGET);
    this.baseMagicNumber = this.magicNumber = BUFF_AMOUNT;
    this.exhaust = true;
  }

  public void use(AbstractPlayer p, AbstractMonster m) {
    if ((!this.dontTriggerOnUseCard)) {
      if (p.hasRelic("Medical Kit")) {
        useMedicalKit(p);
      }
    }
  }

  private void poisonEnemies(int amount) {
    AbstractPlayer p = AbstractDungeon.player;
    for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
      if ((!monster.isDead) && (!monster.isDying)) {
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(monster, p, new PoisonPower(monster, p, amount), amount));
      }
    }
  }

  public void triggerWhenDrawn() {
    AbstractPlayer p = AbstractDungeon.player;
    if ((p.hasPower("Evolve")) && (!p.hasPower("No Draw"))) {
      p.getPower("Evolve").flash();
      AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, p.getPower("Evolve").amount));
    }
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new PlatedArmorPower(p, BUFF_AMOUNT), BUFF_AMOUNT));

    poisonEnemies(POISON_AMOUNT);
  }

  public AbstractCard makeCopy() {
    return new VirulentFungus();
  }

  public void upgrade() { }

  private static String getDescription() {
    return DESCRIPTION + POISON_AMOUNT + EXTENDED_DESCRIPTION[0];
  }

  @Override
  public boolean canUse(AbstractPlayer p, AbstractMonster m) {
    return true;
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}
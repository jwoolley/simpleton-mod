package thesimpleton.cards.attack;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ThrowDaggerEffect;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.SaladShooterAction;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.cards.interfaces.AbstractDynamicTextCard;
import thesimpleton.effects.utils.VFXActionTemplate;
import thesimpleton.enums.AbstractCardEnum;

// TODO: make into AbstractDynamicTextCard (include count)

public class SaladShooter extends AbstractDynamicTextCard {
  public static final String ID = "TheSimpletonMod:SaladShooter";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String UPGRADE_DESCRIPTION;
  public static final String IMG_PATH = "cards/saladshooter.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

  private static final int COST = 1;
  private static final int DAMAGE = 6;
  private static final int UPGRADE_DAMAGE_AMOUNT = 2;

  public SaladShooter() {
    super(ID, NAME, TheSimpletonMod.getImageResourcePath(IMG_PATH), COST, getDescription(false), TYPE,
        AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);

    this.baseDamage = this.damage = DAMAGE;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new SaladShooterAction(
        p,
        AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng),
        this.damage,
        SimpletonUtil.getNumTimesHarvestedThisTurn() + 1,
        new VFXActionTemplate() {
          @Override
          public VFXAction getAction(float x, float y) {
            return new VFXAction(p, new ThrowDaggerEffect(x, y), 0.1F);
          }
        },
        new SFXAction("ATTACK_FIRE", 0.025f)
      )
    );
  }

  @Override
  public AbstractCard makeCopy() {
    return new SaladShooter();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeDamage(UPGRADE_DAMAGE_AMOUNT);
//      initializeDescription();
    }
  }

  private static String getDescription(boolean extendedDescription) {
    String description = DESCRIPTION;

    final int numTriggers = SimpletonUtil.getNumTimesHarvestedThisTurn() + 1;

    if (extendedDescription) {
      description += EXTENDED_DESCRIPTION[1] +
          numTriggers + (numTriggers == 1 ? EXTENDED_DESCRIPTION[2] : EXTENDED_DESCRIPTION[3]);
    }

    description += EXTENDED_DESCRIPTION[0];

    return description;
  }

  @Override
  protected void updateDescription(boolean extendedDescription) {
    this.rawDescription = getDescription(extendedDescription);
    this.initializeDescription();
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
  }
}

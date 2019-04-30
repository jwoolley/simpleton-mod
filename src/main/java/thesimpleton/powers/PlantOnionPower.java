package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Onions;
import thesimpleton.powers.utils.Crop;

public class PlantOnionPower extends AbstractCropPower {
  public static final Crop enumValue = Crop.ONIONS;
  public static final String POWER_ID = "TheSimpletonMod:PlantOnionPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static PowerType POWER_TYPE = PowerType.BUFF;
  public static final String IMG = "plantonion.png";
  private static final CardRarity cropRarity = CardRarity.COMMON;
  private static final AbstractCropPowerCard powerCard = new Onions();
  private static int BASE_WEAK_PER_STACK = 1;
  private static int weakPerStack = 1;

  public PlantOnionPower(AbstractCreature owner, int amount) {
    this(owner, amount, false);
  }

  public PlantOnionPower(AbstractCreature owner, int amount, boolean isFromCard) {
    super(enumValue, NAME, POWER_ID, POWER_TYPE, DESCRIPTIONS, IMG, owner, cropRarity, powerCard, amount, isFromCard);
    updateDescription();
  }

  @Override
  public void updateDescription() {
    this.description = getPassiveDescription() + " NL " + DESCRIPTIONS[0];
  }

  protected int harvestAction(int harvestAmount) {
    if (harvestAmount > 0) {
      for (int i = 0; i < harvestAmount; i++) {
        AbstractMonster m = SimpletonUtil.getRandomMonster();
        if (m != null) {
          AbstractDungeon.actionManager.addToBottom(
              new ApplyPowerAction(m, this.owner, new WeakPower(m, 1, false),1));
        }
      }
    }
    return harvestAmount;
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}
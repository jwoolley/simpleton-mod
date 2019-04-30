package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Mushrooms;
import thesimpleton.powers.utils.Crop;

public class PlantMushroomPower extends AbstractCropPower {
  public static final Crop enumValue = Crop.MUSHROOMS;

  public static final String POWER_ID = "TheSimpletonMod:PlantMushroomPower";
  private static final PowerStrings powerStrings;

  public static final String NAME;
  public static final String[] DESCRIPTIONS;
  public static PowerType POWER_TYPE = PowerType.BUFF;
  public static final String IMG = "plantmushroom.png";
  public static final CardRarity cropRarity = CardRarity.RARE;
  private static final AbstractCropPowerCard powerCard = new Mushrooms();

  private static final int MATURITY_THRESHOLD = 2;

  public PlantMushroomPower(AbstractCreature owner, int amount) {
    this(owner, amount, false);
  }

  public PlantMushroomPower(AbstractCreature owner, int amount, boolean isFromCard) {
    super(enumValue, NAME, POWER_ID, POWER_TYPE, DESCRIPTIONS, IMG, owner, cropRarity, powerCard, amount,  isFromCard,false, MATURITY_THRESHOLD);
    updateDescription();
  }

  @Override
  public void updateDescription() {
    this.description = getPassiveDescription() + " NL " + DESCRIPTIONS[0];
  }

  protected int harvestAction(int harvestAmount) {
    if (harvestAmount > 0) {
      for (int i = 0; i < harvestAmount; i++) {
        AbstractCard card = AbstractDungeon.returnTrulyRandomColorlessCardInCombat().makeCopy();
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(card, 1));
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

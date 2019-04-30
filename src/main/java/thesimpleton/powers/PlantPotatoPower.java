package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Potatoes;
import thesimpleton.powers.utils.Crop;
import thesimpleton.relics.HotPotato;

public class PlantPotatoPower extends AbstractCropPower {
  public static final Crop enumValue = Crop.POTATOES;

  public static final String POWER_ID = "TheSimpletonMod:PlantPotatoPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static PowerType POWER_TYPE = PowerType.BUFF;
  public static final String IMG = "plantpotato.png";
  public static final CardRarity cropRarity = CardRarity.COMMON;
  private static final AbstractCropPowerCard powerCard = new Potatoes();

  public PlantPotatoPower(AbstractCreature owner, int amount) {
    this(owner, amount, false);
  }

  public PlantPotatoPower(AbstractCreature owner, int amount, boolean isFromCard) {
    super(enumValue, NAME, POWER_ID, POWER_TYPE, DESCRIPTIONS, IMG, owner, cropRarity, powerCard, amount, isFromCard);
    this.name = NAME;
    updateDescription();
    logger.debug("MAKIN' POTATOES (instantiating plantpotatopower). name: " + NAME);
  }

  @Override
  public void updateDescription() {
    this.description = getPassiveDescription() + " NL " + DESCRIPTIONS[0];
  }

  protected int harvestAction(int harvestAmount) {
    if (harvestAmount > 0) {
      if (SimpletonUtil.getPlayer().hasRelic(HotPotato.ID)) {
        SimpletonUtil.getPlayer().getRelic(HotPotato.ID).flash();
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(SimpletonUtil.FLAMING_SPUD, harvestAmount));
      } else {
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(SimpletonUtil.SPUD_MISSILE, harvestAmount));
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
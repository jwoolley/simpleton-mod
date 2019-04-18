package thesimpleton.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.characters.TheSimpletonCharacter;
import thesimpleton.utilities.*;

abstract public class AbstractCropTriggerCard extends CustomCard implements TriggerClient {
  private final CombatLifecycleTriggerListener cropTickTriggerListener;
  private final Trigger cropTickTrigger;
  private boolean didRegisterDrawnTrigger = false;

  private static int ID_COUNTER = 1;
  protected int instanceId;

  public AbstractCropTriggerCard(String id, String name, String imgUrl, int cost,
                                 String rawDescription, CardType type, CardColor color,
                                 CardRarity rarity, CardTarget target, Trigger trigger) {
    super(id, name, imgUrl, cost, rawDescription, type, color, rarity, target);

    this.instanceId = ID_COUNTER++;

    TheSimpletonMod.logger.debug("Instantiating AbstractCropTriggerCard: " + this.name + " (" + this.instanceId + ")");

    this.cropTickTrigger = trigger != null ? trigger : () -> {
      TheSimpletonMod.logger.debug((this.name + " (" + this.instanceId + "): crop tick trigger triggered " + this.instanceId));
      updateDescription();
    };

    this.cropTickTriggerListener = () -> cropTickTrigger;

    registerCropTriggerListener();

    if (AbstractDungeon.isPlayerInDungeon()) {
      updateDescription();
    }
  }

  @Override
  public void atTurnStart() {
    super.atTurnStart();
    updateDescription();
  }

  abstract protected void updateDescription();

  public TriggerListener getTriggerListener() { return this.cropTickTriggerListener; }

  private void registerCropTriggerListener() {
    Logger logger = TheSimpletonMod.logger;

    if (AbstractDungeon.isPlayerInDungeon()) {
      logger.debug(this.name + " (" + this.instanceId + "): AbstractDungeon.player is defined. Registering cropTickTriggerListener.");

      ((TheSimpletonCharacter) AbstractDungeon.player).getCropUtil().addCropTickedTriggerListener(this.cropTickTriggerListener);
    } else {
      logger.debug(this.name + " (" + this.instanceId + "): AbstractDungeon.player is not currently defined. Registering precombatpredrawtrigger.");
      TheSimpletonCharacter.addPrecombatPredrawTrigger(() -> {
        logger.debug(this.name + " (" + this.instanceId + "): Applying preloaded precombatpredrawtrigger to register cropTickTriggerListener.");

        ((TheSimpletonCharacter) AbstractDungeon.player).getCropUtil().addCropTickedTriggerListener(this.cropTickTriggerListener);
      });
    }
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    TheSimpletonMod.logger.debug((this.name + " (" + this.instanceId + ") use() called"));
  }

  @Override
  public void triggerWhenDrawn() {
    super.triggerWhenDrawn();
    TheSimpletonMod.logger.debug((this.name + " (" + this.instanceId + ")::triggerWhenDrawn"));

    CropUtil.triggerCardUpdates();

    if (!didRegisterDrawnTrigger) {
      TheSimpletonMod.logger.debug((this.name + " (" + this.instanceId + ")::triggerWhenDrawn registering trigger"));
      ((TheSimpletonCharacter) AbstractDungeon.player).getCropUtil().addCropTickedTriggerListener(this.cropTickTriggerListener);
      didRegisterDrawnTrigger = true;
    }

    TheSimpletonMod.logger.debug((this.name + " (" + this.instanceId + ")::triggerWhenDrawn  Updating description"));
    updateDescription();
  }
}

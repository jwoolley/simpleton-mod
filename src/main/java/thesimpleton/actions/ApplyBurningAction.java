package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.powers.BurningPower;
import thesimpleton.relics.GasCan;
import thesimpleton.utilities.ModLogger;

import java.util.List;
import java.util.stream.Collectors;

public class ApplyBurningAction extends AbstractGameAction {
  private static ModLogger logger = TheSimpletonMod.traceLogger;
  private static float ACTION_DURATION = Settings.ACTION_DUR_XFAST;

  private final boolean isSecondaryApplication;
  private final boolean useFastMode;

  public ApplyBurningAction(AbstractCreature target, AbstractCreature source, int amount) {
    this(target, source, amount, false);
  }

  public ApplyBurningAction(AbstractCreature target, AbstractCreature source, int amount, boolean isSecondaryApplication) {
    this(target, source, amount, isSecondaryApplication, false);
  }

  public ApplyBurningAction(AbstractCreature target, AbstractCreature source, int amount, boolean isSecondaryApplication, boolean useFastMode) {
    this.target = target;
    this.source = source;
    this.amount = amount;
    this.duration = ACTION_DURATION;
    this.startDuration = ACTION_DURATION;
    this.isSecondaryApplication = isSecondaryApplication;
    this.useFastMode = useFastMode;
  }

  @Override
  public void update() {


    logger.trace("ApplyBurningAction.update :: ACTION_DURATION: " + ACTION_DURATION);
    logger.trace("ApplyBurningAction.update :: duration: " + this.duration);

    if (this.duration == this.startDuration) {

      logger.trace("ApplyBurningAction.update :: Applying  " +  this.amount + " Burning" );
      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(
              this.target, this.source, new BurningPower(this.target, this.source, this.amount), this.amount,
              this.useFastMode));

      AbstractPlayer player = AbstractDungeon.player;
      if (player.hasRelic(GasCan.ID) && !this.isSecondaryApplication) {
        List<AbstractMonster> otherMonsters = AbstractDungeon.getMonsters().monsters.stream()
            .filter(m -> m != this.target && !m.isDeadOrEscaped()).collect(Collectors.toList());

        if (otherMonsters.size() > 0) {
          ((GasCan)player.getRelic(GasCan.ID)).triggerEffect();

          otherMonsters.forEach(m ->
              AbstractDungeon.actionManager.addToBottom(
                  new ApplyBurningAction(m, this.source, this.amount / 2, true)));
        }
      }
      this.tickDuration();
      this.isDone = true;
    }
    this.tickDuration();
  }
}

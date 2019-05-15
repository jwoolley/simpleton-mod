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
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.powers.BurningPower;
import thesimpleton.relics.GasCan;
import thesimpleton.relics.unused.DemonicMark;

import java.rmi.activation.ActivationMonitor;
import java.util.List;
import java.util.stream.Collectors;

public class ApplyBurningAction extends AbstractGameAction {
  private static float ACTION_DURATION = Settings.ACTION_DUR_XFAST;

  private final boolean isSecondaryApplication;

  public ApplyBurningAction(AbstractCreature target, AbstractCreature source, int amount) {
    this(target, source, amount, false);
  }

  public ApplyBurningAction(AbstractCreature target, AbstractCreature source, int amount, boolean isSecondaryApplication) {
    this.target = target;
    this.source = source;
    this.amount = amount;
    this.duration = ACTION_DURATION;
    this.startDuration = ACTION_DURATION;
    this.isSecondaryApplication = isSecondaryApplication;
  }

  @Override
  public void update() {

    Logger logger = TheSimpletonMod.logger;

    logger.debug("ApplyBurningAction.update :: ACTION_DURATION: " + ACTION_DURATION);
    logger.debug("ApplyBurningAction.update :: duration: " + this.duration);

    if (this.duration == this.startDuration) {

      logger.debug("ApplyBurningAction.update :: Applying  " +  this.amount + " Burning" );
      AbstractDungeon.actionManager.addToBottom(
          new ApplyPowerAction(
              this.target, this.source, new BurningPower(this.target, this.source, this.amount), this.amount));

      AbstractPlayer player = SimpletonUtil.getPlayer();
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

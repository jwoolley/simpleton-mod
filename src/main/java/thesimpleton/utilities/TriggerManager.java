package thesimpleton.utilities;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TriggerManager {
  private ArrayList<TriggerListener> triggerListeners;


  public TriggerManager() {
    this(new ArrayList<>());
  }

  public TriggerManager(List<TriggerListener> triggerListeners) {
    this.triggerListeners = new ArrayList<>(triggerListeners);
    TheSimpletonMod.logger.debug("TriggerManager: Instantiated with " + this.triggerListeners.size() + " triggers");
  }

  public int numTriggerListeners() {
    return triggerListeners.size();
  }

  public void addTriggerListener(TriggerListener triggerListener) {
    triggerListeners.add(triggerListener);
  }

  public void addTrigger(Trigger trigger) {
    triggerListeners.add(() -> trigger);
  }

  public void addTriggerListeners(List<TriggerListener> triggerListeners) { triggerListeners.addAll(triggerListeners);  }

  public void removeTriggerListener(TriggerListener triggerListener) {
    triggerListeners.remove(triggerListener);
  }

  public void triggerAll() {
    TheSimpletonMod.logger.debug("TriggerManager.triggerAll triggering " + this.triggerListeners.size() + " triggers");
    // copy to prevent concurrent modification
    List<TriggerListener> listeners = new ArrayList(triggerListeners);
    listeners.forEach(triggerListener ->  triggerListener.getTrigger().trigger());

//      (new HashSet<>(triggerListeners)).forEach(triggerListener -> triggerListener.getTrigger().trigger());
  }

  public void clear() { this.clear(t -> true); }

  public void clear(Predicate predicate)  {
    List<TriggerListener> listenersToClear = triggerListeners.stream()
        .filter(t -> predicate.test(t))
        .collect(Collectors.toList());
    triggerListeners.removeAll(listenersToClear);
  }
}

package thesimpleton.utilities;

import thesimpleton.TheSimpletonMod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TriggerManager {
  private final ArrayList<TriggerListener> triggerListeners;
  public final String name;

  public TriggerManager(String name) { this(name, new ArrayList<>()); }

  public TriggerManager(String name, List<TriggerListener> triggerListeners) {
    this.triggerListeners = new ArrayList<>(triggerListeners);
    this.name = name;
    TheSimpletonMod.logger.debug("TriggerManager [" + this.name + "] instantiated with " + this.triggerListeners.size() + " triggers");
  }

  public int numTriggerListeners() {
    return triggerListeners.size();
  }

  public void addTriggerListener(TriggerListener triggerListener) {
    triggerListeners.add(triggerListener);
    TheSimpletonMod.logger.debug("TriggerManager [" + this.name + "] addTriggerListener called");
  }

  public void addTrigger(Trigger trigger) {
    triggerListeners.add(() -> trigger);
  }

  public void addTriggerListeners(List<TriggerListener> triggerListeners) { triggerListeners.addAll(triggerListeners);  }

  public void removeTriggerListener(TriggerListener triggerListener) {
    triggerListeners.remove(triggerListener);
  }

  public void triggerAll() {
    TheSimpletonMod.logger.debug("TriggerManager.triggerAll [" + this.name + "] triggering " + this.triggerListeners.size() + " triggers");
    // copy to prevent concurrent modification
    List<TriggerListener> listeners = new ArrayList(triggerListeners);
    listeners.forEach(triggerListener ->  triggerListener.getTrigger().trigger());
  }

  public void clear() { this.clear(t -> true); }

  public void clear(Predicate predicate)  {
    long numTriggersToClear = triggerListeners.stream()
        .filter(t -> predicate.test(t)).count();

    TheSimpletonMod.logger.debug("TriggerManager.clear [" + this.name + "] clearing " + numTriggersToClear + " triggers");

    List<TriggerListener> listenersToClear = triggerListeners.stream()
        .filter(t -> predicate.test(t))
        .collect(Collectors.toList());
    triggerListeners.removeAll(listenersToClear);
  }
}

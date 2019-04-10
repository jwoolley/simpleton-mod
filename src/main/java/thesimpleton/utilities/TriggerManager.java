package thesimpleton.utilities;

import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TriggerManager {
  private ArrayList<Trigger> triggers;


  public TriggerManager() {
    this(new ArrayList<>());
  }

  public TriggerManager(List<Trigger> triggers) {
    this.triggers = new ArrayList<>(triggers);
    TheSimpletonMod.logger.debug("TriggerManager: Instantiated with " + this.triggers.size() + " triggers");
  }

  public int numTriggers() {
    return triggers.size();
  }

  public void addTrigger(Trigger trigger) {
    triggers.add(trigger);
  }

  public void addTriggers(List<Trigger> triggers) { triggers.addAll(triggers);  }

  public void removeTrigger(Trigger trigger) {
    triggers.remove(trigger);
  }

  public void triggerAll() {
    TheSimpletonMod.logger.debug("TriggerManager.triggerAll triggering " + this.triggers.size() + " triggers");
    (new HashSet<>(triggers)).forEach(trigger -> trigger.trigger());
  }

  public void clear()  { triggers.clear(); }
}

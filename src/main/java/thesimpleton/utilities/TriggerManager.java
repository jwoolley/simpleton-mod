package thesimpleton.utilities;

import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;

import java.util.ArrayList;

public class TriggerManager {
  private ArrayList<Trigger> triggers;

  public TriggerManager() {
    this.triggers = new ArrayList<>();
  }

  public void addTrigger(Trigger trigger) {
    triggers.add(trigger);
  }

  public void removeTrigger(Trigger trigger) {
    triggers.remove(trigger);
  }

  public void triggerAll() {
    triggers.forEach(trigger -> trigger.trigger());
  }

  public void clear()  { triggers.clear(); }
}

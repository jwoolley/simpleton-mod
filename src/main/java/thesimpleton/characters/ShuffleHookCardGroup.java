package thesimpleton.characters;

import java.util.ArrayList;
import com.megacrit.cardcrawl.cards.*;


public class ShuffleHookCardGroup extends CardGroup {

  private ArrayList<ShuffleListener> listeners;

  ShuffleHookCardGroup(CardGroup cardGroup) {
    super(cardGroup, cardGroup.type);
    listeners = new ArrayList<>();
  }

  void registerShuffleListener(ShuffleListener listener) {
    listeners.add(listener);
  }

  @Override
  public void shuffle() {
    super.shuffle();
    triggerListeners();
  }

  @Override
  public void shuffle(com.megacrit.cardcrawl.random.Random rng) {
    System.out.println();
    super.shuffle(rng);
    triggerListeners();
  }

  private void triggerListeners() {
    listeners.forEach(listener -> listener.onShuffle());
  }
}

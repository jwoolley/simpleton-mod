package thesimpleton.relics;

import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class Trowel extends CustomRelic implements CustomSavable<Integer> {
  public Trowel(String id, Texture texture, RelicTier tier, LandingSound sfx) {
    super(id, texture, tier, sfx);
  }

  @Override
  public Integer onSave() {
    return this.counter;
  }

  @Override
  public void onLoad(Integer counter) {
    this.counter = counter;
  }

  @Override
  public void onVictory() {
    flash();
    this.counter++;
    this.updateDescription(AbstractDungeon.player.chosenClass);
  }

}

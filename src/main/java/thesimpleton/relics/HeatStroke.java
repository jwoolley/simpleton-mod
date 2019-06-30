package thesimpleton.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.ApplyBurningAction;
import thesimpleton.characters.TheSimpletonCharacter;

public class HeatStroke extends CustomRelic {
  public static final String ID = "TheSimpletonMod:HeatStroke";
  public static final String IMG_PATH = "relics/heatstroke.png";
  public static final String IMG_PATH_LARGE = "relics/heatstroke_large.png";
  public static final String OUTLINE_IMG_PATH = "relics/heatstroke_outline.png";

  private static final RelicTier TIER = RelicTier.SPECIAL;
  private static final LandingSound SOUND = LandingSound.FLAT;

  private boolean firstTurn = true;
  private final int burningAmount;

  public HeatStroke(int burningAmount) {
    super(ID, new Texture(TheSimpletonMod.getResourcePath(IMG_PATH)),
        new Texture(TheSimpletonMod.getResourcePath(OUTLINE_IMG_PATH)), TIER, SOUND);
    this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getResourcePath(IMG_PATH_LARGE));
    this.counter = this.burningAmount = burningAmount;
    updateDescription(AbstractDungeon.player.chosenClass);
  }


  @Override
  public void atPreBattle() {
    this.firstTurn = true;
  }

  @Override
  public void atTurnStart() {
    if (this.firstTurn) {
      this.flash();
      if (counter > 0) {
        AbstractDungeon.actionManager.addToBottom(
            new ApplyBurningAction(AbstractDungeon.player, AbstractDungeon.player, this.burningAmount));
      }
      this.counter = -1;
      this.firstTurn = false;
      this.usedUp();
    }
  }

  @Override
  public String getUpdatedDescription() {
    return this.DESCRIPTIONS[0] + this.burningAmount + this.DESCRIPTIONS[1];
  }

  public void updateDescription(AbstractPlayer.PlayerClass c) {
    this.description = getUpdatedDescription();
    this.tips.clear();
    this.tips.add(new PowerTip(this.name, this.description));
    initializeTips();
  }

  public void removeIfUsed(){
    // TODO: use actions to ensure the flash goes off
    this.flash();
    AbstractDungeon.player.relics.removeIf(r -> AbstractDungeon.player.hasRelic(r.relicId) && r.usedUp);
    AbstractDungeon.player.reorganizeRelics();
  }

  @Override
  public AbstractRelic makeCopy() {
    return new HeatStroke(this.burningAmount);
  }
}

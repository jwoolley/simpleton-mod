package thesimpleton.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thesimpleton.TheSimpletonMod;

public class GrassPellets extends CustomRelic {
  public static final String ID = "TheSimpletonMod:GrassPellets";
  public static final String IMG_PATH = "relics/grasspellets.png";
  public static final String IMG_PATH_LARGE = "relics/grasspellets_large.png";
  public static final String OUTLINE_IMG_PATH = "relics/grasspellets_outline.png";

  private static final RelicTier TIER = RelicTier.SHOP;
  private static final LandingSound SOUND = LandingSound.CLINK;

  private static final int STACK_AMOUNT = 3;

  public GrassPellets() {
    super(ID, new Texture(TheSimpletonMod.getResourcePath(IMG_PATH)),
        new Texture(TheSimpletonMod.getResourcePath(OUTLINE_IMG_PATH)), TIER, SOUND);
    this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getResourcePath(IMG_PATH_LARGE));
    this.counter = -1;
  }

  @Override
  public String getUpdatedDescription() {
    return this.DESCRIPTIONS[0] + STACK_AMOUNT + this.DESCRIPTIONS[1];
  }


  @Override
  public void atTurnStart() {
    this.counter = 0;
  }

  @Override
  public void onVictory() {
    this.counter = -1;
  }

  public void increaseCountAndMaybeActivate() {
    this.counter++;
    if (this.counter > 0 && this.counter % STACK_AMOUNT == 0) {
      this.counter = 0;
      this.flash();
      AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
      AbstractDungeon.actionManager.addToBottom(new RemoveDebuffsAction(AbstractDungeon.player));
    }
  }

  @Override
  public AbstractRelic makeCopy() {
    return new GrassPellets();
  }

}

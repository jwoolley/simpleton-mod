package thesimpleton.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.WoodChipperAction;

public class WoodChipper extends CustomRelic {
  public static final String ID = "TheSimpletonMod:WoodChipper";
  public static final String IMG_PATH = "relics/woodchipper.png";
  public static final String IMG_PATH_LARGE = "relics/woodchipper_large.png";
  public static final String OUTLINE_IMG_PATH = "relics/woodchipper_outline.png";

  private static final RelicTier TIER = RelicTier.COMMON;
  private static final LandingSound SOUND = LandingSound.SOLID;

  private static final int MAX_NUM_CARDS = 3;
  private boolean activated = false;

  public WoodChipper() {
    super(ID, new Texture(TheSimpletonMod.getResourcePath(IMG_PATH)),
        new Texture(TheSimpletonMod.getResourcePath(OUTLINE_IMG_PATH)), TIER, SOUND);
    this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getResourcePath(IMG_PATH_LARGE));
  }

  @Override
  public String getUpdatedDescription() {
    return this.DESCRIPTIONS[0] + MAX_NUM_CARDS + this.DESCRIPTIONS[1];
  }

  public void atBattleStartPreDraw()
  {
    this.activated = false;
  }

  @Override
  public void atTurnStartPostDraw() {
    if (!this.activated) {
      this.activated = true;
      flash();
      AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
      this.flash();
      AbstractDungeon.actionManager.addToBottom(new WoodChipperAction(AbstractDungeon.player, MAX_NUM_CARDS));
    }
  }

  @Override
  public AbstractRelic makeCopy() {
    return new WoodChipper();
  }
}
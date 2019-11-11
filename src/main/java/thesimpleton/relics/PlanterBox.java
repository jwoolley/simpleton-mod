package thesimpleton.relics;

import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thesimpleton.TheSimpletonMod;
import thesimpleton.powers.AbundancePower;

public class PlanterBox extends CustomRelic implements CustomSavable<Integer> {
  public static final String ID = "TheSimpletonMod:PlanterBox";
  public static final String IMG_PATH = "relics/planterbox.png";
  public static final String IMG_PATH_LARGE = "relics/planterbox_large.png";
  public static final String OUTLINE_IMG_PATH = "relics/planterbox_outline.png";

  private static final RelicTier TIER = RelicTier.UNCOMMON;
  private static final LandingSound SOUND = LandingSound.SOLID;

  private static final int TURNS_PER_TRIGGER = 3;
  private static final int PLOT_AMOUNT = 1;
  private boolean firstTurn = true;

  public PlanterBox() {
    super(ID, new Texture(TheSimpletonMod.getResourcePath(IMG_PATH)),
        new Texture(TheSimpletonMod.getResourcePath(OUTLINE_IMG_PATH)), TIER, SOUND);
    this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getResourcePath(IMG_PATH_LARGE));
  }


  private int getPlotAmount() {
    return PLOT_AMOUNT;
  }

  @Override
  public void atPreBattle() {
    this.firstTurn = true;
  }

  @Override
  public void atTurnStart() {
    this.counter++;
    if (this.counter >= TURNS_PER_TRIGGER) {
      flash();
      AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
      AbstractDungeon.actionManager.addToBottom(new IncreaseMaxOrbAction(getPlotAmount()));
      this.counter = 0;
    }
  }

  @Override
  public String getUpdatedDescription() {
    final int plotAmount = getPlotAmount();
    return this.DESCRIPTIONS[0] + TURNS_PER_TRIGGER + DESCRIPTIONS[1]
        + plotAmount + (plotAmount == 1 ? DESCRIPTIONS[2] : DESCRIPTIONS[3]);
  }

  @Override
  public AbstractRelic makeCopy() {
    return new PlanterBox();
  }

  @Override
  public Integer onSave() {
    return this.counter;
  }

  @Override
  public void onLoad(Integer counter) {
    this.counter = counter;
  }
}

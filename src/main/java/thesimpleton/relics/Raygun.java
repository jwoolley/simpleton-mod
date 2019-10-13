package thesimpleton.relics;

import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.RaygunAction;
import thesimpleton.cards.SimpletonUtil;


public class Raygun extends CustomRelic implements CustomSavable<Integer> {
  public static final String ID = "TheSimpletonMod:Raygun";
  public static final String IMG_PATH = "relics/raygun.png";
  public static final String IMG_PATH_LARGE = "relics/raygun_large.png";
  public static final String OUTLINE_IMG_PATH = "relics/raygun_outline.png";

  private static final RelicTier TIER = RelicTier.SPECIAL;
  private static final LandingSound SOUND = LandingSound.MAGICAL;

  private static final int CHARGES_PER_ENERGY = 5;

  public Raygun() {
    super(ID, new Texture(TheSimpletonMod.getResourcePath(IMG_PATH)),
        new Texture(TheSimpletonMod.getResourcePath(OUTLINE_IMG_PATH)), TIER, SOUND);
    this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getResourcePath(IMG_PATH_LARGE));
  }

  @Override
  public void onPlayerEndTurn() {
    final int energy = EnergyPanel.getCurrentEnergy();
    if (energy > 0) {
      AbstractDungeon.actionManager.addToBottom(new SFXAction("CHARGE_LASER_1"));
      this.flash();
      this.counter += energy * CHARGES_PER_ENERGY;
    } else if (this.counter > 0) {
      this.flash();
      this.activate(this.counter);
      this.counter = 0;
    }
  }

  public void clearCharges() {
    this.counter = 0;
  }

  @Override
  public String getUpdatedDescription() {
    return this.DESCRIPTIONS[0] + CHARGES_PER_ENERGY + DESCRIPTIONS[1];
  }

  private void activate(int damageAmount) {
    AbstractDungeon.actionManager.addToBottom(
        new RaygunAction(this, AbstractDungeon.player, SimpletonUtil.getRandomMonster(), damageAmount));
  }

  @Override
  public AbstractRelic makeCopy() {
    return new Raygun();
  }

  @Override
  public void onEquip() {
    this.counter = 0;
  }

  @Override
  public Integer onSave() {
    return this.counter;
  }

  @Override
  public void onLoad(Integer savedChargeAmount) {
    this.counter = savedChargeAmount;
  }
}

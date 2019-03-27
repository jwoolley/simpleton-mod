package thesimpleton.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.CurseUtil;
import thesimpleton.powers.AbstractCropPower;
import thesimpleton.powers.PlantPotatoPower;

import java.util.List;
import java.util.stream.Collectors;

// TODO: reflavor as fertilizer; reuse "the harvester" + icon for a different relic

public class TheHarvester extends CustomRelic {
  public static final String ID = "TheSimpletonMod:TheHarvester";
  public static final String IMG_PATH = "relics/theharvester.png";
  public static final String IMG_PATH_LARGE = "relics/theharvester_large.png";
  public static final String OUTLINE_IMG_PATH = "relics/theharvester_outline.png";

  private static final RelicTier TIER = RelicTier.STARTER;
  private static final LandingSound SOUND = LandingSound.HEAVY;

  private static final int CROP_AMOUNT = 1;

  public TheHarvester() {
    super(ID, new Texture(TheSimpletonMod.getResourcePath(IMG_PATH)),
        new Texture(TheSimpletonMod.getResourcePath(OUTLINE_IMG_PATH)), TIER, SOUND);
    this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getResourcePath(IMG_PATH_LARGE));
  }

  @Override
  public String getUpdatedDescription() {
    return this.DESCRIPTIONS[0];
  }

  @Override
  public void onShuffle() {
    List<AbstractCropPower> eligiblePowers = AbstractCropPower.getActiveCropPowers().stream()
        .filter(power -> power.amount < 5)
        .collect(Collectors.toList());

    eligiblePowers.forEach(power -> power.stackPower(CROP_AMOUNT));

    if (eligiblePowers.size() > 0) {
      flash();
    }
  }

  @Override
  public AbstractRelic makeCopy() {
    return new TheHarvester();
  }
}

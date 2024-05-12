package thesimpleton.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.crops.Crop;

import java.util.ArrayList;
import java.util.List;

public class CashCrop extends CustomRelic {
  public static final String ID = "TheSimpletonMod:CashCrop";
  public static final String IMG_PATH = "relics/cashcrop.png";
  public static final String IMG_PATH_LARGE = "relics/cashcrop_large.png";
  public static final String OUTLINE_IMG_PATH = "relics/cashcrop_outline.png";

  private static final RelicTier TIER = RelicTier.UNCOMMON;
  private static final LandingSound SOUND = LandingSound.CLINK;

  private static final int GOLD_PER_TRIGGER = 5;

  private final List<Crop> cropsHarvestedThisCombat;

  public CashCrop() {
    super(ID, new Texture(TheSimpletonMod.getImageResourcePath(IMG_PATH)),
        new Texture(TheSimpletonMod.getImageResourcePath(OUTLINE_IMG_PATH)), TIER, SOUND);
    this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getImageResourcePath(IMG_PATH_LARGE));
    cropsHarvestedThisCombat = new ArrayList<>();
  }

  @Override
  public String getUpdatedDescription() {
    return this.DESCRIPTIONS[0] + GOLD_PER_TRIGGER + this.DESCRIPTIONS[1];
  }

  @Override
  public void atBattleStartPreDraw() {
    cropsHarvestedThisCombat.clear();
  }

  public void onHarvest(Crop crop) {
    if (!cropsHarvestedThisCombat.contains(crop)) {
      cropsHarvestedThisCombat.add(crop);
      this.flash();

      AbstractPlayer player = AbstractDungeon.player;
      player.gainGold(GOLD_PER_TRIGGER);
      for (int i = 0; i < 5; i++) {
        AbstractDungeon.effectList.add(new GainPennyEffect(player, player.hb.cX, player.hb.cY, player.hb.cX, player.hb.cY, true));
      }
    }
  }

  @Override
  public AbstractRelic makeCopy() {
    return new CashCrop();
  }
}

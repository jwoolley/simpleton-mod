package thesimpleton.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.crops.Crop;

import java.util.ArrayList;
import java.util.List;

public class Honeycomb extends CustomRelic {
  public static final String ID = "TheSimpletonMod:Honeycomb";
  public static final String IMG_PATH = "relics/honeycomb.png";
  public static final String IMG_PATH_LARGE = "relics/honeycomb_large.png";
  public static final String OUTLINE_IMG_PATH = "relics/honeycomb_outline.png";

  private static final RelicTier TIER = RelicTier.UNCOMMON;
  private static final LandingSound SOUND = LandingSound.FLAT;

  private static final int DAMAGE_PER_TRIGGER = 1;
  private static final DamageInfo.DamageType DAMAGE_TYPE = DamageInfo.DamageType.THORNS;

  private final List<Crop> cropsHarvestedThisCombat;

  public Honeycomb() {
    super(ID, new Texture(TheSimpletonMod.getImageResourcePath(IMG_PATH)),
        new Texture(TheSimpletonMod.getImageResourcePath(OUTLINE_IMG_PATH)), TIER, SOUND);
    this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getImageResourcePath(IMG_PATH_LARGE));
    cropsHarvestedThisCombat = new ArrayList<>();
  }

  @Override
  public String getUpdatedDescription() {
    return this.DESCRIPTIONS[0] + DAMAGE_PER_TRIGGER + this.DESCRIPTIONS[1];
  }

  @Override
  public void atBattleStartPreDraw() {
    cropsHarvestedThisCombat.clear();
  }

  public void onPlantCrop(Crop crop) {
    Logger logger = TheSimpletonMod.logger;

    AbstractDungeon.actionManager.addToTop(new SFXAction("ATTACK_BEE_BUZZ_1"));
    if (!cropsHarvestedThisCombat.contains(crop)) {
      this.flash();

      final AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
      if (target != null) {
        AbstractDungeon.actionManager.addToTop(
            new DamageAction(target,
                new DamageInfo(AbstractDungeon.player, DAMAGE_PER_TRIGGER, DAMAGE_TYPE),  AbstractGameAction.AttackEffect.BLUNT_LIGHT));
      }
    }
  }

  @Override
  public AbstractRelic makeCopy() {
    return new Honeycomb();
  }
}
package thesimpleton.potions;

import basemod.abstracts.CustomPotion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.StaffFireEffect;
import com.megacrit.cardcrawl.vfx.combat.GiantFireEffect;
import com.megacrit.cardcrawl.vfx.combat.RedFireballEffect;
import thesimpleton.actions.ApplyBurningAction;

import java.util.ArrayList;
import java.util.Collections;


public class KindlingPotion extends CustomPotion {
  public static final String POTION_ID = "TheSimpletonMod:KindlingPotion";
  private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
  public static final String NAME = potionStrings.NAME;
  public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

  public static final PotionSize POTION_SHAPE = PotionSize.BOTTLE;
  public static final PotionColor POTION_COLOR = PotionColor.POISON;
  public static final int POTENCY = 15;

  public static final Color BASE_COLOR = Color.SCARLET.cpy();
  public static final Color HYBRID_COLOR = Color.RED.cpy();
  public static final Color SPOTS_COLOR = Color.CLEAR.cpy();

  public KindlingPotion() {
    super(NAME, POTION_ID, PotionRarity.UNCOMMON, POTION_SHAPE, POTION_COLOR);
    this.isThrown = false;
    this.targetRequired = false;

    PotionStrings potionKeywordStrings =
        CardCrawlGame.languagePack.getPotionString("TheSimpletonMod:KindlingPotionKeyword");

    this.tips.add(new PowerTip(potionKeywordStrings.NAME, potionKeywordStrings.DESCRIPTIONS[0]));
  }

  public void use(AbstractCreature target) {
    final ArrayList<AbstractMonster> monsters = new ArrayList<>(AbstractDungeon.getCurrRoom().monsters.monsters);
    Collections.shuffle(monsters);

    for (AbstractMonster monster : monsters) {
      AbstractDungeon.actionManager.addToBottom(new VFXAction(new GiantFireEffect()));
      AbstractDungeon.actionManager.addToBottom(new VFXAction(new RedFireballEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.y, monster.hb.cX, monster.hb.cY, 0)));
      AbstractDungeon.actionManager.addToBottom(new VFXAction(new StaffFireEffect(monster.hb.cX, monster.hb.y)));

      AbstractDungeon.actionManager.addToBottom(
          new ApplyBurningAction(monster, AbstractDungeon.player, this.getPotency()));
    }
  }

  public CustomPotion makeCopy() {
    return new KindlingPotion();
  }

  @Override
  public void initializeData() {
    this.potency = getPotency();
    this.description = (DESCRIPTIONS[0] + this.potency + DESCRIPTIONS[1]);
    this.tips.clear();
    this.tips.add(new PowerTip(this.name, this.description));
  }

  @Override
  public int getPotency(int ascensionLevel) {
    return POTENCY;
  }

  @Override
  public int getPotency()
  {
    int potency = getPotency(AbstractDungeon.ascensionLevel);
    if ((AbstractDungeon.player != null) && (AbstractDungeon.player.hasRelic("SacredBark"))) {
      potency *= 2;
    }
    return potency;
  }
}
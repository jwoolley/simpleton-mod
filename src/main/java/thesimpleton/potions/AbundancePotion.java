package thesimpleton.potions;

import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.badlogic.gdx.graphics.Color;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.powers.AbundancePower;


public class AbundancePotion extends CustomPotion {
  public static final String POTION_ID = "TheSimpletonMod:AbundancePotion";
  private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
  public static final String NAME = potionStrings.NAME;
  public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

  public static final PotionSize POTION_SHAPE = PotionSize.HEART;
  public static final PotionColor POTION_COLOR = PotionColor.POISON;
  public static final int POTENCY = 1;

  public static final Color BASE_COLOR = Color.GOLD;
  public static final Color HYBRID_COLOR = Color.CLEAR;
  public static final Color SPOTS_COLOR = Color.RED;

  public AbundancePotion() {
    super(NAME, POTION_ID, PotionRarity.UNCOMMON, POTION_SHAPE, POTION_COLOR);
    this.potency = getPotency();
    this.description = (DESCRIPTIONS[0] + this.potency + DESCRIPTIONS[1]);
    this.isThrown = false;
    this.targetRequired = false;
    this.tips.add(new PowerTip(this.name, this.description));

    PotionStrings potionKeywordStrings =
        CardCrawlGame.languagePack.getPotionString("TheSimpletonMod:AbundancePotionKeyword");

    this.tips.add(new PowerTip(potionKeywordStrings.NAME, potionKeywordStrings.DESCRIPTIONS[0]));
  }

  public void use(AbstractCreature target) {
    AbstractPlayer player = SimpletonUtil.getPlayer();
    final int potency = this.getPotency();
    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(player,player, new AbundancePower(player, player, potency), potency));
  }


  public CustomPotion makeCopy() {
    return new AbundancePotion();
  }

  public int getPotency(int ascensionLevel) {
    return POTENCY;
  }
}
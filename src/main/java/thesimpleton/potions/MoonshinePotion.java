package thesimpleton.potions;

import basemod.abstracts.CustomPotion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.powers.ConfusionPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import thesimpleton.powers.IntoxicatedPower;
import thesimpleton.powers.SoberUpPower;


public class MoonshinePotion extends CustomPotion {
  public static final String POTION_ID = "TheSimpletonMod:MoonshinePotion";
  private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
  public static final String NAME = potionStrings.NAME;
  public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

  private static final PotionSize POTION_SHAPE = PotionSize.JAR;
  private static final PotionColor POTION_COLOR = PotionColor.NONE;

  private static final int CARD_DRAW_AMOUNT = 5;
  private static final int STRENGTH_AMOUNT = 5;
  private static final int POTENCY = 1;

  public static final Color BASE_COLOR = new Color(1.0F, 1.0F, 1.0F, 0.50F);
  public static final Color HYBRID_COLOR = BASE_COLOR;
  public static final Color SPOTS_COLOR = new Color(1.0F, 1.0F, 1.0F, 0.6F);

  public MoonshinePotion() {
    super(NAME, POTION_ID, PotionRarity.UNCOMMON, POTION_SHAPE, POTION_COLOR);
    this.isThrown = false;
    this.targetRequired = false;

    PotionStrings potionKeywordStrings =
        CardCrawlGame.languagePack.getPotionString("TheSimpletonMod:MoonshinePotionKeyword");

    this.tips.add(new PowerTip(potionKeywordStrings.NAME, potionKeywordStrings.DESCRIPTIONS[0]));
  }

  public void use(AbstractCreature target) {
    AbstractPlayer player = AbstractDungeon.player;

    AbstractDungeon.actionManager.addToBottom(new SFXAction("DRINK_BOTTLE_1"));
    AbstractDungeon.actionManager.addToBottom(new SFXAction("GIBBERISH_ANGRY_1"));
    AbstractDungeon.actionManager.addToBottom(
        new TalkAction(true, DESCRIPTIONS[3], 1.0F, 2.0F));

    AbstractDungeon.actionManager.addToBottom(
        new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, AbstractDungeon.player.hand.size(),
            false));

    final int strength = STRENGTH_AMOUNT * this.getPotency();
    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player,
        new StrengthPower(player, strength), strength));
    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player,
        new LoseStrengthPower(player, strength), strength));

    final boolean wasConfused = player.hasPower(ConfusionPower.POWER_ID);

    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new IntoxicatedPower(player)));
    if (!wasConfused) {
      AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new SoberUpPower(player)));
    }

    AbstractDungeon.actionManager.addToBottom(new DrawCardAction(player, CARD_DRAW_AMOUNT * this.getPotency()));
  }

  @Override
  public int getPotency(int ascensionLevel) {
    return POTENCY;
  }

  @Override
  public void initializeData() {
    this.potency = getPotency();
    this.description = getDescription(this.potency);
    this.tips.clear();
    this.tips.add(new PowerTip(this.name, this.description));
  }

  private static String getDescription(int potency) {
   return (DESCRIPTIONS[0]
        + (CARD_DRAW_AMOUNT * potency)
        + DESCRIPTIONS[1]
        + (STRENGTH_AMOUNT * potency)
        + DESCRIPTIONS[2]);
  }

  public CustomPotion makeCopy() {
    return new MoonshinePotion();
  }
}
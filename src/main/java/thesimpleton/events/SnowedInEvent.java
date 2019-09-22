package thesimpleton.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.FrozenEgg2;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.curse.Frostbite;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Potatoes;
import thesimpleton.cards.skill.StockTheCellar;

public class SnowedInEvent extends CustomSimpletonEvent implements CustomSimpletonOnlyEvent
{
  public static final String ID = TheSimpletonMod.makeID("SnowedInEvent");

  private static final String IMG_PATH = SimpletonEventHelper.getUiPath("snowedin1");
  private static final EventStrings eventStrings;
  private static final String NAME;
  private static final String[] DESCRIPTIONS;
  private static final String[] OPTIONS;

  private static final AbstractCard CURSE_CARD = new Frostbite();
  private final AbstractCard CARD_REWARD = new StockTheCellar();
  private final AbstractRelic RELIC_REWARD = new FrozenEgg2();

  private final int NUM_REWARD_CARDS_1 = 2;
  private final int NUM_REWARD_CARDS_2 = 2;

  private static final int MIN_GOLD_COST = 20;
  private static final int MAX_GOLD_COST = 40;
  private static final int MIN_DAMAGE_AMOUNT = 10;
  private static final int MAX_DAMAGE_AMOUNT = 25;
  private final AbstractCropPowerCard cropCardReward;
  private final AbstractRelic relicReward;
  private final int goldCost;
  private final int hpCost;
  private boolean receivesCurse;

  private SimpletonEventHelper.EventState state;

  /*
  "OPTIONS": [
        0  "[Ration] #gReceive #g",
        1  "[Ransack] #gReceive ",
        2  "[Burrow] #gGain ",
        3  ". #rLose #r",
        4  " #rGold.",
        5 " #rHP.",
        6  ". #r50%: #rBecome #rCursed #r– #r",
        7  ".",
        8  "[Locked] Requires: At Least ",
        9  "Leave."
          ]
    */

  public SnowedInEvent() {
    super(NAME, DESCRIPTIONS[0],  TheSimpletonMod.getResourcePath(IMG_PATH));

    goldCost = SimpletonEventHelper.getGoldCost(MIN_GOLD_COST, MAX_GOLD_COST);
    hpCost = SimpletonEventHelper.getIntInRange(MIN_DAMAGE_AMOUNT, MAX_DAMAGE_AMOUNT);
    cropCardReward = SimpletonEventHelper.getSeasonalCropPowerCard(AbstractCard.CardRarity.COMMON, new Potatoes());

    cropCardReward.upgrade();

    if (RELIC_REWARD.canSpawn() && !AbstractDungeon.player.hasRelic(RELIC_REWARD.relicId)) {
      relicReward = RELIC_REWARD;
    } else {
      relicReward = AbstractDungeon.returnRandomScreenlessRelic(AbstractRelic.RelicTier.UNCOMMON);
    }

    if (goldCost > 0) {
        this.imageEventText.setDialogOption(OPTIONS[0] + NUM_REWARD_CARDS_1 + " " + CARD_REWARD.name + OPTIONS[3]
          + goldCost + OPTIONS[4], CARD_REWARD);
    } else {
      this.imageEventText.setDialogOption(OPTIONS[8] + MIN_GOLD_COST + OPTIONS[4], true,
          CARD_REWARD);
    }

    this.imageEventText.setDialogOption(OPTIONS[1] + NUM_REWARD_CARDS_2 + " " + cropCardReward.name + OPTIONS[3]
        + hpCost + OPTIONS[5], cropCardReward);

    this.imageEventText.setDialogOption(OPTIONS[2] + relicReward.name + OPTIONS[6] + CURSE_CARD.name + OPTIONS[7],
        CURSE_CARD, relicReward);

    this.state = SimpletonEventHelper.EventState.WAITING;

    // TODO: replace with howling wind sound
    CardCrawlGame.sound.play("WIND_HOWL_1");
  }

  @Override
  protected void buttonEffect(int buttonPressed) {
    switch (state) {
      case WAITING:
        switch (buttonPressed) {

          case 0:
            SimpletonEventHelper.gainCards(CARD_REWARD, CARD_REWARD);
            AbstractDungeon.player.loseGold(this.goldCost);
            break;

          case 1:
            SimpletonEventHelper.gainCards(cropCardReward, cropCardReward);
            AbstractDungeon.player.damage(new DamageInfo(null, hpCost, DamageInfo.DamageType.HP_LOSS));
            break;

          case 2:
            SimpletonEventHelper.receiveRelic(relicReward);
            receivesCurse = AbstractDungeon.miscRng.randomBoolean(0.5F);
            if (receivesCurse) {
              // TODO: replace with "cold" sound
              CardCrawlGame.sound.play("RAGE");
              SimpletonEventHelper.gainCard(CURSE_CARD);
            }
            break;

          default:
            break;
        }
        this.imageEventText.clearAllDialogs();
        this.imageEventText.setDialogOption(OPTIONS[9]);
        this.state = SimpletonEventHelper.EventState.LEAVING;
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        break;
      case LEAVING:
        this.imageEventText.clearAllDialogs();
        this.imageEventText.setDialogOption(OPTIONS[9]);
        openMap();
        break;
    }
  }

  static {
    eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    NAME = eventStrings.NAME;
    DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    OPTIONS = eventStrings.OPTIONS;
  }
}
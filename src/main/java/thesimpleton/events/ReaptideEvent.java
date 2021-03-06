package thesimpleton.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Doubt;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.curse.Spoilage;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Corn;
import thesimpleton.cards.power.crop.Potatoes;
import thesimpleton.crops.Crop;
import thesimpleton.relics.GourdCharm;
import thesimpleton.relics.OnionBelt;

public class ReaptideEvent extends CustomSimpletonEvent implements CustomSimpletonOnlyEvent {
  public static final String ID = TheSimpletonMod.makeID("ReaptideEvent");

  private static final String IMG_PATH = SimpletonEventHelper.getUiPath("reaptide1");
  private static final EventStrings eventStrings;
  private static final String NAME;
  private static final String[] DESCRIPTIONS;
  private static final String[] OPTIONS;

  private static final int MIN_GOLD_COST = 25;
  private static final int MAX_GOLD_COST = 55;

  private static final AbstractCard CURSE_DOUBT = new Doubt();
  private static final AbstractCard CURSE_SPOILAGE = new Spoilage();
  private static final AbstractRelic GOURD_CHARM = new GourdCharm();
  private static final AbstractRelic ONION_BELT = new OnionBelt();
  private static final int BOSS_RELIC_CURSE_CHANCE_PERCENTAGE = 100;
  private static final int CURSE_CHANCE_PERCENTAGE = 33;

  private final AbstractCard curseCard;
  private final AbstractCropPowerCard commonCropCard;
  private final AbstractCropPowerCard uncommonCropCard;
  private final AbstractRelic relicReward;
  private final int goldCost;
  private final int curseChancePercentage;

  private SimpletonEventHelper.EventState state;

  public ReaptideEvent() {
    super(NAME, DESCRIPTIONS[0],  TheSimpletonMod.getImageResourcePath(IMG_PATH));

    commonCropCard = SimpletonEventHelper.getSeasonalCropPowerCard(AbstractCard.CardRarity.COMMON, new Potatoes());
    commonCropCard.upgrade();

    uncommonCropCard = SimpletonEventHelper.getSeasonalCropPowerCard(AbstractCard.CardRarity.UNCOMMON, new Corn());
    uncommonCropCard.upgrade();

    goldCost = SimpletonEventHelper.getGoldCost(MIN_GOLD_COST, MAX_GOLD_COST);

    if (TheSimpletonMod.getSeasonInfo().isInSeason(Crop.SQUASH) && GOURD_CHARM.canSpawn()
        && !AbstractDungeon.player.hasRelic(GourdCharm.ID)) {
      relicReward = GOURD_CHARM;
      curseChancePercentage = BOSS_RELIC_CURSE_CHANCE_PERCENTAGE;
    } else if (TheSimpletonMod.getSeasonInfo().isInSeason(Crop.ONIONS) && ONION_BELT.canSpawn()
        && !AbstractDungeon.player.hasRelic(OnionBelt.ID)) {
      relicReward = ONION_BELT;
      curseChancePercentage = BOSS_RELIC_CURSE_CHANCE_PERCENTAGE;
    } else {
      relicReward = AbstractDungeon.relicRng.randomBoolean() ?
          AbstractDungeon.returnRandomScreenlessRelic(AbstractRelic.RelicTier.UNCOMMON) :
          AbstractDungeon.returnRandomScreenlessRelic(AbstractRelic.RelicTier.COMMON);
      curseChancePercentage = CURSE_CHANCE_PERCENTAGE;
    }

    curseCard = AbstractDungeon.miscRng.randomBoolean(0.33F) ? CURSE_DOUBT : CURSE_SPOILAGE;

    this.imageEventText.setDialogOption(OPTIONS[0] + commonCropCard.name + OPTIONS[3], commonCropCard);

    if (goldCost > 0) {
      this.imageEventText.setDialogOption(OPTIONS[1] + uncommonCropCard.name + OPTIONS[4] + goldCost + OPTIONS[5],
          uncommonCropCard);
    } else {
      this.imageEventText.setDialogOption(OPTIONS[9] + MIN_GOLD_COST + OPTIONS[5], true,
          uncommonCropCard);
    }

    this.imageEventText.setDialogOption(
        OPTIONS[2] + relicReward.name
        + (this.curseChancePercentage < 100 ? OPTIONS[6] + curseChancePercentage + OPTIONS[7] : OPTIONS[3])
        + OPTIONS[8] + curseCard.name + OPTIONS[3], curseCard, relicReward);

    this.state = SimpletonEventHelper.EventState.WAITING;
    CardCrawlGame.sound.play("HOOTING_BIRD_1");
  }

  @Override
  protected void buttonEffect(int buttonPressed) {
    switch (state) {
      case WAITING:
        switch (buttonPressed) {

          case 0:
            SimpletonEventHelper.gainCard(commonCropCard);
            break;

          case 1:
            SimpletonEventHelper.gainCard(uncommonCropCard);
            AbstractDungeon.player.loseGold(this.goldCost);
            break;

          case 2:
            SimpletonEventHelper.receiveRelic(relicReward);
            final boolean receivesCurse = AbstractDungeon.miscRng.randomBoolean(curseChancePercentage / 100.0F);
            if (receivesCurse) {
              if (curseCard.cardID == CURSE_DOUBT.cardID) {
                CardCrawlGame.sound.play("HEART_BEAT");
              } else {
                CardCrawlGame.sound.play("RAGE");
              }
              SimpletonEventHelper.gainCard(curseCard);
            }
          default:
            break;
        }

        // TODO: handle curse outcome with corresponding screens
        this.imageEventText.clearAllDialogs();
        this.imageEventText.setDialogOption(OPTIONS[10]);
        this.state = SimpletonEventHelper.EventState.LEAVING;
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        break;
      case LEAVING:
        this.imageEventText.clearAllDialogs();
        this.imageEventText.setDialogOption(OPTIONS[10]);
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
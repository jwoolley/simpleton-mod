package thesimpleton.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Doubt;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Corn;
import thesimpleton.cards.power.crop.Potatoes;
import thesimpleton.crops.Crop;
import thesimpleton.relics.GourdCharm;
import thesimpleton.relics.OnionBelt;

public class ReaptideEvent extends AbstractImageEvent
{
  public static final String ID = TheSimpletonMod.makeID("ReaptideEvent");

  private static final String IMG_PATH = SimpletonEventHelper.getUiPath("reaptide1");
  private static final EventStrings eventStrings;
  private static final String NAME;
  private static final String[] DESCRIPTIONS;
  private static final String[] OPTIONS;

  private static final AbstractCard CURSE_CARD = new Doubt();
  private static final int MIN_GOLD_COST = 40;
  private static final int MAX_GOLD_COST = 75;

  private final AbstractCropPowerCard commonCropCard;
  private final AbstractCropPowerCard uncommonCropCard;
  private final AbstractRelic relicReward;
  private final AbstractRelic GOURD_CHARM = new GourdCharm();
  private final AbstractRelic ONION_BELT = new OnionBelt();
  private final int goldCost;

  private boolean receivesCurse;

  private SimpletonEventHelper.EventState state;

  public ReaptideEvent() {
    super(NAME, DESCRIPTIONS[0],  TheSimpletonMod.getResourcePath(IMG_PATH));

    commonCropCard = TheSimpletonMod.getSeasonalCropCards()
        .stream().filter(c -> c.rarity == AbstractCard.CardRarity.COMMON)
        .findFirst()
        .orElse(new Potatoes());
    commonCropCard.upgrade();

    uncommonCropCard = TheSimpletonMod.getSeasonalCropCards()
        .stream().filter(c -> c.rarity == AbstractCard.CardRarity.UNCOMMON)
        .findFirst()
        .orElse(new Corn());
    // TODO: Handle case where no upgradable cards

    goldCost = getGoldCost();

    if (TheSimpletonMod.getSeasonInfo().isInSeason(Crop.SQUASH) && GOURD_CHARM.canSpawn()
        && !AbstractDungeon.player.hasRelic(GourdCharm.ID)) {
      relicReward = GOURD_CHARM;
    } else if (TheSimpletonMod.getSeasonInfo().isInSeason(Crop.ONIONS) && ONION_BELT.canSpawn()
        && !AbstractDungeon.player.hasRelic(OnionBelt.ID)) {
      relicReward = ONION_BELT;
    } else {
      relicReward = AbstractDungeon.returnRandomScreenlessRelic(AbstractRelic.RelicTier.COMMON);
    }

    this.imageEventText.setDialogOption(OPTIONS[0] + commonCropCard.name + OPTIONS[3]);

    if (goldCost > 0) {
      this.imageEventText.setDialogOption(OPTIONS[1] + uncommonCropCard.name + OPTIONS[4] + goldCost + OPTIONS[5]);
    } else {
      this.imageEventText.setDialogOption(OPTIONS[8] + MIN_GOLD_COST + OPTIONS[5], true);
    }

    this.imageEventText.setDialogOption(OPTIONS[2] + relicReward.name + OPTIONS[7] + CURSE_CARD.name + OPTIONS[3]);

    this.state = SimpletonEventHelper.EventState.WAITING;
    CardCrawlGame.sound.play("FALL_MEADOW_1");
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
            receivesCurse = AbstractDungeon.miscRng.randomBoolean(0.5F);
            if (receivesCurse) {
              CardCrawlGame.sound.play("HEART_BEAT");
              SimpletonEventHelper.gainCard(CURSE_CARD);
            }
          default:
            break;
        }
        // TOOD: handle 50% curse outcome with corresponding screens
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

  private int getGoldCost()  {
    if (AbstractDungeon.player.gold < MIN_GOLD_COST) {
      return 0;
    } else if (AbstractDungeon.player.gold > MAX_GOLD_COST) {
      return AbstractDungeon.miscRng.random(MIN_GOLD_COST, MAX_GOLD_COST);
    }
    return AbstractDungeon.miscRng.random(MIN_GOLD_COST, AbstractDungeon.player.gold);
  }

  static {
    eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    NAME = eventStrings.NAME;
    DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    OPTIONS = eventStrings.OPTIONS;
  }
}
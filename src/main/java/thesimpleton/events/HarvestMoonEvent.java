package thesimpleton.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.attack.Strike_TheSimpleton;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.events.SimpletonEventHelper.EventState;

import java.util.List;
import java.util.stream.Collectors;

public class HarvestMoonEvent extends CustomSimpletonEvent implements CustomSimpletonOnlyEvent
{
  public static final String ID = TheSimpletonMod.makeID("HarvestMoonEvent");

  private static final String IMG_PATH = SimpletonEventHelper.getUiPath("harvestmoon1");
  private static final EventStrings eventStrings;
  private static final String NAME;
  private static final String[] DESCRIPTIONS;
  private static final String[] OPTIONS;
  private static final int MAX_HP_LOSS_PERCENT = 20;
  private static final int NUM_CROP_CARDS = 8;

  private final int maxHpLoss;

  private EventState state;

  public HarvestMoonEvent() {
    super(NAME, DESCRIPTIONS[0],  TheSimpletonMod.getResourcePath(IMG_PATH));

    maxHpLoss = (int)(AbstractDungeon.player.maxHealth * (MAX_HP_LOSS_PERCENT / 100.f));

    this.imageEventText.setDialogOption(OPTIONS[0]);
    this.imageEventText.setDialogOption(OPTIONS[1] + NUM_CROP_CARDS + OPTIONS[2] + maxHpLoss + OPTIONS[3]);

    this.state = SimpletonEventHelper.EventState.WAITING;
    CardCrawlGame.sound.play("CRICKETS_CHIRP_1");
  }

  @Override
  protected void buttonEffect(int buttonPressed) {
    switch (state) {
      case WAITING:
        switch (buttonPressed) {
          case 0:
            getStrikes().forEach(c -> SimpletonEventHelper.upgradeCard(c));
            break;
          case 1:
            AbstractDungeon.player.decreaseMaxHealth(maxHpLoss);
            performHarvest();
            break;
          default:
            break;
        }
        this.imageEventText.clearAllDialogs();
        this.imageEventText.setDialogOption(OPTIONS[4]);
        this.state = EventState.LEAVING;
        break;
      case LEAVING:
        openMap();
        break;
      default:
        openMap();
    }
  }
  private void performHarvest() {
    List<AbstractCard> strikes = getStrikes();

    List<AbstractCropPowerCard> cropCards =
        AbstractCropPowerCard.getRandomCropPowerCards(NUM_CROP_CARDS, false);

    AbstractDungeon.player.masterDeck.group.removeAll(strikes);

    for (AbstractCropPowerCard card : cropCards) {
      if (AbstractDungeon.relicRng.randomBoolean()) {
        card.upgrade();
      }

      // TODO: show all cards gained (4 cards, twice). Use an action

      SimpletonEventHelper.gainCard(card);
    }

  }

  public List<AbstractCard> getStrikes() {
    return AbstractDungeon.player.masterDeck.group.stream()
        .filter(c -> c instanceof Strike_TheSimpleton).collect(Collectors.toList());
  }

  static {
    eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    NAME = eventStrings.NAME;
    DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    OPTIONS = eventStrings.OPTIONS;
  }
}
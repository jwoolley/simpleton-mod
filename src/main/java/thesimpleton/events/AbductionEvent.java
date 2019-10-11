package thesimpleton.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.attack.GammaBlast;
import thesimpleton.relics.AlienArtifact;

public class AbductionEvent extends CustomSimpletonEvent
{
  public static final String ID = TheSimpletonMod.makeID("AbductionEvent");

  private static final String IMG_PATH = SimpletonEventHelper.getUiPath("abduction1");
  private static final String IMG_PATH_2 = SimpletonEventHelper.getUiPath("abduction2");
  private static final String IMG_PATH_3 = SimpletonEventHelper.getUiPath("abduction3");

  private static final EventStrings eventStrings;
  private static final String NAME;
  private static final String[] DESCRIPTIONS;
  private static final String[] OPTIONS;

  private final AbstractRelic RELIC_REWARD_1 = new AlienArtifact();
  private final AbstractCard CARD_REWARD_1 = new GammaBlast();

  private static final int MIN_GOLD_COST = 35;
  private static final int MAX_GOLD_COST = 60;

  private final int goldCost;

  private SimpletonEventHelper.EventState state;

  public AbductionEvent() {
    super(NAME, DESCRIPTIONS[0],  TheSimpletonMod.getResourcePath(IMG_PATH));

    final AbstractPlayer player = AbstractDungeon.player;

    goldCost = SimpletonEventHelper.getGoldCost(MIN_GOLD_COST, MAX_GOLD_COST);

    this.imageEventText.setDialogOption(OPTIONS[0]);

    if (goldCost > 0) {
      this.imageEventText.setDialogOption(OPTIONS[1] + goldCost + OPTIONS[3] + OPTIONS[4]);
    } else {
      this.imageEventText.setDialogOption(OPTIONS[2] + MIN_GOLD_COST + OPTIONS[3], true);
    }

    this.state = SimpletonEventHelper.EventState.WAITING;
    CardCrawlGame.sound.play("SCIFI_MUSIC_1");
  }

  @Override
  protected void buttonEffect(int buttonPressed) {
    switch (state) {
      case WAITING:
        switch (buttonPressed) {

          case 0:
            CardCrawlGame.sound.play("TRACTOR_BEAM_1");
            SimpletonEventHelper.receiveRelic(RELIC_REWARD_1);
            this.imageEventText.loadImage(TheSimpletonMod.getResourcePath(IMG_PATH_2));
            this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
            break;

          case 1:
            CardCrawlGame.sound.play("COW_MOO_1");
            SimpletonEventHelper.gainCard(CARD_REWARD_1);
            this.imageEventText.loadImage(TheSimpletonMod.getResourcePath(IMG_PATH_3));
            this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
            break;

          default:
            break;
        }
        this.imageEventText.clearAllDialogs();
        this.imageEventText.setDialogOption(OPTIONS[5]);
        this.state = SimpletonEventHelper.EventState.LEAVING;
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        break;
      case LEAVING:
        this.imageEventText.clearAllDialogs();
        this.imageEventText.setDialogOption(OPTIONS[5]);
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
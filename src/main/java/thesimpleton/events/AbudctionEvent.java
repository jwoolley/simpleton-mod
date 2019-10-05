package thesimpleton.events;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thesimpleton.TheSimpletonMod;

public class AbudctionEvent extends CustomSimpletonEvent
{
  public static final String ID = TheSimpletonMod.makeID("AbudctionEvent");

  private static final String IMG_PATH = SimpletonEventHelper.getUiPath("abduction1");
  private static final EventStrings eventStrings;
  private static final String NAME;
  private static final String[] DESCRIPTIONS;
  private static final String[] OPTIONS;

//  private final AbstractRelic DEFAULT_REWARD_RELIC = new BottledTornado();
//  private final AbstractRelic SECOND_REWARD_RELIC = new BottledLightning();
//  private final AbstractRelic FALLBACK_REWARD_RELIC = new Lantern();
//  private static final AbstractCard CURSE_GNATS = new Gnats();
//  private static final int CURSE_CHANCE_PERCENTAGE = 33;
//
//  private final AbstractRelic relicReward;
//  private final AbstractPotion potionReward;
//  private final AbstractCard curseCard;
//  private final boolean curseIsInjury;

  private SimpletonEventHelper.EventState state;

  public AbudctionEvent() {
    super(NAME, DESCRIPTIONS[0],  TheSimpletonMod.getResourcePath(IMG_PATH));

    final AbstractPlayer player = AbstractDungeon.player;

//    this.imageEventText.setDialogOption(OPTIONS[0] + potionReward.name + OPTIONS[2]);
//    this.imageEventText.setDialogOption(OPTIONS[1] + relicReward.name + OPTIONS[3] + CURSE_CHANCE_PERCENTAGE
//            + OPTIONS[4] + curseCard.name + OPTIONS[2],
//        curseCard, relicReward);

    this.state = SimpletonEventHelper.EventState.WAITING;
    CardCrawlGame.sound.play("MAGIC_CHIMES_1");
  }

  @Override
  protected void buttonEffect(int buttonPressed) {
    switch (state) {
      case WAITING:
        switch (buttonPressed) {

          case 0:
            CardCrawlGame.sound.play("POTION_3");
//            AbstractDungeon.player.obtainPotion(this.potionReward);
            break;

          case 1:
//            final boolean receiveCurse = AbstractDungeon.miscRng.randomBoolean(CURSE_CHANCE_PERCENTAGE / 100.0F);
//            if (receiveCurse) {
//              TheSimpletonMod.logger.debug("TheSimpletonMod::FirefliesEvent receiving curse");
////              if (curseIsInjury) {
////                CardCrawlGame.sound.play("VO_GREMLINDOPEY_2C");
////              }
////              SimpletonEventHelper.gainCard(curseCard);
//            }
//
//            SimpletonEventHelper.receiveRelic(relicReward);
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
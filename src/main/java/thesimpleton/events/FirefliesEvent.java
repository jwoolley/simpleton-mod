package thesimpleton.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BottledLightning;
import com.megacrit.cardcrawl.relics.BottledTornado;
import com.megacrit.cardcrawl.relics.Lantern;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.curse.Gnats;

public class FirefliesEvent extends CustomSimpletonEvent
{
  public static final String ID = TheSimpletonMod.makeID("FirefliesEvent");

  private static final String IMG_PATH = SimpletonEventHelper.getUiPath("fireflies1");
  private static final EventStrings eventStrings;
  private static final String NAME;
  private static final String[] DESCRIPTIONS;
  private static final String[] OPTIONS;

  private final AbstractRelic DEFAULT_REWARD_RELIC = new BottledTornado();
  private final AbstractRelic SECOND_REWARD_RELIC = new BottledLightning();
  private final AbstractRelic FALLBACK_REWARD_RELIC = new Lantern();
  private static final AbstractCard CURSE_GNATS = new Gnats();

  private final AbstractRelic relicReward;
  private final AbstractPotion potionReward;
  private final AbstractCard curseCard;
  private final boolean curseIsInjury;

  private SimpletonEventHelper.EventState state;

  public FirefliesEvent() {
    super(NAME, DESCRIPTIONS[0],  TheSimpletonMod.getResourcePath(IMG_PATH));

    final AbstractPlayer player = AbstractDungeon.player;

    potionReward = PotionHelper.getRandomPotion();

    if (AbstractDungeon.miscRng.randomBoolean(0.5F) && player.masterDeck.getPowers().size() > 0 && !player.hasRelic(DEFAULT_REWARD_RELIC.relicId)) {
      relicReward = DEFAULT_REWARD_RELIC;
    } else if (AbstractDungeon.miscRng.randomBoolean(0.5F) && player.masterDeck.getSkills().size() > 0 && !player.hasRelic(SECOND_REWARD_RELIC.relicId)) {
      relicReward = SECOND_REWARD_RELIC;
    } else if (!player.hasRelic(FALLBACK_REWARD_RELIC.relicId)) {
      relicReward = FALLBACK_REWARD_RELIC;
    } else {
      relicReward = AbstractDungeon.returnRandomScreenlessRelic(AbstractRelic.RelicTier.COMMON);
    }

    curseCard = CURSE_GNATS;
    curseIsInjury = false;

    this.imageEventText.setDialogOption(OPTIONS[0] + potionReward.name + OPTIONS[2]);
    this.imageEventText.setDialogOption(OPTIONS[1] + relicReward.name + OPTIONS[3] + curseCard.name + OPTIONS[2],
        curseCard, relicReward);

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
            AbstractDungeon.player.obtainPotion(this.potionReward);
            break;

          case 1:
            final boolean receiveCurse = AbstractDungeon.miscRng.randomBoolean();
            if (receiveCurse) {
              TheSimpletonMod.logger.debug("TheSimpletonMod::FirefliesEvent receiving curse");
              if (curseIsInjury) {
                CardCrawlGame.sound.play("VO_GREMLINDOPEY_2C");
              }
              SimpletonEventHelper.gainCard(curseCard);
            }

            SimpletonEventHelper.receiveRelic(relicReward);
            break;

          default:
            break;
        }
        this.imageEventText.clearAllDialogs();
        this.imageEventText.setDialogOption(OPTIONS[4]);
        this.state = SimpletonEventHelper.EventState.LEAVING;
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        break;
      case LEAVING:
        this.imageEventText.clearAllDialogs();
        this.imageEventText.setDialogOption(OPTIONS[4]);
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
package thesimpleton.events;

import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.*;
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

  private final AbstractRelic TORNADO_RELIC_REWARD = new BottledTornado();
  private final AbstractRelic LIGHTNING_RELIC_REWARD = new BottledLightning();
  private final AbstractRelic LANTERN_RELIC_REWARD = new Lantern();
  private final AbstractRelic INSECT_RELIC_REWARD = new PreservedInsect();
  private static final AbstractCard CURSE_GNATS = new Gnats();
  private static final int CURSE_CHANCE_PERCENTAGE = 50;

  private static final int BASE_GOLD_REWARD = 25;

  private final AbstractRelic relicReward;
  private final AbstractPotion potionReward;
  private final int goldReward;
  private final AbstractCard curseCard;

  private SimpletonEventHelper.EventState state;

  public FirefliesEvent() {
    super(NAME, DESCRIPTIONS[0],  TheSimpletonMod.getImageResourcePath(IMG_PATH));

    final AbstractPlayer player = AbstractDungeon.player;

    potionReward = PotionHelper.getRandomPotion();
    curseCard = CURSE_GNATS;

    boolean canPlayerGainTornado = player.masterDeck.getPowers().size() > 0 && !player.hasRelic(TORNADO_RELIC_REWARD.relicId);
    boolean canPlayerGainLightning =  player.masterDeck.getSkills().size() > 0 && !player.hasRelic(LIGHTNING_RELIC_REWARD.relicId);
    boolean canPlayerGainLantern = !player.hasRelic(LANTERN_RELIC_REWARD.relicId);
    boolean canPlayerGainInsect =  !player.hasRelic(INSECT_RELIC_REWARD.relicId);
    boolean canPlayerGainBottleRelic = canPlayerGainTornado || canPlayerGainLightning;
    boolean canPlayerGainOtherRelic = canPlayerGainLantern || canPlayerGainInsect;

    AbstractRelic bottleRelicReward = ((canPlayerGainTornado && (AbstractDungeon.miscRng.randomBoolean(0.5F) || !canPlayerGainLightning))
            ? TORNADO_RELIC_REWARD
            : (canPlayerGainLightning ? LIGHTNING_RELIC_REWARD :  AbstractDungeon.returnRandomScreenlessRelic(AbstractRelic.RelicTier.UNCOMMON)));

    AbstractRelic otherRelicReward = ((canPlayerGainLantern && (AbstractDungeon.miscRng.randomBoolean(0.5F) || !canPlayerGainInsect))
            ? LANTERN_RELIC_REWARD
            : (canPlayerGainInsect ? INSECT_RELIC_REWARD : AbstractDungeon.returnRandomScreenlessRelic(AbstractRelic.RelicTier.COMMON)));

    int tempGoldReward = 0;
    if (canPlayerGainBottleRelic && (!canPlayerGainOtherRelic || AbstractDungeon.miscRng.randomBoolean(0.5F))) {
      relicReward = bottleRelicReward;
    } else {
      relicReward = otherRelicReward;
    }
    if (relicReward.tier == AbstractRelic.RelicTier.UNCOMMON || relicReward.tier == AbstractRelic.RelicTier.RARE) {
      tempGoldReward = BASE_GOLD_REWARD * 3;
    } else if (relicReward.tier == AbstractRelic.RelicTier.COMMON) {
      tempGoldReward = BASE_GOLD_REWARD * 6;
    } else {
      tempGoldReward = BASE_GOLD_REWARD * 10;
    }

    goldReward = tempGoldReward;

    this.imageEventText.setDialogOption(OPTIONS[0] + potionReward.name + OPTIONS[2]);

    this.imageEventText.setDialogOption(OPTIONS[1] + relicReward.name + OPTIONS[6] + goldReward + OPTIONS[7] + CURSE_CHANCE_PERCENTAGE
                    + OPTIONS[4] + curseCard.name + OPTIONS[2],
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
            final boolean receiveCurse = AbstractDungeon.miscRng.randomBoolean(CURSE_CHANCE_PERCENTAGE / 100.0F);
            if (receiveCurse) {
              CardCrawlGame.sound.playA("ATTACK_BEE_BUZZ_1", 1.5F);
              TheSimpletonMod.traceLogger.trace("TheSimpletonMod::FirefliesEvent receiving curse");
              SimpletonEventHelper.gainCard(curseCard);
            } else {
              CardCrawlGame.sound.play("GOLD_GAIN");
            }
            SimpletonEventHelper.receiveRelic(relicReward);
            AbstractDungeon.player.gainGold(this.goldReward);
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
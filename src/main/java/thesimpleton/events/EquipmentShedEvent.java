package thesimpleton.events;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.ScrapeEffect;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.cards.GainCardAction;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.cards.attack.ReapAndSow;
import thesimpleton.cards.attack.ReapAndSquash;
import thesimpleton.cards.attack.ReekAndSow;
import thesimpleton.cards.curse.Nettles;
import thesimpleton.cards.curse.Spoilage;
import thesimpleton.cards.power.crop.Strawberries;
import thesimpleton.cards.skill.Surplus;
import thesimpleton.crops.Crop;
import thesimpleton.events.SimpletonEventHelper.EventState;
import thesimpleton.seasons.SeasonInfo;
import thesimpleton.ui.SettingsHelper;

public class EquipmentShedEvent extends AbstractImageEvent
{
  public static final String ID = TheSimpletonMod.makeID("EquipmentShedEvent");

  private static final String IMG_PATH = SimpletonEventHelper.getUiPath("equipmentshed1");
  private static final EventStrings eventStrings;
  private static final String NAME;
  private static final String[] DESCRIPTIONS;
  private static final String[] OPTIONS;

  private static final AbstractCard CURSE_CARD = new Spoilage();
  private final AbstractCard POTATO_REPLACEMENT_CARD = new Surplus();
  private final AbstractCard SQUASH_REPLACEMENT_CARD = new ReapAndSquash();
  private final AbstractCard ONION_REPLACEMENT_CARD = new ReekAndSow();
  private final AbstractCard CARD_TO_REPLACE = new ReapAndSow();

  private static final float HP_DAMAGE_PERCENT = 0.1F;
  private static final float HP_DAMAGE_PERCENT_A15 = 0.2F;
  private static final int MAX_HP_INCREASE_AMOUNT = 5;

  private final AbstractCard replacementCard;
  private final AbstractCard cardToReplace;

  private final boolean playerHasReapAndSow;
  private final int damage;
  private EventState state;

  public EquipmentShedEvent() {
    super(NAME, DESCRIPTIONS[0],  TheSimpletonMod.getResourcePath(IMG_PATH));

    final SeasonInfo seasonInfo = TheSimpletonMod.getSeasonInfo();

    replacementCard =  (!TheSimpletonMod.isPlayingAsSimpleton() ? SimpletonEventHelper.getRandomUncommonCardFromPool()
        : (seasonInfo.isInSeason(Crop.SQUASH) ? SQUASH_REPLACEMENT_CARD
        : (seasonInfo.isInSeason(Crop.ONIONS) ? ONION_REPLACEMENT_CARD
        : POTATO_REPLACEMENT_CARD)));

    playerHasReapAndSow = SimpletonUtil.playerHasCard(CARD_TO_REPLACE);
    cardToReplace =  playerHasReapAndSow ? SimpletonUtil.getCardFromPlayerMasterDeck(CARD_TO_REPLACE.cardID) : null;

    this.damage = MathUtils.round(AbstractDungeon.player.maxHealth
        * (AbstractDungeon.ascensionLevel >= 15 ? HP_DAMAGE_PERCENT_A15 : HP_DAMAGE_PERCENT));

    this.imageEventText.setDialogOption(
        OPTIONS[2] + MAX_HP_INCREASE_AMOUNT + OPTIONS[6] + CURSE_CARD.name + OPTIONS[7],
        CURSE_CARD);

    this.imageEventText.setDialogOption(
        (playerHasReapAndSow ? (OPTIONS[0] + cardToReplace.name + OPTIONS[3]) : OPTIONS[1])
        +  replacementCard.name + OPTIONS[4] + this.damage + OPTIONS[5],
        replacementCard);

    this.imageEventText.setDialogOption(OPTIONS[8]);

    this.state = EventState.WAITING;
    CardCrawlGame.sound.play("EVENT_NLOTH");
  }

  @Override
  protected void buttonEffect(int buttonPressed) {
    switch (state) {
      case WAITING:
        switch (buttonPressed) {
          case 0:
            AbstractDungeon.player.increaseMaxHp(MAX_HP_INCREASE_AMOUNT, true);
            SimpletonEventHelper.gainCard(CURSE_CARD);
            break;

          case 1:
            AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, this.damage));
            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
            AbstractDungeon.actionManager.addToBottom(new VFXAction(
                new ScrapeEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.1F));
            CardCrawlGame.sound.play("ATTACK_IRON_3");


            if (playerHasReapAndSow) {
              SimpletonEventHelper.loseCard(cardToReplace, Settings.WIDTH * SettingsHelper.getScaleX(),
                  Settings.HEIGHT * SettingsHelper.getScaleY());

              AbstractDungeon.actionManager.addToBottom(new WaitAction(1.5F));

              SimpletonEventHelper.gainCard(replacementCard,
                  (Settings.WIDTH - 2 * AbstractCard.IMG_WIDTH * 1.1F) / 2.0F * SettingsHelper.getScaleX(),
                  Settings.HEIGHT / 2.0F * SettingsHelper.getScaleY());
            } else {
              SimpletonEventHelper.gainCard(replacementCard);
            }

            break;

          case 2:
            leaveEvent();
            break;

          default:
            break;
        }
        this.imageEventText.clearAllDialogs();
        this.imageEventText.setDialogOption(OPTIONS[8]);
        this.state = EventState.LEAVING;
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        break;
      case LEAVING:
        leaveEvent();
        break;
    }
  }

  private void leaveEvent() {
    this.imageEventText.clearAllDialogs();
    this.imageEventText.setDialogOption(OPTIONS[8]);
    openMap();
  }

  static {
    eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    NAME = eventStrings.NAME;
    DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    OPTIONS = eventStrings.OPTIONS;
  }
}

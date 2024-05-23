package thesimpleton.events;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.ScrapeEffect;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.cards.attack.ReapAndSow;
import thesimpleton.cards.attack.ReapAndSquash;
import thesimpleton.cards.attack.ReekAndSow;
import thesimpleton.cards.curse.Spoilage;
import thesimpleton.cards.skill.Surplus;
import thesimpleton.crops.Crop;
import thesimpleton.events.SimpletonEventHelper.EventState;
import thesimpleton.seasons.SeasonInfo;
import thesimpleton.ui.SettingsHelper;

public class EquipmentShedEvent extends CustomSimpletonEvent
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

  private static final float HP_DAMAGE_PERCENT = 0.12F;
  private static final float HP_DAMAGE_PERCENT_A15 = 0.2F;
  private static final float MAX_HP_INCREASE_PERCENTAGE = 0.13F;

  private final AbstractCard replacementCard;
  private final AbstractCard cardToReplace;

  private final int maxHpIncrease;
  private final int damage;
  private EventState state;

  private final int BASE_CURSE_CHANCE_PERCENTAGE = 50;

  public EquipmentShedEvent() {
    super(NAME, DESCRIPTIONS[0],  TheSimpletonMod.getImageResourcePath(IMG_PATH));

    final SeasonInfo seasonInfo = TheSimpletonMod.getSeasonInfo();

    replacementCard =  (!TheSimpletonMod.isPlayingAsSimpleton() ? SimpletonEventHelper.getRandomRareCardFromPool()
        : (seasonInfo.isInSeason(Crop.SQUASH) ? SQUASH_REPLACEMENT_CARD
        : (seasonInfo.isInSeason(Crop.ONIONS) ? ONION_REPLACEMENT_CARD
        : POTATO_REPLACEMENT_CARD)));

    final boolean playerHasReapAndSow = SimpletonUtil.playerHasCard(CARD_TO_REPLACE);
    cardToReplace =  playerHasReapAndSow ? SimpletonUtil.getCardFromPlayerMasterDeck(CARD_TO_REPLACE.cardID)
        : SimpletonEventHelper.getRandomCardFromDeck(
            c -> c.rarity != AbstractCard.CardRarity.RARE && c.type != AbstractCard.CardType.CURSE);

    this.damage = MathUtils.round(AbstractDungeon.player.maxHealth
        * (AbstractDungeon.ascensionLevel >= 15 ? HP_DAMAGE_PERCENT_A15 : HP_DAMAGE_PERCENT));


    this.maxHpIncrease = (int)(MAX_HP_INCREASE_PERCENTAGE * AbstractDungeon.player.maxHealth);

    this.imageEventText.setDialogOption(
        getMaxHpOptionText(OPTIONS[2], maxHpIncrease, BASE_CURSE_CHANCE_PERCENTAGE), CURSE_CARD);

    this.imageEventText.setDialogOption(
        (cardToReplace != null ? (OPTIONS[0] + cardToReplace.name + OPTIONS[5]) : OPTIONS[1])
            +  replacementCard.name + OPTIONS[6] + this.damage + OPTIONS[7],
        replacementCard);

    this.imageEventText.setDialogOption(OPTIONS[12]);

    this.state = EventState.WAITING;
    CardCrawlGame.sound.play("EVENT_NLOTH");
  }

  private String getMaxHpOptionText(String optionPrefixText, int maxHpAmount, int curseChancePercentage) {
    return optionPrefixText + OPTIONS[4] + maxHpAmount + OPTIONS[8]
        + (curseChancePercentage < 100 ? OPTIONS[9] + curseChancePercentage + OPTIONS[10] : "")
        +  (OPTIONS[11] + CURSE_CARD.name + OPTIONS[8]);
  }

  @Override
  protected void buttonEffect(int buttonPressed) {
    switch (state) {
      case WAITING:
        switch (buttonPressed) {
          case 0:
            AbstractDungeon.player.increaseMaxHp(maxHpIncrease, true);

            boolean receiveCurse = AbstractDungeon.miscRng.randomBoolean(BASE_CURSE_CHANCE_PERCENTAGE / 100.0F);

            if (receiveCurse) {
              CardCrawlGame.sound.play("CRUNCH_NEGATIVE_1");
              SimpletonEventHelper.gainCard(CURSE_CARD);
            }
            break;

          case 1:
            AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, this.damage));
            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
            AbstractDungeon.actionManager.addToBottom(new VFXAction(
                new ScrapeEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.1F));
            CardCrawlGame.sound.play("ATTACK_IRON_3");

            if (cardToReplace != null) {
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
          default:
            leaveEvent();
            break;
        }

        this.imageEventText.clearAllDialogs();
        this.imageEventText.setDialogOption(OPTIONS[12]);
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

package thesimpleton.cards.interfaces;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.core.Settings;
import thesimpleton.cards.SimpletonCardHelper;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.ui.SettingsHelper;

abstract public class AbstractDynamicTextCard extends CustomCard {
  public AbstractDynamicTextCard(String id, String name, String imgUrl, int cost,
                                 String rawDescription, CardType type, CardColor color,
                                 CardRarity rarity, CardTarget target) {
    super(id, name, imgUrl, cost, rawDescription, type, color, rarity, target);
  }


  @Override
  public void hover() {

    if (SimpletonCardHelper.isCardInHand(this)) {
      updateDescription(SimpletonUtil.isPlayerInCombat());
    }
    super.hover();
  }

  @Override
  public void unhover() {
    super.unhover();
    super.untip();
    updateDescription(false);
  }

  abstract protected void updateDescription(boolean extendedDescription);
}
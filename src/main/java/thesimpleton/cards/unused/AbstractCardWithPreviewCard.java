package thesimpleton.cards.unused;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.utilities.SimpletonModSettings;

abstract public class AbstractCardWithPreviewCard extends CustomCard {
  private MultiLock renderPreviewCard;
  public AbstractCardWithPreviewCard(String id, String name, String img, int cost, String rawDescription, CardType type,
                                     CardColor color, CardRarity rarity, CardTarget target) {
    super(id, name, img, cost, rawDescription, type, color, rarity, target);
    renderPreviewCard = MultiLock.ZERO;
  }

  abstract public AbstractCard getPreviewCard();

  public void renderCardTip(SpriteBatch sb) {
    super.renderCardTip(sb);

  // TODO: reposition if card is offscreen (most likely off right side?)
    if (shouldRenderPreviewCard()) {
    final AbstractCard _previewCard = getPreviewCard();
    if (_previewCard != null) {
      _previewCard.current_x = _previewCard.hb.x = getPreviewXOffset();
      _previewCard.current_y = _previewCard.hb.y = getPreviewYOffset();
      _previewCard.render(sb);
    }
  }
}

  private float getPreviewXOffset() {
    final float cardScale = this.drawScale * Settings.scale;
    final boolean isCardNearRightEdge = this.current_x > Settings.WIDTH * 0.725F;
    final boolean isCardNearLeftEdge = this.current_x < (1 - 0.725F);
    final boolean isInCombat = SimpletonUtil.isPlayerInCombat();
    if (isCardNearRightEdge && !isInCombat || isCardNearLeftEdge && isInCombat) {
      return (this.hb.x + this.hb.width + this.hb.width * .44f) * cardScale;
    } else {
      return (this.hb.x - this.hb.width * .44f) * cardScale;
    }
  }


  private float getPreviewYOffset() {
    final float cardScale = this.drawScale * Settings.scale;
    return (this.hb.y + this.hb.height * 0.64f) * cardScale;
  }

  @Override
  public void hover() {
    super.hover();
    if (renderPreviewCard != MultiLock.TWO) {
      renderPreviewCard = MultiLock.values()[renderPreviewCard.ordinal() + 1];
    }
  }

  @Override
  public void unhover() {
    if (renderPreviewCard != MultiLock.ZERO) {
      renderPreviewCard = MultiLock.values()[renderPreviewCard.ordinal() - 1];
    }
    super.unhover();
  }

  @Override
  public void updateHoverLogic() {
    super.updateHoverLogic();

    if (Settings.hideCards) {
      renderPreviewCard = MultiLock.ZERO;
    }
  }

  enum MultiLock {
    ZERO, ONE, TWO;
  }

  private boolean shouldRenderPreviewCard() {
    return !(SimpletonUtil.isCardInHand(this) && SimpletonModSettings.HIDE_PREVIEW_CARDS_IN_COMBAT)
        && this.renderPreviewCard == MultiLock.TWO
        && (AbstractDungeon.player == null
        || (this.isHoveredInHand(Settings.scale) && !AbstractDungeon.player.isDraggingCard));
  }

  @Override
  public void untip() {
    super.untip();
    renderPreviewCard = MultiLock.ZERO;
  }
}
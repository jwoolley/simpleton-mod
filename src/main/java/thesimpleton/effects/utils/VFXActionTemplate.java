package thesimpleton.effects.utils;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

abstract public class VfxTemplate {
  public abstract VFXAction getEffect(float x, float y);
}

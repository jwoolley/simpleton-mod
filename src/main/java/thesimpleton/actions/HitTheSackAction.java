package thesimpleton.actions;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thesimpleton.TheSimpletonMod;
import thesimpleton.orbs.PotatoCropOrb;

public class HitTheSackAction  extends AbstractGameAction
{
  private static final int SPUDS_PER_TICK = 1;
  private DamageInfo info;
  private boolean freeToPlayOnce;
  private boolean upgraded ;
  private int energyOnUse;
  private AbstractPlayer p;
  private AbstractMonster m;

  private final int potatoesPerTick;
  private int numPotatoReps;
  private int numDamageReps;

  private boolean isFirstRep;


  public HitTheSackAction(AbstractPlayer p, AbstractMonster m, DamageInfo info, int potatoesPerTick, boolean upgraded, boolean freeToPlayOnce, int energyOnUse)
  {
    this(p, m, info, potatoesPerTick, upgraded, freeToPlayOnce, energyOnUse, true, -1, -1);
  }

  public HitTheSackAction(AbstractPlayer p, AbstractMonster m, DamageInfo info, int potatoesPerTick,
                          boolean upgraded, boolean freeToPlayOnce, int energyOnUse, boolean isFirstRep, int numDamageReps, int numPotatoReps)
  {
    this.p = p;
    this.m = m;
    this.info = info;
    this.amount = potatoesPerTick;
    this.potatoesPerTick = potatoesPerTick;
    this.freeToPlayOnce = freeToPlayOnce;
    this.upgraded = upgraded;
    this.duration = Settings.ACTION_DUR_XFAST;
    this.actionType = ActionType.DAMAGE;
    this.energyOnUse = energyOnUse;

    this.isFirstRep = isFirstRep;
    this.numDamageReps = numDamageReps;
    this.numPotatoReps = numPotatoReps;

    TheSimpletonMod.logger.debug("HitTheSackAction: energyOnUse: " + energyOnUse + "; upgraded: " + upgraded);
  }

  @Override
  public void update() {
    if (isFirstRep) {
      TheSimpletonMod.logger.debug("HitTheSackAction:: update firstRep numDamageReps");

      this.numDamageReps = EnergyPanel.totalCount;

      TheSimpletonMod.logger.debug("HitTheSackAction:: update firstRep numDamageReps (1): " + this.numDamageReps);


      if (this.energyOnUse != -1) {
        this.numDamageReps = this.energyOnUse;
        TheSimpletonMod.logger.debug("HitTheSackAction:: update firstRep numDamageReps (2): " + this.numDamageReps);
      }
      if (this.upgraded) {
        this.numDamageReps++;
        TheSimpletonMod.logger.debug("HitTheSackAction:: update firstRep numDamageReps (3): " + this.numDamageReps);
      }
      if (this.p.hasRelic("Chemical X")) {
        this.numDamageReps += 2;
        this.p.getRelic("Chemical X").flash();

      }

      TheSimpletonMod.logger.debug("HitTheSackAction:: update firstRep numDamageReps (4): " + this.numDamageReps);

      this.numPotatoReps = numDamageReps;
    }

    if (this.numDamageReps <= 0 && this.numPotatoReps <= 0) {
        this.isDone = true;
        return;
    } else if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
//      AbstractDungeon.actionManager.clearPostCombatActions();
      this.isDone = true;
      return;
    }

    this.duration -= Gdx.graphics.getDeltaTime();

    if (this.duration < 0.0F)
    {
      if (this.numDamageReps > 0)  {
        if (!m.isDead && !m.isDying) {
          AbstractDungeon.actionManager.addToBottom(new DamageAction(this.m, this.info, AttackEffect.NONE, true));
          AbstractDungeon.actionManager.addToBottom(new SFXAction("BLUNT_FAST", 0.1f));
        }

        this.numDamageReps--;

        if (!this.freeToPlayOnce) {
          this.p.energy.use(EnergyPanel.totalCount);
        }
      } else if (numPotatoReps > 0) {
        AbstractDungeon.actionManager.addToBottom(new CropSpawnAction(new PotatoCropOrb(SPUDS_PER_TICK),true));
        numPotatoReps--;
      }
      AbstractDungeon.actionManager.addToBottom(
          new HitTheSackAction(this.p, this.m, this.info, this.potatoesPerTick,
              this.upgraded, this.freeToPlayOnce, this.energyOnUse, false,
              numDamageReps, numPotatoReps));

      this.isDone = true;
    }
  }
}
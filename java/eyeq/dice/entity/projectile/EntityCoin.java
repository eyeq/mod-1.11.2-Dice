package eyeq.dice.entity.projectile;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import eyeq.dice.Dice;

public class EntityCoin extends EntityThrowable {
    public EntityCoin(World world) {
        super(world);
    }

    public EntityCoin(World world, EntityLivingBase thrower) {
        super(world, thrower);
    }

    public EntityCoin(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if(result.entityHit != null) {
            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.5F);
        }
        if(this.world.isRemote) {
            return;
        }

        BlockPos pos = this.getPosition();
        EntityItem entityItem = new EntityItem(this.world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Dice.coin));
        this.world.spawnEntity(entityItem);
        if(this.getThrower() instanceof EntityPlayer) {
            ITextComponent text = new TextComponentTranslation(rand.nextBoolean() ? Dice.I18n_HEADS : Dice.I18n_TAILS);
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(text);
        }
        this.world.setEntityState(this, (byte) 3);
        this.setDead();
    }
}

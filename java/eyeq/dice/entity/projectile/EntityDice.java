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
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import eyeq.dice.Dice;

public class EntityDice extends EntityThrowable {
    public EntityDice(World world) {
        super(world);
    }

    public EntityDice(World world, EntityLivingBase thrower) {
        super(world, thrower);
    }

    public EntityDice(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    private int getDice() {
        return rand.nextInt(6) + 1;
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if(result.entityHit != null) {
            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 1.0F);
        }
        if(this.world.isRemote) {
            return;
        }

        BlockPos pos = this.getPosition();
        EntityItem entityItem = new EntityItem(this.world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Dice.dice));
        this.world.spawnEntity(entityItem);
        if(this.getThrower() instanceof EntityPlayer) {
            ITextComponent text = new TextComponentString(Integer.toString(getDice()));
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(text);
        }
        this.world.setEntityState(this, (byte) 3);
        this.setDead();
    }
}

package eyeq.dice.item;

import eyeq.dice.Dice;
import eyeq.dice.entity.projectile.EntityCoin;
import eyeq.util.item.ItemThrow;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemCoin extends ItemThrow {
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if(!world.isRemote) {
            ITextComponent text = new TextComponentTranslation(Dice.I18n_COIN);
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(text);
        }
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public SoundEvent getSoundEvent() {
        return Dice.entityCoinThrow;
    }

    @Override
    public EntityThrowable createEntityThrowable(World world, EntityPlayer player) {
        return new EntityCoin(world, player);
    }
}

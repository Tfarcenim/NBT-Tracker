package com.tfar.nbttracker;

import io.netty.buffer.Unpooled;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.List;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(NBTTracker.MODID)
@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class NBTTracker {
  public static final String MODID = "nbttracker";

  static ItemStack hoveredStack = ItemStack.EMPTY;
  static int cachedSize = 0;
  // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
  // Event bus for receiving Registry Events)
  @SubscribeEvent
  public static void tooltip(final ItemTooltipEvent t) {
    CompoundNBT nbt = t.getItemStack().getTag();
    if (nbt == null || !t.getFlags().isAdvanced()) return;
    List<ITextComponent> tooltips = t.getToolTip();
    ItemStack eventStack = t.getItemStack();
    if (!hoveredStack.equals(eventStack)){
      cachedSize = getNbtSize(t.getItemStack());
    }
    hoveredStack = t.getItemStack();
    tooltips.add(new StringTextComponent("NBT Size: ").appendSibling(new StringTextComponent("" + cachedSize).applyTextStyle(cachedSize > 2000000 ? TextFormatting.DARK_RED : TextFormatting.GREEN).appendSibling(new StringTextComponent(" bytes"))));
  }


  public static int getNbtSize(ItemStack stack) {
    return getNbtSize(stack.getTag());
  }

  public static int getNbtSize(@Nullable CompoundNBT nbt) {
    PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
    buffer.writeCompoundTag(nbt);
    return buffer.writerIndex();
  }
}

package com.evandev.redomesticate.fabric.mixin;

//? if fabric {
import com.evandev.redomesticate.event.EventProxy;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {

    @Shadow
    @Final
    private DataSlot cost;

    //? if >=26.1 {
    /*public AnvilMenuMixin(@Nullable MenuType<?> type, int containerId, Inventory playerInventory, ContainerLevelAccess access, ItemCombinerMenuSlotDefinition slots) {
        super(type, containerId, playerInventory, access, slots);
    }
    *///?} else {
    public AnvilMenuMixin(@Nullable MenuType<?> type, int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(type, containerId, playerInventory, access);
    }
    //?}

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    private void redomesticate$customAnvilResult(CallbackInfo ci) {
        ItemStack left = this.inputSlots.getItem(0);
        ItemStack right = this.inputSlots.getItem(1);

        int[] outCost = new int[1];
        ItemStack result = EventProxy.onUpdateAnvil(left, right, outCost);

        if (!result.isEmpty()) {
            this.resultSlots.setItem(0, result);
            this.cost.set(outCost[0]);
            this.broadcastChanges();
            ci.cancel();
        }
    }
}
//?}

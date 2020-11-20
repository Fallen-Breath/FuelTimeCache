package me.fallenbreath.fueltimecache.mixins;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin
{
	private static Map<Item, Integer> BURN_TIMES = null;

	@Inject(method = "createFuelTimeMap", at = @At("HEAD"), cancellable = true)
	private static void stopDumb(CallbackInfoReturnable<Map<Item, Integer>> cir)
	{
		if (BURN_TIMES != null)
		{
			cir.setReturnValue(BURN_TIMES);
			cir.cancel();
		}
	}

	@Inject(method = "createFuelTimeMap", at = @At("TAIL"))
	private static void cacheResult(CallbackInfoReturnable<Map<Item, Integer>> cir)
	{
		if (BURN_TIMES == null)
		{
			BURN_TIMES = cir.getReturnValue();
		}
	}
}

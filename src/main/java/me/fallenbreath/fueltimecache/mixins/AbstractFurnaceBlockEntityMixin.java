package me.fallenbreath.fueltimecache.mixins;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

/**
 * Every time when a furnace want to know the burn time of an item or to check if an item is burnable, it will call
 * the method {@link AbstractFurnaceBlockEntity#createFuelTimeMap}. It might cause quite a lot of lag when there are
 * lots of hoppers trying to insert a whatever item into the fuel slot of furnaces.
 * Notice that the return Map of createFuelTimeMap is always the same, so caching the result permanently will boost
 * the performance quite a lot.
 * It might mess things up if other mods dynamically add some extra fuel information, but that's another thing
 * @author Fallen_Breath
 */
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

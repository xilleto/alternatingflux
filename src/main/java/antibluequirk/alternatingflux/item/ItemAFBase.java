package antibluequirk.alternatingflux.item;

import net.minecraft.item.Item;
import antibluequirk.alternatingflux.AlternatingFlux;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.items.ItemIEBase;

public class ItemAFBase extends ItemIEBase {
	public ItemAFBase(String name, int stackSize, String... subNames) {
		super(name, stackSize, subNames);
		this.fixupItem();
	}

	/*
	 * This function allows us to use IEBase classes, by fixing things up so they come from our mod.
	 * It should be called right after the super call in any constructor of a class that derives from ItemIEBase
	 * This is kind of hacky, but allows us to avoid copying a lot of code.
	 */
    private final void fixupItem() {
    	//First, get the item out of IE's registries.
    	Item rItem = IEContent.registeredIEItems.remove(IEContent.registeredIEItems.size() - 1);
    	if (rItem != this) throw new IllegalStateException("fixupItem was not called at the appropriate time");
    	
    	//Now, reconfigure the block to match our mod.
    	this.setTranslationKey(AlternatingFlux.MODID + "." + this.itemName);
    	this.setCreativeTab(AlternatingFlux.creativeTab);
    	
    	//And add it to our registries.
    	AlternatingFlux.items.add(this);
    }
}
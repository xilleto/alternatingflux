package antibluequirk.alternatingflux.block;

import antibluequirk.alternatingflux.AlternatingFlux;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.BlockIEBase;
import blusunrize.immersiveengineering.common.blocks.BlockIETileProvider;
import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.item.Item;

abstract class BlockAFTileProvider<E extends Enum<E> & BlockIEBase.IBlockEnum> extends BlockIETileProvider<E>
{
	public BlockAFTileProvider(String name, Material material, PropertyEnum<E> mainProperty, Class<? extends ItemBlockIEBase> itemBlock, Object... additionalProperties) {
		super(name, material, mainProperty, itemBlock, additionalProperties);
		this.unregisterFromIEContent(itemBlock);
	}

	@Override
	public String createRegistryName()
	{
		return AlternatingFlux.MODID + ":" + name;
	}
	
	/**
	 * This function allows us to use BlockIEBase class, by fixing things up so they come from our mod.
     * It should be called right after the super call in any constructor of a class that derives from BlockIEBase
     * This is kind of hacky, but allows us to avoid copying a lot of code.
	 */
    private final void unregisterFromIEContent(Class<? extends ItemBlockIEBase> itemBlock) {
    	Block rBlock = IEContent.registeredIEBlocks.remove(IEContent.registeredIEBlocks.size()-1);
    	if (rBlock != this) throw new IllegalStateException("fixupBlock was not called at the appropriate time, removed block did not match");
    	
    	Item rItem = IEContent.registeredIEItems.remove(IEContent.registeredIEItems.size()-1);
    	if (rItem.getClass() != itemBlock) throw new IllegalStateException("fixupBlock was not called at the appropriate time");
    	
    	//And add it to our registries.
    	AlternatingFlux.blocks.add(this);
    	try{
    		AlternatingFlux.items.add(itemBlock.getConstructor(Block.class).newInstance(this));
    	}catch(Exception e){e.printStackTrace();}
    }
}

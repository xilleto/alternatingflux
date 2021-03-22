package antibluequirk.alternatingflux.block;

import com.google.common.collect.ImmutableSet;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityTransformerHV;

public class TileEntityTransformerAF extends TileEntityTransformerHV
{
    {
        acceptableLowerWires = ImmutableSet.of(WireType.HV_CATEGORY);
    }


	//public WireType getSecondCable() { return super.getLimiter(1); }
	
	@Override
	protected boolean canTakeLV()
	{
		return false;
	}
	@Override
	protected boolean canTakeMV()
	{
		return false;
	}
	@Override
	protected boolean canTakeHV()
	{
		return true;
	}
	
	@Override
	protected float getLowerOffset() {
		return super.getHigherOffset();
	}

	@Override
	protected float getHigherOffset() {
		return .75F;
	}

	@Override
    public String getHigherWiretype()
    {
        return "AF";
    }

}
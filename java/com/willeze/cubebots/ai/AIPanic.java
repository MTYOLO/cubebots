package com.willeze.cubebots.ai;

import net.minecraft.entity.ai.EntityAIPanic;

import com.willeze.cubebots.entity.CubeBot;

public class AIPanic extends EntityAIPanic {

	CubeBot b;

	public AIPanic(CubeBot par1EntityCreature, float par2) {
		super(par1EntityCreature, par2);
		b = par1EntityCreature;
	}

	@Override
	public boolean shouldExecute() {
		if (b.tamed && b.getLastAttacker() != null)
			return false;

		return super.shouldExecute() && b.canPanic();
	}

}

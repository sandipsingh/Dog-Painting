package com.app.lighter;

import java.util.Random;

import org.anddev.andengine.entity.particle.Particle;
import org.anddev.andengine.entity.particle.initializer.IParticleInitializer;

public class RandomColorInitializer implements IParticleInitializer
{
	private Random random;
	
	public RandomColorInitializer()
	{
		random = new Random();
	}
	
	
	public void onInitializeParticle(Particle particle)
	{
		particle.setColor(
			random.nextFloat(),
			random.nextFloat(),
			random.nextFloat()
		);
	}
}

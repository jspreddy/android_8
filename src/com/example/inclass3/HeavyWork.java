package com.example.inclass3;

import java.util.Random;

public class HeavyWork {
	static final int COUNT = 900000;
	static int i;
	static double getNumber(){
		double num = 0;
		Random rand = new Random();
		for(i=0;i<COUNT; i++){
			num = num + rand.nextDouble();
		}
		return num / ((double) COUNT);
	}
}
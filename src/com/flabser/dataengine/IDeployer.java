package com.flabser.dataengine;

public interface IDeployer {
	boolean isStructureActual();
	int deploy();
	int remove();
}

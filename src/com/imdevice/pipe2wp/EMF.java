package com.imdevice.pipe2wp;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class EMF {
	private static final EntityManagerFactory emfInstance =
	        Persistence.createEntityManagerFactory("transactions-optional");
	private EMF(){}
	public EntityManagerFactory get(){
		return emfInstance;
	}
}

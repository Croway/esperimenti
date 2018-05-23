package it.croway.esperimenti.di;

import javax.inject.Inject;

public class Parent {

	public Child child;

	@Inject
	void setChild(ChildImpl c) {
		this.child = c;
	}
	
	public void test() {
		System.out.println(child);
		
		System.out.println("Parent");
	}
}

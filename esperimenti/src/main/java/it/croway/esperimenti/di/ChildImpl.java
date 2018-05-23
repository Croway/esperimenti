package it.croway.esperimenti.di;

import javax.annotation.Resource;

@Resource
public class ChildImpl implements Child {

	public ChildImpl(int b) {
		System.out.println(b);
	}
	
	public ChildImpl() {
		System.out.println("Child Impl");
	}
	
	public int test(int a) {
		// TODO Auto-generated method stub
		return 0;
	}

}

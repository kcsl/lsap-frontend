package edu.iastate.sdmay1809.shared;

import java.util.ArrayList;

public class ArrayListAnySize<E> extends ArrayList<E> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4751507894181781665L;

	public ArrayListAnySize(int i) {
		super(i);
	}

	@Override
    public void add(int index, E element){
        if(index >= 0 && index <= size()){
            super.add(index, element);
            return;
        }
        int insertNulls = index - size();
        for(int i = 0; i < insertNulls; i++){
            super.add(null);
        }
        super.add(element);
    }
}

/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.userlibrary;

import org.parallelme.userlibrary.function.Filter;
import org.parallelme.userlibrary.function.Foreach;
import org.parallelme.userlibrary.function.Map;
import org.parallelme.userlibrary.function.Reduce;

/**
 * This array currently supports single-dimension arrays with numerical types
 * defined by ParallelME.
 *
 * Future versions should be written to support n-dimensional configurations.
 *
 * @author Pedro Caldeira
 * @param <R>
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class GenericArray<E> {
	private Object array;	

	/**
	 * @param array
	 *            Primitive type array that will provide the data for this
	 *            array.
	 * @param typeParameterClass
	 *            Class object containing necessary information from
	 *            parametrized type E. This information will be used during
	 *            sequential executions in Java.
	 */
	public GenericArray(Object array) {
		this.array = array;									
	}

	/**
	 * Returns the inner array that was informed in the constructor with any
	 * changes that may have been performed on iterator's calls.
	 */
	public Object toJavaArray() {
		return array;
	}

	/**
	 * Fills an informed array with the data stored in the inner array.
	 */
	public void toJavaArray(final Object array) {
		if (java.lang.reflect.Array.getLength(array) != java.lang.reflect.Array
				.getLength(this.array)) {
			throw new RuntimeException("Array sizes' differ.");
		}
		for (int i = 0; i < java.lang.reflect.Array.getLength(array); i++) {
			java.lang.reflect.Array.set(array, i,
					java.lang.reflect.Array.get(this.array, i));
		}
	}
	

	/**element
	 * {@inheritDoc}
	 */	
	public void foreach(Foreach<E> userFunction) {
		E element;
		for (int i = 0; i < java.lang.reflect.Array.getLength(array); i++) {				
			element = userFunction.function( (E) java.lang.reflect.Array.get(array, i));
			java.lang.reflect.Array.set(array, i, element);
		}
	}

	/**
	 * {@inheritDoc}
	 */	
	public E reduce(Reduce<E> userFunction) {
		int length = java.lang.reflect.Array.getLength(array);
		if (length > 0) {				
			E ret = (E) java.lang.reflect.Array.get(array, 0);
			for (int i = 1; i < length; i++) {					
				E element = (E) java.lang.reflect.Array.get(array, i);					
				ret = userFunction.function(ret, element);
			}
			return ret;
		} else {
			throw new RuntimeException("Failure reducing empty array.");
		}
	}

	public GenericArray<E> splitMap(Class<E> classR, int slice_size, Reduce<E> userFunction){		
		int length = java.lang.reflect.Array.getLength(this.array)/slice_size;
		E foo;
		try {
			foo = classR.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		
		Object retArray = java.lang.reflect.Array.newInstance(
				foo.getClass(), length);
		for(int i = 0; i < length ; i++){
			GenericArray currentSubArray = this.subArray(classR, i*slice_size, slice_size);
			E retReduce = (E) currentSubArray.reduce(userFunction);
			java.lang.reflect.Array.set(retArray, i, retReduce);
		}
		return new GenericArray<E>(retArray);		
	}
	
	private GenericArray<E> subArray(Class<E> classR, int begin, int size){
		try {
			E foo = classR.newInstance();
			Object subarray = java.lang.reflect.Array.newInstance(foo.getClass(), size);
			for(int i = 0; i < size; i++){ //Possible overflow here
				java.lang.reflect.Array.set(subarray, i, java.lang.reflect.Array.get(this.array, begin+i));
			}
			return new GenericArray(subarray);
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}		
	}
	
	
	/**
	 * {@inheritDoc}
	 * @param <R>
	 */	
	public <R> GenericArray<R> map(Map<R, E> userFunction) {
		int length = java.lang.reflect.Array.getLength(array);			
		R[] retArray = (R[]) new Object[length];			
		if (length > 0) {
			for (int i = 0; i < length; i++) {					
				E element = (E) java.lang.reflect.Array.get(array, i);					
				E ret = (E) userFunction.function(element);
				java.lang.reflect.Array.set(retArray, i, ret);
			}
		}
		return new GenericArray<R>(retArray);
	}

	/**
	 * {@inheritDoc}
	 */	
	public GenericArray<E> filter(Filter<E> userFunction) {
		int length = java.lang.reflect.Array.getLength(array);
		boolean[] filterIndexes = new boolean[length];
		int filterLength = 0;
		for (int i = 0; i < length; i++) {				
			E element = (E) java.lang.reflect.Array.get(array, i);				
			boolean functionRet = userFunction.function(element);
			if (functionRet) {
				filterLength++;
			}
			filterIndexes[i] = functionRet;
		}			
		E[] retArray = (E[]) new Object[filterLength];

		int i = 0;
		for (int j = 0; j < length; j++) {
			if (filterIndexes[j]) {
				java.lang.reflect.Array.set(retArray, i,
						java.lang.reflect.Array.get(array, j));
				i++;
			}
		}
		return new GenericArray<E>(retArray);
	}
}

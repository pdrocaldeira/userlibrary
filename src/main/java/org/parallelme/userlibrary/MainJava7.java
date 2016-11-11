package org.parallelme.userlibrary;

import java.util.Arrays;
import java.util.Random;

import org.parallelme.userlibrary.datatype.Int16;
import org.parallelme.userlibrary.datatype.Int32;
import org.parallelme.userlibrary.datatype.ObjectArray;
import org.parallelme.userlibrary.function.Foreach;
import org.parallelme.userlibrary.function.Map;
import org.parallelme.userlibrary.function.Reduce;

public class MainJava7 {
	public static void main(String[] args) {
		int i = 0;
		int word_size = 4;
		int words_in_a_book = word_size * 4;
		Random random = new Random();
		
		
		int[] book = new int[words_in_a_book];
		int[] target_word = new int[word_size];
		
		for (int x = 0; x < book.length; x++) {
			book[x] = random.nextInt(10);
		}
		
		for (int x = 0; x < target_word.length; x++) {
			target_word[x] = random.nextInt(10);
		}
		
		GenericArray<Integer> generic_array_book = new GenericArray<Integer>(book);
		
		Array<Int32> arrayBook = new Array<Int32>(book, Int32.class);
		
		System.out.println("Book: "+Arrays.toString(book));
		System.out.println("Target Word: "+Arrays.toString(target_word));
		
		generic_array_book.foreach(new Foreach<Integer>(){
			public Integer function(Integer element){
				element++;
				return element;
			}
		});
		generic_array_book.foreach(new Foreach<Integer>(){
			public Integer function(Integer element){
				System.out.println(element);
				return element;
			}
		});
		
		GenericArray<Integer> generic_array_after_map = generic_array_book.map(new Map<Integer, Integer>(){

			@Override
			public Integer function(Integer element) {
				return element+2;
			}
			
		});
		
		generic_array_after_map.foreach(new Foreach<Integer>(){
			public Integer function(Integer element){
				System.out.print(element+"-");
				return element;
			}
		});
		
		Integer after_reduce = generic_array_after_map.reduce(new Reduce<Integer>(){
			public Integer function(Integer element1, Integer element2){
				element1 += element2;
				return element1;
			}
		});
		
		System.out.println("\nAfter reduce: "+after_reduce);
		
		Array<Int32> afterSplitMap = arrayBook.splitMap(Int32.class, word_size, new Reduce<Int32>(){
			@Override
			public Int32 function(Int32 element1, Int32 element2){				
				element1.value += element2.value;
				return element1;
			}
		});						
		
		int[][] tmp2 = new int[2][2];
		
		for (int x = 0; x < tmp2.length; x++) {
			for (int j = 0; j < tmp2[x].length; j++){
				tmp2[x][j] = ++i;
			}			
		}
		/*
		ObjectArray[] object_array_book = new ObjectArray[64];		
		int[] word;			
		
		for(int index = 0; index < object_array_book.length; index++){
			word = new int[word_size];
			for(int index2 = 0; index2 < word_size; index2++){
				word[index2] =  random.nextInt(10);
			}
			object_array_book[index] = new ObjectArray(word);
		}		
		
		Array<ObjectArray> arrayObjArrayBook = new Array<ObjectArray>(book, ObjectArray.class);
		
		Array<ObjectArray> afterMap = arrayObjArrayBook.map(ObjectArray.class, new Map<ObjectArray, ObjectArray>(){

			@Override
			public ObjectArray function(ObjectArray element) {
				int[] compared = new int[word_size];
				for(int i = 0; i < word_size; i++){
					compared[i] = java.lang.reflect.Array.getInt(element.getValue(), i) != target_word[i] ? 0 : 1;
				}
				return new ObjectArray(compared);
			}			
		});
		
		System.out.println("Target Word: "+target_word);
		afterMap.foreach(new Foreach<ObjectArray>(){

			@Override
			public void function(ObjectArray element) {
				System.out.println(element.getValue());
			}
			
		});*/
		
		Array<Int32> array = new Array<Int32>(book, Int32.class);
		System.out.println("Elements: ");
		array.toJavaArray(book);
		for (int x = 0; x < book.length; x++) {
			System.out.println("tmp[" + x + "] = " + book[x]);
		}
		
		System.out.println("Reduce Java 7: ");
		Int32 ret7 = array.reduce(new Reduce<Int32>(){
			@Override
			public Int32 function(Int32 element1, Int32 element2){
				element1.value += element2.value;
				return element1;
			}
		});

		System.out.println(ret7.value);
	}

}

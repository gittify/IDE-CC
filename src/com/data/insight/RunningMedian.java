package com.data.insight;

import java.util.Collections;
import java.util.PriorityQueue;

public class RunningMedian {

	
	PriorityQueue<Double> minHeap = new PriorityQueue<Double>();
	PriorityQueue<Double> maxHeap = new PriorityQueue<Double>(10,Collections.reverseOrder());
	Double firstVal = null;
	Double secondVal = null;	
	Double median = null;
	
	/** 
	 * For the first two elements add smaller one to the maxHeap, 
	 * and bigger one to the minHeap 
	 * 
	 * Add next item to one of the heaps
   	 *	if next item is smaller than maxHeap root add it to maxHeap,
   	 *	else add it to minHeap
	 */
	public Double runMedian(Double count){
		
		//initialize both the heaps together
		if (minHeap.isEmpty() &&  maxHeap.isEmpty())
		{
			if (firstVal == null) {
				firstVal = count;
				median = firstVal;
			}
			else{
				secondVal = count;
				if (firstVal < secondVal)
				{
					minHeap.offer(secondVal);
					maxHeap.offer(firstVal);
				}
				else{
					minHeap.offer(firstVal);
					maxHeap.offer(secondVal);
				}
				median = (firstVal + secondVal)/2;
			}
		}
		else{
			if (count < maxHeap.peek()){
				maxHeap.offer(count);
			}
			else{
				minHeap.offer(count);
			}
		
		
			balanceHeaps();
			calculateRunningMedian();
			
		}
		
		return median;
	}
	
	/**
	 *  Median calculation
	 *  Even number of elements in both heaps then take the average
	 *  Odd number then the heap with more elements
	 * */
	private void calculateRunningMedian() {
		//the heaps contain equal elements
		if (maxHeap.size() == minHeap.size())
		{
			//median = (root of maxHeap + root of minHeap)/2
			median = (maxHeap.peek()+minHeap.peek())/2;
			
		}
		else{
		// median = root of the heap with more elements
		median = maxHeap.size() > minHeap.size()?maxHeap.peek():minHeap.peek();
		}
		
	}

	/**
	 * Balance the heaps (after this step heaps will be either balanced or
     * one of them will contain 1 more item)
     * */
	private void balanceHeaps() {
		
		if (Math.abs(maxHeap.size() - minHeap.size()) > 1)
		{	
			if (maxHeap.size() > minHeap.size()){
				minHeap.add(maxHeap.poll());
			}
			else{
				maxHeap.add(minHeap.poll());
			}
		}
		
	}


	public static void main(String[] args) {
		
		RunningMedian rm = new RunningMedian();
		rm.runMedian(11.0);
		rm.runMedian(0.0);
		rm.runMedian(8.0);

	}

}

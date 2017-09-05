package Taxiruf;

import java.util.ListIterator;

public interface test
{
	void showTaxi(int taxi_id);
	void showWaiting();
	void maintainRoad(int tag,Coordinate x,Coordinate y);
	ListIterator<Coordinate> showTracker(int taxi_id,int times);
}

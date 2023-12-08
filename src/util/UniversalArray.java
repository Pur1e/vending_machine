package util;

public interface UniversalArray<T> {
	void add(T element);
	
	void addAll(Object[] elements);
	
	void delete(int index);
	
	int size();
	
	T get(int index);
	
	<E> E[] toArray(E[] a);
	
	Object[] toArray();
}

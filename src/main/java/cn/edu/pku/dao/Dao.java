package cn.edu.pku.dao;

public abstract class Dao<T,D> {
	// Basic Dao<> Class
	// T is read() method's return type
	// D is write() method's input type

	public abstract T read();

	public abstract void write(D data);
}

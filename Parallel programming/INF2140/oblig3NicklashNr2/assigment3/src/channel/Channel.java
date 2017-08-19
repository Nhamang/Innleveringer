package channel;

public interface Channel<T> {
	public void send(T v) throws InterruptedException;
	public T receive() throws InterruptedException;
}

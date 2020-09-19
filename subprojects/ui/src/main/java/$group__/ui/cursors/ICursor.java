package $group__.ui.cursors;

public interface ICursor
		extends AutoCloseable {
	long getHandle();

	@Override
	void close();
}

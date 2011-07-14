package zanoccio.javakit.lambda;

public interface Function2<R, A, B> {
	public R call(A p1, B p2) throws Throwable;
}

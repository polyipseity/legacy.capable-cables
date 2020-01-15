package etaoinshrdlcumwfgypbvkjxqz.$modId__.utilities.constructs;

import net.minecraftforge.fml.common.discovery.ASMDataTable;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;

public interface IAnnotationProcessor<A extends Annotation> {
	/* SECTION methods */

	Class<A> annotationType();

	void process(ASMDataTable asm);


	/* SECTION static methods */

	static String getMessage(IAnnotationProcessor<?> processor, @Nullable String msg) { return "Process annotation '" + processor.annotationType() + "'" + (msg == null || msg.isEmpty() ? "" : ": " + msg); }
}

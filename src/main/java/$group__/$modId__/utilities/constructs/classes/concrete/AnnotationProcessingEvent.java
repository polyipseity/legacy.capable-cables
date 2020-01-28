package $group__.$modId__.utilities.constructs.classes.concrete;

import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class AnnotationProcessingEvent extends Event {
	/* SECTION variables */

	protected final String modId;
	protected final ASMDataTable asm;


	/* SECTION constructors */

	public AnnotationProcessingEvent(String modId, ASMDataTable asm) {
		this.modId = modId;
		this.asm = asm;
	}


	/* SECTION getters & setters */

	public String getModId() { return modId; }

	public ASMDataTable getAsm() { return asm; }
}

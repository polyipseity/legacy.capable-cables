package $group__.common.events;

import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public class AnnotationProcessingEvent extends Event {
	protected final String modId;
	protected final ASMDataTable asm;
	@Nullable
	protected final Logger logger;

	public AnnotationProcessingEvent(String modId, ASMDataTable asm, @Nullable Logger logger) {
		this.modId = modId;
		this.asm = asm;
		this.logger = logger;
	}

	public String getModId() { return modId; }

	public ASMDataTable getAsm() { return asm; }

	@Nullable
	public Logger getLogger() { return logger; }
}

package $group__.utilities;

import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.apache.logging.log4j.message.SimpleMessage;

import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static $group__.utilities.PreconditionUtilities.checkArgumentTypes;
import static $group__.utilities.PreconditionUtilities.checkArrayContentType;
import static $group__.utilities.PrimitiveUtilities.PRIMITIVE_DATA_TYPE_TO_DEFAULT_VALUE_MAP;
import static java.util.Arrays.asList;

public enum LoggerUtilities {
	/* MARK empty */;

	public static final String
			PREFIX_MOD_LIFECYCLE = "Mod lifecycle: ",
			PREFIX_REFLECTION = "Reflection: ",
			PREFIX_INVOCATION = "Invocation: ";


	public enum EnumMessages {
		FACTORY_SIMPLE_MESSAGE("{}",
				String.class) {
			@Override
			protected SimpleMessage makeMessage1(Object... args) { return new SimpleMessage((String) args[0]); }
		},
		FACTORY_PARAMETERIZED_MESSAGE("{}", true,
				String.class, Object[].class) {
			@Override
			protected Message makeMessage1(Object... args) {
				return new ParameterizedMessage((String) args[0],
						(Object[]) args[1]);
			}
		},
		PREFIX_MOD_LIFECYCLE_MESSAGE(PREFIX_MOD_LIFECYCLE + "{}", false,
				Message.class) {
			private ParameterizedMessage makeMessage0(Message msg) {
				return new ParameterizedMessage(getMessage(),
						msg.getFormattedMessage());
			}

			@Override
			protected Message makeMessage1(Object... args) {
				return makeMessage0((Message) args[0]);
			}
		},
		SUFFIX_WITH_THROWABLE("{}",
				Message.class, Throwable.class) {
			private ParameterizedMessage makeMessage0(Message msg, Throwable t) {
				return new ParameterizedMessage(getMessage(),
						msg.getFormattedMessage(),
						t);
			}

			@Override
			protected ParameterizedMessage makeMessage1(Object... args) {
				return makeMessage0((Message) args[0],
						(Throwable) args[1]);
			}
		},
		REFLECTION_UNABLE_TO_GET_MEMBER(PREFIX_REFLECTION + "Unable to get {} of class '{}'{}{}", true, 1,
				String.class, Class.class, String.class, Class[].class) {
			private ParameterizedMessage makeMessage0(String description, Class<?> clazz, @Nullable String name,
			                                          Class<?>... arguments) {
				return new ParameterizedMessage(getMessage(),
						description,
						clazz.toGenericString(),
						EnumMessages.optionalNonnull(name, n -> ", name '" + n + '\''),
						arguments.length == 0 ? "" : ", arguments '" + Arrays.toString(arguments) + '\'');
			}

			@Override
			protected ParameterizedMessage makeMessage1(Object... args) {
				return makeMessage0((String) args[0],
						(Class<?>) args[1], (String) args[2], (Class<?>[]) args[3]);
			}
		},
		REFLECTION_UNABLE_TO_SET_ACCESSIBLE(PREFIX_REFLECTION + "Unable to set {} '{}'{} {}accessible", 1,
				String.class, AccessibleObject.class, Class.class, boolean.class) {
			private ParameterizedMessage makeMessage0(String description, AccessibleObject reflected, @Nullable Class<
					?> clazz, boolean accessible) {
				return new ParameterizedMessage(getMessage(),
						description,
						reflected,
						EnumMessages.optionalNonnull(clazz, c -> " of class '" + c.toGenericString() + '\''),
						accessible ? "" : "in");
			}

			@Override
			protected ParameterizedMessage makeMessage1(Object... args) {
				return makeMessage0((String) args[0],
						(AccessibleObject) args[1], (Class<?>) args[2], (boolean) args[3]);
			}
		},
		REFLECTION_UNABLE_TO_G_SET_FIELD(PREFIX_REFLECTION + "Unable to {}et field '{}'{} of class '{}'{}", 2,
				boolean.class, Field.class, Object.class, Object.class) {
			private ParameterizedMessage makeMessage0(boolean set, Field field, @Nullable Object obj,
			                                          @Nullable Object value) {
				return new ParameterizedMessage(getMessage(),
						set ? 's' : 'g',
						field.toGenericString(),
						EnumMessages.optionalNonnull(obj, o -> " of object '" + obj + '\''),
						field.getDeclaringClass().toGenericString(),
						set ? " to value '" + value + '\'' : "");
			}

			@Override
			protected ParameterizedMessage makeMessage1(Object... args) {
				return makeMessage0((boolean) args[0],
						(Field) args[1], args[2], args[3]);
			}
		},
		INVOCATION_UNABLE_TO_FIND_METHOD_HANDLE(PREFIX_INVOCATION + "Unable to find {} of class '{}'{}{}{}", 3,
				String.class, Class.class, String.class, MethodType.class, Class.class) {
			private ParameterizedMessage makeMessage0(String description, Class<?> clazz, @Nullable String name,
			                                          @Nullable MethodType methodType,
			                                          @Nullable Class<?> typeOrSpecialCaller) {
				return new ParameterizedMessage(getMessage(),
						description,
						clazz.toGenericString(),
						EnumMessages.optionalNonnull(name, n -> ", name '" + n + '\''),
						EnumMessages.optionalNonnull(methodType, a -> ", method type '" + a + '\''),
						methodType == null ? EnumMessages.optionalNonnull(typeOrSpecialCaller,
								s -> ", type '" + s.toGenericString() + '\'') :
								EnumMessages.optionalNonnull(typeOrSpecialCaller,
										s -> ", special caller '" + s.toGenericString() + '\''));
			}

			@Override
			protected ParameterizedMessage makeMessage1(Object... args) {
				return makeMessage0((String) args[0],
						(Class<?>) args[1], (String) args[2], (MethodType) args[3], (Class<?>) args[4]);
			}
		},
		INVOCATION_UNABLE_TO_UN_REFLECT_MEMBER(PREFIX_INVOCATION + "Unable to un-reflect {} '{}' with lookup '{}'{}", 1,
				String.class, Member.class, Lookup.class, Class.class) {
			private ParameterizedMessage makeMessage0(String description, Member member, Lookup lookup,
			                                          @Nullable Class<?> specialCaller) {
				return new ParameterizedMessage(getMessage(),
						description,
						member,
						lookup,
						EnumMessages.optionalNonnull(specialCaller,
								s -> ", special caller '" + s.toGenericString() + '\''));
			}

			@Override
			protected ParameterizedMessage makeMessage1(Object... args) {
				return makeMessage0((String) args[0],
						(Member) args[1], (Lookup) args[2], (Class<?>) args[3]);
			}
		},
		INVOCATION_UNABLE_TO_INVOKE_METHOD_HANDLE(PREFIX_INVOCATION + "Unable to invoke method handle '{}'{}", true,
				MethodHandle.class, Object[].class) {
			private ParameterizedMessage makeMessage0(MethodHandle method, Object... arguments) {
				return new ParameterizedMessage(getMessage(),
						method,
						arguments.length == 0 ? "" : ", arguments '" + Arrays.toString(arguments) + '\'');
			}

			@Override
			protected ParameterizedMessage makeMessage1(Object... args) {
				return makeMessage0((MethodHandle) args[0],
						(Object[]) args[1]);
			}
		};


		protected final String message;
		protected final Class<?>[] argTypes;
		@Nullable
		protected final Class<?>[] argTypeOptionals;
		@Nullable
		protected final Class<?> argVarargs;


		EnumMessages(@SuppressWarnings("SameParameterValue") String message, Class<?>... argTypes) { this(message, false, argTypes); }

		EnumMessages(String message, boolean varargs, Class<?>... argTypes) { this(message, varargs, 0, argTypes); }

		EnumMessages(String message, boolean varargs, int argTypeOptionals, Class<?>... argTypes) {
			this.message = message;
			int argTypesL = argTypes.length;

			if (varargs) {
				int l = argTypesL - 1;
				this.argVarargs = argTypes[l].getComponentType();
				argTypes = Arrays.copyOf(argTypes, l);
			} else this.argVarargs = null;

			if (argTypeOptionals > 0) {
				int l = argTypesL - argTypeOptionals;
				this.argTypeOptionals = Arrays.copyOfRange(argTypes, l - 1, argTypesL);
				argTypes = Arrays.copyOf(argTypes, l);
			} else this.argTypeOptionals = null;

			this.argTypes = argTypes;
		}

		EnumMessages(String message, int argTypeOptionals, Class<?>... argTypes) {
			this(message, false,
					argTypeOptionals, argTypes);
		}

		private static <O> String optionalNonnull(@Nullable O optional, Function<? super O, String> whenPresent) { return Optional.ofNullable(optional).map(whenPresent).orElse(""); }

		public final Message makeMessage(Object... args) {
			checkArgumentTypes(getArgTypes(), args);
			@SuppressWarnings("unchecked") final List<Object>[] argsList = new List[]{asList(args)};
			final int[] argTsL = {getArgTypes().length};
			final int[] argsL = {argsList[0].size()};

			getArgTypeOptionals().map(CastUtilities::<Class<?>[]>upcast).ifPresent(argTOs -> {
				int argTOsL = argTOs.length, matched = 0;
				ArrayList<Object> argOptionals = new ArrayList<>(argTOsL);
				for (int i = 0, j = argTsL[0] - 1; i < argTOsL && j < argsL[0]; ++i, ++j) {
					@Nullable Object arg = args[j];
					Class<?> argTO = argTOs[i];
					if (arg == null || argTO.isInstance(arg)) {
						argOptionals.add(i, arg);
						++matched;
					} else argOptionals.add(i, PRIMITIVE_DATA_TYPE_TO_DEFAULT_VALUE_MAP.get(argTO));
				}

				for (int i = 0; i < matched; ++i) argsList[0].remove(argTsL[0] + i);
				argsList[0].addAll(argTsL[0], argOptionals);
				argTsL[0] += argTOsL;
				argsL[0] += argTOsL - matched;
			});

			getArgVarargs().ifPresent(argVarargs -> {
				@Nullable Object[] varargs = null;
				if (argTsL[0] < argsL[0]) {
					Object varargs1 = args[argTsL[0]];
					if (varargs1.getClass().isArray()) varargs = (Object[]) varargs1;
				}

				if (varargs == null) varargs = Arrays.copyOfRange(args, argTsL[0], argsL[0]);

				checkArrayContentType(argVarargs, varargs);
				argsList[0].set(argTsL[0], varargs);
				argsList[0] = argsList[0].subList(0, ++argTsL[0]);
			});

			return makeMessage1(argsList[0].toArray());
		}

		@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
		protected Class<?>[] getArgTypes() { return argTypes; }

		protected Optional<? extends Class<?>[]> getArgTypeOptionals() { return Optional.ofNullable(argTypeOptionals); }

		protected Optional<? extends Class<?>> getArgVarargs() { return Optional.ofNullable(argVarargs); }

		protected Message makeMessage1(Object... args) { return new ParameterizedMessage(getMessage(), args); }

		protected String getMessage() { return message; }
	}
}

package com.santos.ravenapi.infra.validation;

import java.util.Objects;

public class ArgumentValidator {

	public synchronized static void validate(Object... methodArguments) {
		for (Object argument : methodArguments) {
			try {
				Objects.requireNonNull(argument);
			} catch (NullPointerException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}
}

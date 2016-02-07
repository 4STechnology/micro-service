/*
 * @(#) biz.fstechnology.micro.common.AutoCloseableWrapper
 * Copyright (c) 2016 4S Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * 	Unless required by applicable law or agreed to in writing, software
 * 	distributed under the License is distributed on an "AS IS" BASIS,
 * 	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 	See the License for the specific language governing permissions and
 * 	limitations under the License.
 */
package biz.fstechnology.micro.common;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Non-AutoCloseable instance wrapper for try-with-rexources sentence.
 * 
 * @author Maruyama Takayuki
 * @since 2016/02/02
 */
public class AutoCloseableWrapper<T> implements AutoCloseable {

	private final T target;

	private final Consumer<T> closeOperation;

	protected AutoCloseableWrapper(T target, Consumer<T> closeOperation) {
		this.target = target;
		this.closeOperation = closeOperation;
	}

	public AutoCloseableWrapper(Supplier<T> targetInitializer, Consumer<T> closeOperation) {
		this(targetInitializer.get(), closeOperation);
	}

	public T unwrap() {
		return target;
	}

	/**
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() {
		closeOperation.accept(target);
	}

}

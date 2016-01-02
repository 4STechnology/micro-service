/*
 * @(#) biz.fstechnology.micro.common.ServiceConnection
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

/**
 * TODO Type Description
 * 
 * @author Maruyama Takayuki
 * @since 2016/01/01
 */
public interface ServiceConnection {

	/**
	 * Call the api with {@code request} argument.
	 * <p>
	 * This method should be synchronous. This method caller thread will be
	 * blocked until the service process complete.
	 * </p>
	 * 
	 * @param request
	 * @return
	 * @see {@link ServiceConnection#callAsync(Request, Consumer)} The
	 *      Asynchronous implementation
	 */
	<T, U> Result<U> call(Request<T> request);

	/**
	 * Call the api with {@code request} argument.
	 * <p>
	 * This method should be Asynchronous. This method caller thread will NOT be
	 * blocked until the service process complete. When the process complete,
	 * call {@code Consumer#accept}.
	 * </p>
	 * 
	 * @param request
	 * @param callback
	 * @see {@link Consumer#accept(Object)}
	 * @see {@link ServiceConnection#call(Request)} The Synchronous
	 *      implementation.
	 */
	<T, U> void callAsync(Request<T> request, Consumer<Result<U>> callback);

}

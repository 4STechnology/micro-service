/*
 * @(#) biz.fstechnology.micro.common.Version
 * Copyright (c) 2015 4S Technology
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

import java.io.Serializable;

import lombok.Getter;

/**
 * Version Descriptor
 * 
 * @author Maruyama Takayuki
 * @since 2015/12/28
 */
@Getter
public final class Version implements Serializable {

	/** $Comment$ */
	private static final long serialVersionUID = 150235198923818634L;
	private final String major;
	private final String minor;
	private final String patch;
	private final String additional;

	public Version(String major, String minor, String patch) {
		this(major, minor, patch, null);
	}

	public Version(String major, String minor, String patch, String additional) {
		this.major = major;
		this.minor = minor;
		this.patch = patch;
		this.additional = additional;
	}

}

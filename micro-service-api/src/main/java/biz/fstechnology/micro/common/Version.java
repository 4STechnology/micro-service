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
	private static final long serialVersionUID = -2201493199969773823L;
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

	/**
	 * Returns SemVer-format version
	 * 
	 * @see java.lang.Object#toString()
	 * @see <a href="http://semver.org/">http://semver.org/</a>
	 */
	@Override
	public String toString() {
		final String delim = ".";
		StringBuffer sb = new StringBuffer() //
				.append(major).append(delim) //
				.append(minor).append(delim) //
				.append(patch).append(delim);
		if (additional != null) {
			sb.append(additional);
		}
		return sb.toString();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((additional == null) ? 0 : additional.hashCode());
		result = prime * result + ((major == null) ? 0 : major.hashCode());
		result = prime * result + ((minor == null) ? 0 : minor.hashCode());
		result = prime * result + ((patch == null) ? 0 : patch.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Version other = (Version) obj;
		if (additional == null) {
			if (other.additional != null)
				return false;
		} else if (!additional.equals(other.additional))
			return false;
		if (major == null) {
			if (other.major != null)
				return false;
		} else if (!major.equals(other.major))
			return false;
		if (minor == null) {
			if (other.minor != null)
				return false;
		} else if (!minor.equals(other.minor))
			return false;
		if (patch == null) {
			if (other.patch != null)
				return false;
		} else if (!patch.equals(other.patch))
			return false;
		return true;
	}

}

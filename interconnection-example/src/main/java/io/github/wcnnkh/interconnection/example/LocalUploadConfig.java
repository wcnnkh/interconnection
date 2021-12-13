package io.github.wcnnkh.interconnection.example;

import io.basc.framework.beans.annotation.ConfigurationProperties;
import lombok.Data;

@Data
@ConfigurationProperties("local.upload")
public class LocalUploadConfig {
	private String host = "/";
	private String path = "/upload/";
	private String filePath;
	/**
	 * 过期时间：分钟(默认不删除)
	 */
	private long expirationTime = -1;

	/**
	 * 是否保留原始名称
	 */
	private boolean retainOriginFilename = false;
}

package io.github.wcnnkh.interconnection.core.support;

import java.util.Map;

import io.basc.framework.context.result.DataResult;
import io.github.wcnnkh.interconnection.core.SyncRequest;

/**
 * 同步处理器，一般用于同步到第三方
 * 
 * @author shuchaowen
 *
 */
public interface SyncProcessor {
	DataResult<Map<String, String>> sync(SyncRequest request);
}

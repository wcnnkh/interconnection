package io.github.wcnnkh.interconnection.example.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import io.basc.framework.context.annotation.EnableCondition;
import io.basc.framework.context.annotation.Provider;
import io.basc.framework.context.ioc.annotation.Autowired;
import io.basc.framework.context.result.DataResult;
import io.basc.framework.context.result.ResultFactory;
import io.basc.framework.env.Sys;
import io.basc.framework.http.HttpMethod;
import io.basc.framework.http.MediaType;
import io.basc.framework.io.FileUtils;
import io.basc.framework.io.Resource;
import io.basc.framework.logger.Logger;
import io.basc.framework.logger.LoggerFactory;
import io.basc.framework.net.message.multipart.MultipartMessage;
import io.basc.framework.timer.boot.annotation.Crontab;
import io.basc.framework.util.CollectionUtils;
import io.basc.framework.util.StringUtils;
import io.basc.framework.util.TimeUtils;
import io.basc.framework.util.XUtils;
import io.basc.framework.web.ServerHttpRequest;
import io.basc.framework.web.resource.StaticResourceLoader;
import io.github.wcnnkh.interconnection.example.LocalUploadConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 示例
 * 
 * @author shuchaowen
 *
 */
@Tag(name = "文件上传示例")
@Path("/example/upload")
@EnableCondition(condition = "local.upload.enable")
@Provider
public class ExampleUploadController implements StaticResourceLoader {
	private static Logger logger = LoggerFactory.getLogger(ExampleUploadController.class);

	@Autowired
	private LocalUploadConfig localUploadConfig;
	@Autowired
	private ResultFactory resultFactory;

	@Path("multiple")
	@Operation(description = "批量上传")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA_VALUE)
	public DataResult<List<String>> multiple(
			@FormParam("files") @Parameter(schema = @Schema(type = "string", format = "binary"), required = true)
			// @Parameter(array = @ArraySchema(schema = @Schema(type = "string", format =
			// "binary"), arraySchema = @Schema(type = "array")), required = true)
			// @ArraySchema(schema = @Schema(type = "string", format = "binary"),
			// arraySchema = @Schema(type = "array"))
			List<MultipartMessage> messages) throws IllegalStateException, IOException {
		if (CollectionUtils.isEmpty(messages)) {
			return resultFactory.error("无文件");
		}

		List<String> urls = new ArrayList<>();
		for (MultipartMessage message : messages) {
			urls.add(upload(message));
		}
		return resultFactory.success(urls);
	}

	@Operation(description = "单个上传")
	@Path("single")
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA_VALUE)
	public DataResult<String> single(
			@FormParam("file") @Parameter(schema = @Schema(type = "string", format = "binary"), required = true) MultipartMessage message)
			throws IllegalStateException, IOException {
		if (message == null) {
			return resultFactory.error("无文件");
		}

		if (message.getSize() == 0) {
			return resultFactory.error("文件大小为0");
		}
		return resultFactory.success(upload(message));
	}

	@Override
	public Resource getResource(ServerHttpRequest request) {
		if (!(request.getMethod() == HttpMethod.GET && request.getPath().startsWith(localUploadConfig.getPath()))) {
			return null;
		}

		String path = localUploadConfig.getFilePath() + "/"
				+ request.getPath().substring(localUploadConfig.getPath().length());
		return Sys.getEnv().getResourceLoader().getResource(StringUtils.cleanPath(path));
	}

	private String upload(MultipartMessage message) throws IllegalStateException, IOException {
		String name = message.getOriginalFilename();
		if (!localUploadConfig.isRetainOriginFilename()) {
			// 不保留\
			String ext = StringUtils.getFilenameExtension(name);
			if (StringUtils.isEmpty(ext)) {
				name = XUtils.getUUID();
			} else {
				name = XUtils.getUUID() + "." + ext;
			}
		}

		String path = "/" + TimeUtils.format(System.currentTimeMillis(), "yyyy/MM/dd") + "/" + name;
		File file = new File(StringUtils.cleanPath(localUploadConfig.getFilePath() + path));
		try {
			message.transferTo(file);
		} finally {
			message.close();
		}
		String url = StringUtils.cleanPath(localUploadConfig.getHost() + localUploadConfig.getPath() + path);
		logger.info("上传文件{}, 返回路径: {}", message.getOriginalFilename(), url);
		return url;
	}

	@Crontab(name = "删除过期文件")
	public void deleteTask() {
		if (localUploadConfig.getExpirationTime() <= 0) {
			return;
		}

		FileUtils.stream(new File(localUploadConfig.getFilePath()))
				.filter((f) -> (System.currentTimeMillis() - f.getFile().lastModified()) >= TimeUnit.MINUTES
						.toMillis(localUploadConfig.getExpirationTime()))
				.forEach((f) -> {
					logger.info("删除过期文件：" + f.getFile().getPath());
					f.getFile().delete();
				});
		;
	}
}

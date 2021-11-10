package io.github.wcnnkh.interconnection.web;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import io.basc.framework.beans.annotation.Autowired;
import io.basc.framework.context.result.DataResult;
import io.basc.framework.context.result.Result;
import io.basc.framework.context.result.ResultFactory;
import io.basc.framework.http.MediaType;
import io.basc.framework.web.message.annotation.RequestBody;
import io.github.wcnnkh.interconnection.core.SyncRequest;
import io.github.wcnnkh.interconnection.core.Transaction;
import io.github.wcnnkh.interconnection.core.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "交易处理")
@Path(value = "transaction")
public class TransactionController {
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private ResultFactory resultFactory;

	@Operation(description = "创建交易")
	@Path("create")
	@POST
	public DataResult<Transaction> create() {
		Transaction transaction = transactionService.create();
		return resultFactory.success(transaction);
	}

	@Operation(description = "同步交易")
	@Path("sync")
	@POST
	@Consumes(MediaType.APPLICATION_JSON_VALUE)
	public Result confirm(@RequestBody @Valid SyncRequest request) {
		return transactionService.sync(request);
	}

	@Operation(description = "查询交易")
	@Path("query")
	@GET
	public DataResult<Transaction> query(@QueryParam("transactionId") String transactionId) {
		Transaction transaction = transactionService.getTransaction(transactionId);
		if (transaction == null) {
			return resultFactory.error("交易不存在");
		}

		return resultFactory.success(transaction);
	}
}

package io.github.wcnnkh.interconnection.web;

import io.basc.framework.beans.annotation.Autowired;
import io.basc.framework.context.result.DataResult;
import io.basc.framework.context.result.Result;
import io.basc.framework.context.result.ResultFactory;
import io.basc.framework.web.message.annotation.RequestBody;
import io.github.wcnnkh.interconnection.core.CancelRequest;
import io.github.wcnnkh.interconnection.core.ConfirmRequest;
import io.github.wcnnkh.interconnection.core.Transaction;
import io.github.wcnnkh.interconnection.core.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

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

	@Operation(description = "确认交易")
	@Path("confirm")
	@POST
	public Result confirm(@RequestBody ConfirmRequest request) {
		return transactionService.confirm(request);
	}
	
	@Operation(description = "取消交易")
	@Path("cancel")
	@POST
	public Result cancel(@RequestBody CancelRequest request) {
		return transactionService.cancel(request);
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

package io.github.wcnnkh.interconnection.simple;

import io.basc.framework.context.annotation.Provider;
import io.basc.framework.core.Ordered;
import io.basc.framework.env.Sys;
import io.basc.framework.sqlite.SQLiteDB;
import io.basc.start.data.db.DbDataService;

@Provider(order = Ordered.LOWEST_PRECEDENCE)
public class SqliteDataService extends DbDataService {

	public SqliteDataService() {
		super(new SQLiteDB(Sys.env.getWorkPath() + "/start-data.db"));
	}
}

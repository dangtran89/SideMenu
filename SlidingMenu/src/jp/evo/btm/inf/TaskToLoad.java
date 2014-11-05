package jp.evo.btm.inf;

import jp.evo.btm.model.Result;

public interface TaskToLoad {
	
	Result process();
	void callback(Result result);
	
}

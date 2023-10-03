package llq.fw.cm.services;

import org.springframework.data.domain.Pageable;

import llq.fw.cm.payload.response.BaseResponse;

public interface BaseService<T,U,S,R> {
	T create(U u);
	T update(U u);
	T search(S s, Pageable p);
	T getDetail(R r);
	BaseResponse delete(R r);
	BaseResponse checkExist(R r);
	
}

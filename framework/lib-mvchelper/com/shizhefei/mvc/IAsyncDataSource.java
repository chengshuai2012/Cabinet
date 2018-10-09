package com.shizhefei.mvc;

public interface IAsyncDataSource<DATA> {

	public RequestHandle refresh(ResponseSender<DATA> sender) throws Exception;//刷新 100 clear  add 10

	public RequestHandle loadMore(ResponseSender<DATA> sender) throws Exception;//加载更多 100- > 110

	public boolean hasMore();

}

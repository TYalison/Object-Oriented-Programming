
输入（任务）：
	IF [File_AbsolutePath] if_type THEN then_type
	注：
		命令行输入【区分大小写】
		严格按照规范格式，每条任务的各个要素间仅允许以空格分隔，末尾回车，否则判为无效任务；
		每条任务占一行；
		if_type可选：size-changed、renamed、path-changed、modified；
		then_type可选：record-summary、record-detail、reco；
		最终所有任务输入完毕后在新的一行以“END”作为结束符。

输出：
	注：
		在F:\中新建summary.txt和detail.txt，并将summary和detail的统计信息保存在这两个文件中
		最后修改时间直接有由file.lastModified得到，以ms为单位，未做任何转化
		测试线程结束则扫描线程跟随结束

另：
	与文件操作相关的操作在Test类中；
	scanner每隔10s扫描一次。

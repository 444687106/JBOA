$(function(){
	
	// 查看
	function search(obj) {
		var id = obj.parent().parent().children().html();
		location = 'leaveView?opr=view&id=' + id;
	}
	// 审批
	function sub(obj) {
		var id = obj.parent().parent().children().html();
		location = 'leaveView?opr=approval&id=' + id;
	}
	
	// 查看
	$('.search').click(function(){
		search($(this));
	});

	// 审批
	$('.sub').click(function(){
		sub($(this));
	});
	
	// 查询
	$('#search').click(function(){
		restJson($(this),null);
	});
	
	// 首页
	$('#home').click(function(){
		restJson($(this),1);
	});
	
	// 上一页
	$('#prev').click(function(){
		var pageNo = parseInt($('span#curPage').text()) - 1;
		restJson($(this),pageNo);
	});
	
	// 下一页
	$('#next').click(function(){
		var pageNo = parseInt($('span#curPage').text()) + 1;
		restJson($(this),pageNo);
	});
	
	// 末页
	$('#last').click(function(){
		restJson($(this),$('#totalPage').text());
	});
	
	// 发送异步
	function restJson(obj,pageNo) {
		// 获得查询条件
		var type = $('#type').find("option:selected").text();
		var start = $('#startDate').val();
		var end = $('#endDate').val();
		$.post('restJson.action',{ "type":type,"start":start,"end":end,"pageNo":pageNo },function(result){
			// 清空节点
			$('.leave').remove();
			$('#page').remove();
			$('.list').append(result);
			// 重新绑定事件
			$('.search').on("click",function(){
				search($(this));
			});
			$('.sub').on('click',function(){
				sub($(this));
			});
			$('#home').on('click',function(){
				restJson($(this),1);
			});
			$('#prev').on('click',function(){
				var pageNo = parseInt($('span#curPage').text()) - 1;
				restJson($(this),pageNo);
			});
			$('#next').on('click',function(){
				var pageNo = parseInt($('span#curPage').text()) + 1;
				restJson($(this),pageNo);
			});
			$('#last').on('click',function(){
				restJson($(this),$('#totalPage').text());
			});
		});
	};
	
});
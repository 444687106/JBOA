$(function(){
	
	// 查看
	function search(obj){
		var id = obj.parent().parent().children().html();
		location = 'detail.action?opr=search&id=' + id;
	};
	// 修改
	function edit(obj){
		var id = obj.parent().parent().children().html();
		location = 'detail.action?opr=edit&id=' + id;
	};
	// 审批
	function sub(obj){
		var id = obj.parent().parent().children().html();
		location = 'detail.action?opr=sub&id=' + id;
	}
	
	$('.search').click(function(){
		search($(this));
	});
	$('.edit').click(function(){
		edit($(this));
	});
	$('.sub').click(function(){
		sub($(this));
	});
	
	
	// 点击查询
	$('#search').click(function(){
		page($(this),null);
	});
	
	// 首页
	$('#home').click(function(){
		page($(this),1);
	});
	// 上一页
	$('#prev').click(function(){
		var pageNo = parseInt($('#page td #pageNo').val()) - 1;
		page($(this),pageNo);
	});
	// 下一页
	$('#next').click(function(){
		var pageNo = parseInt($('#page td #pageNo').val()) + 1;
		page($(this),pageNo);
	});
	// 末页
	$('#last').click(function(){
		var pageNo = $('#totalPage').text();
		page($(this),pageNo);
	});
	
	// 分页的方法
	function page(obj,pageNo){
		// 获得参数
		var status = $("#status").find("option:selected").text();
		var startDate = $('#startDate').val();
		var endDate = $('#endDate').val();
		$.post("findJson.action",{"status":status,"startDate":startDate,"endDate":endDate,"pageNo":pageNo},function(result){
			$('.voucher').remove();
			$('#page').remove();
			$('.list').append(result);
			// 绑定事件
			$('#home').on("click",function(){
				page($(this),1);
			});
			$('#prev').on("click",function(){
				var pageNo = parseInt($('#page td #pageNo').val()) - 1;
				page($(this),pageNo);
			});
			$('#next').on("click",function(){
				var pageNo = parseInt($('#page td #pageNo').val()) + 1;
				page($(this),pageNo);
			});
			$('#last').on("click",function(){
				var pageNo = $('#totalPage').text();
				page($(this),pageNo);
			});
			$('.search').on('click',function(){
				search($(this));
			});
			$('.edit').on('click',function(){
				edit($(this));
			});
			$('.sub').click(function(){
				sub($(this));
			});
		});
	};
});
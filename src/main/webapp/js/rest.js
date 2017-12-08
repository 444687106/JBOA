$(function(){
	
	// �鿴
	function search(obj) {
		var id = obj.parent().parent().children().html();
		location = 'leaveView?opr=view&id=' + id;
	}
	// ����
	function sub(obj) {
		var id = obj.parent().parent().children().html();
		location = 'leaveView?opr=approval&id=' + id;
	}
	
	// �鿴
	$('.search').click(function(){
		search($(this));
	});

	// ����
	$('.sub').click(function(){
		sub($(this));
	});
	
	// ��ѯ
	$('#search').click(function(){
		restJson($(this),null);
	});
	
	// ��ҳ
	$('#home').click(function(){
		restJson($(this),1);
	});
	
	// ��һҳ
	$('#prev').click(function(){
		var pageNo = parseInt($('span#curPage').text()) - 1;
		restJson($(this),pageNo);
	});
	
	// ��һҳ
	$('#next').click(function(){
		var pageNo = parseInt($('span#curPage').text()) + 1;
		restJson($(this),pageNo);
	});
	
	// ĩҳ
	$('#last').click(function(){
		restJson($(this),$('#totalPage').text());
	});
	
	// �����첽
	function restJson(obj,pageNo) {
		// ��ò�ѯ����
		var type = $('#type').find("option:selected").text();
		var start = $('#startDate').val();
		var end = $('#endDate').val();
		$.post('restJson.action',{ "type":type,"start":start,"end":end,"pageNo":pageNo },function(result){
			// ��սڵ�
			$('.leave').remove();
			$('#page').remove();
			$('.list').append(result);
			// ���°��¼�
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
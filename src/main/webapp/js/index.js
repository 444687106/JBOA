$(function(){
	$('#save').click(function(){		// 添加明细
		var tr = $(this).parent();	// 获取整个tr标签
		// 获得下拉框当前选中项的文本
		var options = tr.children().children().children();
		var text = '';
		for(var i=0;i<options.length;i++) {//下拉框的长度就是他的选项数
			if(options[i].selected==true) {
				text=options[i].text;//获取文本
			}	
		}
		
		if(tr.children().next().children().val().length == 0){
			alert('请输入金额!');
		} else if(isNaN(tr.children().next().children().val())) {
			alert('请输入数字!!');
		} else {
			var des = tr.children().next().next().children().val();
			// 获得金额
			var price = parseFloat(tr.children().next().children().val());
			// 总金额
			var totalPrice = parseFloat($('#totalPrice1').text());
			totalPrice = parseFloat(totalPrice) + parseFloat(price);
			var str = '';
			str += '<tr>';
			str += '<td>' + text + '<input type="hidden" name="items" value="'+ text +'" />' + '</td>';
			str += '<td> ￥'+ price.toFixed(2) + '<input type="hidden" name="accounts" value="' + price.toFixed(2) +'" />' +'</td>';
			str += '<td>'+ des + '<input type="hidden" name="dess" value="' + des + '" />' +'</td>';
			str += '<td class="del" ><img src="images/delete.gif" width="16" height="16" /></td>';
			str += '</tr>';
			// 追加节点
			tr.parent().before(str);
			// 重置节点内容
			options[0].selected = true;
			tr.children().next().children().val('');
			tr.children().next().next().children().val('');
			// 计算总金额,并保留两位小数
			$('#totalPrice1').text(totalPrice.toFixed(2));
			$('[name="totalPrice"]').val(totalPrice.toFixed(2));
			// 绑定事件之前解绑事件,避免重复触发
			$('.del').off('click');
			$('.del').on('click',function(){	// 绑定事件
				del($(this),$('#totalPrice1'),$('[name="totalPrice"]'));
			});
		}
	});
	// 删除明细
	del = function(delObj,totalObj,valObj){
		var del = delObj.parent();
		var totalPrice = totalObj.text();
		var price = $(this).prev().prev().text();
		var flag = confirm('确定要删除此项吗?');
		if(flag){
			del.remove();
			price = delObj.prev().prev().text();
			totalPrice = parseFloat(totalPrice) - parseFloat(price.substring(2));
			// 计算总金额,并保留两位小数
			totalObj.text(totalPrice.toFixed(2));
			valObj.val(totalPrice.toFixed(2));
		}
	};
	
	// 移除明细
	$('.del').click(function(){
		del($(this),$('#totalPrice1'),$('[name="totalPrice"]'));
	});
	
	// 判断金额和明细条数是否大于1
	function checkPrice(){
		var totalPrice = $('[name="totalPrice"]').val();
		var price = 0;
		$('input[name="accounts"]').each(function(i,n){
			price += parseInt($(this).val());
		});
		if (price != totalPrice) {
			if (confirm('当前明细金额与总金额不匹配,保存后会自动计算总金额,是否继续?')) {
				if (price <= 0) {
					alert('请添加至少一条明细!!');
					return false;
				}
			} else {
				return false;
			}
		} else {
			if (price <= 0) {
				alert('请添加至少一条明细!!');
				return false;
			}
		}
		return true;
	}
	
	// 点击保存后判断明细与总金额是否匹配
	$('#form1').submit(checkPrice);
});



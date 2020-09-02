/**
 * 说明:
 *
 * @author zhanglin/016873
 * @version: V1.0.0
 * @update 2020/9/1
 */
function login(){
    var phoneNumber = $("#phoneNumber").val();
    var password = $("#password").val();
    var state= $("#rememberme")[0];
    var json={};
    json.phoneNumber=phoneNumber;
    json.password=password;
    if(state.checked){                     //checked用于判断rememberme对象（checkbox对象）有没有被选中
        json.rememberme=true;
    }else{
        json.rememberme=false;
    }

    var data=JSON.stringify(json);
    $.ajax({
        type:"POST",
        url:"/user/login",
        data:data,
        contentType:"application/json",
        success:function(data){
            var data1=data;
            if(data1.status==200){
                alert("登录成功！");
                window.location.href='index.html';
            }else{
                alert(data1.msg);
            }
        }
    });
}

function register(){
    var phoneNumber = $("#phoneNumber").val();
    var password = $("#password").val();
    var json={};
    json.phoneNumber=phoneNumber;
    json.password=password;
    var data=JSON.stringify(json);
        $.ajax({
            type:"POST",
            url:"/user/registerPhone",
            data:data,
            contentType:"application/json",
            success:function(data){
                var data1=data;
                if(data1.status==200){
                    alert("注册成功！");
                    self.location='index.html';
                }else{
                    data1.msg;
                }
                //显示数据
            }
        });
}

//检查手机格式的正确性，以及手机是否被注册
function checkNumber(){
    var phoneNumber = $("#phoneNumber").val();
    //对手机号的验证
    var pattern = /^1[34578]\d{9}$/;
    var isValid=pattern.test(phoneNumber);
    if(!isValid){
        alert('提示:请输入有效的手机号！');
        $("#phoneNumber").val("");
    }else{
        var phoneNumber = $("#phoneNumber").val();
        var json={};
        json.phoneNumber=phoneNumber;
        var data=JSON.stringify(json);
        $.ajax({
            type:"POST",
            url:"/user/check",
            data:data,
            contentType:"application/json",
            success:function(data){
                var data1=data;
                if(data1.data==true){
                    alert("手机已注册！");
                    $("#phoneNumber").val("");
                }
            }
        })
    }
}

//检查两次输入的密码是否一致
function checkPassword(){
    var password = document.getElementById("password").value;
    var confirm_password = document.getElementById("confirm_password").value;
    if(password!=confirm_password){
            alert('提示:两次输入的密码不一致，请重新输入！');
    }
}


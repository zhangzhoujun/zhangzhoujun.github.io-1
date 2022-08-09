

### WebView Js 回调函数
   
* App  Js对象

    GOSBridge

* Js方法：getUserData
   1. 功能：
      
      获取用户数据 
   2. 参数：
   
      ```
      {
        "callbackKey":"callBackKeyValue"
      }
      ```
   3. 返回：
   
      ```
      callBackKeyValue(String jsonStr,String otherStr)
      ```   
* Js方法：getAppData
   1. 功能：
      
      获取手机信息
   2. 参数：
   
      ```
      {
        "callbackKey":"callBackKeyValue"
      }
      ```
   3. 返回：
   
      ```
      callBackKeyValue(String jsonStr,String otherStr)
      
      jsonStr:
      {
	    "brand":"",
	    "model":"",
	    "sysVersion":"",
	    "deviceId":"",
	    "appVersion":"",

      }
      
      ```   
* Js方法：setPage
   1. 功能：
      
      设置当前页面信息
   2. 参数：
   
      ```
      {
        "callbackKey":"callBackKeyValue"
        "title":"",             // 页面标题
        "isPullRefresh":boolean, // 是否下拉刷新
        "bgColor":"",           // header背景颜色
        "color":"",             // header文字颜色
        "header": boolean,       // 是否显示header
        "isShowTr": boolean      // 是否开启右上角
      }
      ```
   3. 返回：
   
      ```
      callBackKeyValue(String jsonStr,String otherStr)
      
      jsonStr:
      {
	    "success":"true" 
      }
      
      ```       
* Js方法：openWebview
   1. 功能：
      
      新开一张H5页
   2. 参数：
   
      ```
      {
        "callbackKey":"callBackKeyValue",
        "url":"",             //  
      }
      ```
   3. 返回：
   
      ```
      callBackKeyValue(String jsonStr,String otherStr)
      
      jsonStr:
      {
	    "success":"true" 
      }
      
      ```   
* Js方法：gotoNative
   1. 功能：
      
      跳转到原生页
   2. 参数：
   
      ```
      {
        "callbackKey":"callBackKeyValue",
        "path":"",             //  
      }
      ```
   3. 返回：
   
      ```
      callBackKeyValue(String jsonStr,String otherStr)
      
      jsonStr:
      成功：
          {
            "success":"true" 
          }
          
      失败：
          {
            "code":"",
            "message:"" 
          }
      ```   
* Js方法：登录
   1. 功能：
      
      原生的退出登录 
   2. 参数：
   
      ```
      {
        "callbackKey":"callBackKeyValue",
        "callbackUrl":"",             
      }
      ```
   3. 返回：
   
      ```
      callBackKeyValue(String jsonStr,String otherStr)
      
      jsonStr:
      成功：
          {
            "success":"true" 
          }
      ```         
      
* Js方法：joinVip  

    1. 功能：
        
        节点社群vip入群支付
    
    2. 参数：
        
        ```
        {
            "path": "",         // 支付类型： aLiPay（支付宝）
            "id":"",            // 社群id
            "vipPrice":""       // 入群vip价格
        }
        ```      

      
* Js方法：payOrder

    1. 功能：
        
        第三方H5调起节点app的支付
    
    2. 参数：
        
        ```
        {
            "payType": "",      // 支付类型： aLiPay（支付宝）
            "orderId":"",       // 订单号
            "orderPrice":""     // 订单金额
        }
        ```
    
        
        
        
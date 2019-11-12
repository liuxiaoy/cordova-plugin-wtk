# cordova-plugin-wtk

使用wtk手机相关服务

## 安装

cordova plugin add cordova-plugin-wtk

## 使用
### 初始化UHF模块

    初始化UHF模块；

    initRFID: function (success, error)
    
### 扫描

    扫描 RFID；
 
    success回调参数
 
         epc EPC信息
 
         rssi 信号强度
 
    startInventoryTag: function (success, error)

### 停止扫描

    停止扫描；

    stopInventory: function (success, error)  

### 关闭UHF模块

    关闭UHF模块；

    freeRFID: function (success, error)


## 感谢

此项目参照的互联网上若干项目

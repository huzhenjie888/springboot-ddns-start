### 本项目技术架构
后端
+ springboot 
+ enjoy
+ sqllite
+ quartz
+ hutools

前端
+ html
+ jquery
+ css
+ enjoy

支持环境
java8、java11

### 本项目启动方式
1、电脑先安装jdk运行环境，如果不会安装可百度，或者扫描下方二维码私信于我。
![苦逼程序猿](./docs/images/qrcode_for_gh_29c75ebcd88d_344.jpg "苦逼程序猿")

2、jdk安装完毕后，设置系统环境变量，可百度。

3、下载本项目，本项目git地址如下
https://github.com/huzhenjie888/springboot-ddns-start.git

4、源码编译可进入项目根目录执行 
`mvn clean package` 

5、复制target目录下面的jar包，执行下面命令进行启动

`java -jar xxxx.jar`
这里需要注意的是，application.yml不进行编译，所以需要手动复制出来跟xxx.jar包放在同一个目录里面。
启动之前，请先将源码中的数据库文件  src/main/resources/dbs/ddns.db 放到此目录下面/www/web/dbs/ddns.db


6、后台启动

window：自行研究

linux： nohup java -jar xxx.jar  


7、如果你有新的ddns平台需要接入，可微信扫描上方二维码催促作者更新，作者收到后，1天内回复是否可以更新，如果可以，3天内完成上线。

8、开源不易，欢迎大家多多指教与批评。

9、如实在无法正常启动项目，可关注上方二维码私信作者，把个人微信号发送至上方二维码，作者可免费指导哦。

10、目前只支持腾讯云动态域名解析，其他平台还在接入中。

### 赞助
可扫描下方二维码赞助作者哦
![苦逼程序猿](./docs/images/61aa52f4e4b08291bde91e98.png "苦逼程序猿")














### JavaFx开发教程
1. 环境： windows7/10, jdk8, eclipse20180917
2. 安装eclipse fx插件，用于javafx项目开发，安装完成后重启eclipse，使插件生效。(安装前可通过File->New->Other中查看是否有JavaFx项目存在，已证明插件是否存在)
    Help->Install New Software
    Add->
        Name: e(fx)clipse
        Location: http://download.eclipse.org/efxclipse/updates-released/2.3.0/site
3. 安装可视化插件：javafx_scenebuilder-2_0-windows（用于在eclipse上完成对窗体的布局操作）
4. 使用eclipse创建JavaFx项目（File->New->Other->JavaFx Project）后续一路默认即可，其实类似普通Java项目
5. 项目创建完成后， 运行Main类，即可看到一个空窗体；
    右键点击项目，export-runnable jar
    使用javafxpackager -deploy -appclass application.Main[包.主类] -native image -srcdir 刚才生成包的完全路径记得双\转义[runnable jar路径] -outdir e:\\tardir\\[输出目录] -outfile et[文件名称]
    https://jingyan.baidu.com/article/ae97a646f9dbfdbbfc461d6d.html 安装鲁大师解决msvcp100.dll不存在错误，导致点击.exe文件打开窗体失败
6. 安装innosetup-5.6.1-unicode.exe插件将上面生成的exe文件打包成独立运行的exe安装包
    一路下一步后，保存.iss文件至项目根目录，后面inno会自动执行脚本，将exe文件生成出来，双击exe即可安装
    如果生成目录找不到还可以通过inno工具栏Build->open output folder打开生成文件的目录
7. 上面所叙述的是最简单的利用java 可执行包生成可安装独立运行的exe文件步骤

https://yuchen112358.github.io/2016/05/06/javafx-install/
https://code.makery.ch/zh-cn/library/javafx-tutorial/part7/
https://www.jianshu.com/p/3798a78303f8
https://docs.oracle.com/javafx/2/ui_controls/progress.htm

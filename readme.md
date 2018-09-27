

已知兼容版本:
Spring boot 1.4.x ------>cxf-spring-boot-starter-jaxws 3.1.x
Spring boot 1.5.x------->cxf-spring-boot-starter-jaxws 3.2.x
所以出现上面问题,且用了spring boot 1.5.x的小伙伴请将cxf-spring-boot-starter-jaxws 版本升级到3.2.1即可解决。

3.2修改spring boot cxf整合后默认的/services路径
在spring boto的application配置文件中配置
cxf.path=/yourpath



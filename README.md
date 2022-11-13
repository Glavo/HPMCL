#  🍒 HiPer Minecraft Launcher

HiPer 官方 QQ 群：

[①群:235256586](https://jq.qq.com/?_wv=1027&k=nWLzktPE) · [②群:212927890](https://jq.qq.com/?_wv=1027&k=5x33G0Bv) · [③群:93365639](https://jq.qq.com/?_wv=1027&k=76EsDqXD) · [④群:93364993](https://qm.qq.com/cgi-bin/qm/qr?k=N06ojqIIfSEoDId2PJSKzvbrNaGTIJmh) (推荐) · [⑤群:93364197](https://jq.qq.com/?_wv=1027&k=dudBV2zZ)

## 简介

HiPer Minecraft Launcher (HPMCL) 是 [Hello Minecraft! Launcher (HMCL)](https://github.com/huanghongxun/HMCL) **非官方分支**，
为用户提供基于 HiPer 的联机支持。HMCL 官方对本分支不负任何责任。

HiPer Minecraft Launcher 仅为[速聚](https://mcer.cn/)提供多人联机服务入口，使用中遇到的任何问题由速聚负责处理。

用户在使用多人联机服务过程中所遇到的任何问题与纠纷（**包括其付费业务**）均与 HiPer Minecraft Launcher 无关，请与速聚协商解决。

## 下载

TODO

## 开源协议

> 本项目遵守 [Hello Minecraft! Launcher](https://github.com/huanghongxun/HMCL) 协议，以 [GPLv3](https://www.gnu.org/licenses/gpl-3.0.html) 协议开源，并遵循附加条款修改程序名称以示与原始版本不同。 

该程序在 [GPLv3](https://www.gnu.org/licenses/gpl-3.0.html) 开源协议下发布, 同时附有附加条款.

### 附加条款 (依据 GPLv3 开源协议第七条)
1. 当您分发该程序的修改版本时, 您必须以一种合理的方式修改该程序的名称或版本号, 以示其与原始版本不同. (依据 [GPLv3, 7(c)](https://github.com/huanghongxun/HMCL/blob/11820e31a85d8989e41d97476712b07e7094b190/LICENSE#L372-L374))

   该程序的名称及版本号可在[此处](https://github.com/Glavo/HPMCL/blob/main/HMCL/src/main/java/org/jackhuang/hmcl/Metadata.java#L33-L35)修改.

2. 您不得移除该程序所显示的版权声明. (依据 [GPLv3, 7(b)](https://github.com/Glavo/HPMCL/blob/11820e31a85d8989e41d97476712b07e7094b190/LICENSE#L368-L370))

## 贡献
如果您想提交一个 Pull Request, 必须遵守如下要求:
* IDE: Intellij IDEA
* 编译器: Java 1.8
* **不要**修改 `gradle` 相关文件

### 编译
于项目根目录执行以下命令:

```bash
./gradlew clean build
```

请确保您至少安装了含有 JavaFX 8 的 Java. 建议使用 Liberica Full JDK 8 或更高版本.

## JVM 选项 (用于调试)
| 参数                                           | 简介                                                                                              |
|----------------------------------------------|-------------------------------------------------------------------------------------------------|
| `-Dhmcl.self_integrity_check.disable=true`   | 检查更新时绕过本体完整性检查.                                                                                 |
| `-Dhmcl.bmclapi.override=<version>`          | 覆盖 BMCLAPI 的 API Root, 默认值为 `https://bmclapi2.bangbang93.com`. 例如 `https://download.mcbbs.net`. |
| `-Dhmcl.font.override=<font family>`         | 覆盖字族.                                                                                           |
| `-Dhmcl.version.override=<version>`          | 覆盖版本号.                                                                                          |
| `-Dhmcl.update_source.override=<url>`        | 覆盖更新源.                                                                                          |
| `-Dhmcl.authlibinjector.location=<path>`     | 使用指定的 authlib-injector (而非下载一个).                                                                |
| `-Dhmcl.openjfx.repo=<maven repository url>` | 添加用于下载 OpenJFX 的自定义 Maven 仓库                                                                    |
| `-Dhmcl.native.encoding=<encoding>`          | 覆盖原生编码.                                                                                         |
| `-Dhmcl.microsoft.auth.id=<App ID>`          | Microsoft OAuth App ID (用于支持微软登录)                                                               |
| `-Dhmcl.microsoft.auth.secret=<App Secret>`  | Microsoft OAuth App 密钥 (用于支持微软登录))                                                              |

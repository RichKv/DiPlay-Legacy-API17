# DiPlay Legacy API17

本项目是 [DiPlay-Legacy-Android](https://github.com/programmerguohuajing/DiPlay-Legacy-Android) 的 Android 4.2.2 / API17 兼容派生版，基于 0.2.7、提交 `c8884adcc75bfda3c134db63877bd6c6f83beb74`。

[English](README.md) · [兼容改动](docs/API17_CHANGES.md) · [构建](docs/BUILD.md) · [安装](docs/INSTALL.md) · [验证](docs/VALIDATION.md) · [许可证](docs/THIRD_PARTY_NOTICES.md)

## 本版用途

仅将最低 Android 等级从 API19（Android 4.4）下放至 API17（Android 4.2.2），修改 API 调用隔离、旧运行时兼容、资源及构建配置；保留上游协议和应用选项，不新增车辆集成或应用服务接口。

本版验证范围为编译、安装及主界面启动。手机配对、无线网络和配件认证是另行准备及验证的条件。能够安装不等于已经完成手机投屏。构建不包含配件身份或凭据，也不自动下载它们。

## 下载和更新

[GitHub Releases](https://github.com/RichKv/DiPlay-Legacy-API17/releases) 提供维护者签名 APK、对应源码和 SHA256 校验文件。以具体版本附带的验证记录为准。本项目不部署下载网站。

正式应用标识 `io.github.richkv.diplay.api17`，开发版加 `.debug`。版本 `0.2.7-api17.1`，versionCode 27。后续覆盖更新必须延续包名和签名证书，并递增版本码；不同签名构建不能默认互相覆盖。

## 构建

使用 JDK 25、Gradle 9.5、Android SDK 37 和 build-tools 36.0.0。API17 变体无需 NDK，不打包 JNI 原生库。

```sh
./gradlew :mobile:assembleApi17Debug
```

## 来源与声明

选定上游最低等级是 API19，未限定必须是 Android 4.4.4。保留[来源记录](docs/UPSTREAM_PROVENANCE.md)、[上游原始说明](docs/UPSTREAM-README.zh-CN.md)及原署名。上游功能和验证记录属于上游，不作为本版 APK 的验证结果。

接收器代码保留 GPLv3；DiAuto 衍生界面保留 AGPL-3.0-only。比亚迪图片和 CarPlay 图标另有声明，见[第三方声明](docs/THIRD_PARTY_NOTICES.md)。本项目是独立派生版，不是官方 DiPlay 发布版或 Apple 认证产品。

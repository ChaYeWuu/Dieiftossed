# Die If Tossed

> 死亡掉落不？ —— 一个趣味 Fabric 模组。
![Fabric](https://img.shields.io/badge/Fabric-0.19.3-b8467a)
![License](https://img.shields.io/badge/License-MIT-blue)

**Die If Tossed**（dieiftossed）是一个面向 Minecraft 26.2（Fabric）的轻量玩法模组。它在两个关键时刻向玩家抛出一个灵魂拷问：

- 当你**主动扔出物品**时：你掉落物品了！你死不死？
- 当你**即将真正死亡**时：哈哈你真死了？但你真的想死吗？

作者：**ChaYeWuu**

---

## 功能特性

### 1. 主动掉落确认
- 在「创建世界 → 游戏规则」中新增自定义规则 **「掉落死亡不？」**（默认开启）。
- 规则开启时，玩家按 `Q` 或从物品栏主动丢弃物品，会弹出确认 GUI。
- 两个选项：
  - **「死！！！」**：玩家立即死亡，且**保留物品栏**（不掉物）。
  - **「补药啊！我不想死！」**：什么都不会发生，物品照常掉落。

### 2. 真正死亡拦截
- 当玩家血量归零、即将进入真正死亡流程时，模组拦截死亡并弹出 GUI。
- 三个选项：
  - **「死！！！」**：确认死亡且保留物品栏，进入正常死亡 / 重生流程。
  - **「补药啊！我不想死！」**：取消死亡，玩家复活（满血、原地、不掉物）。
  - **「听天由命」**：50% 概率走死亡分支、50% 走复活分支。

---

## 安装方法

### 前置条件
- **Fabric Loader** `0.19.3` 或更高版本。
- **Fabric API** `0.152.2+26.2` 或更高版本。
- Minecraft `26.2`。

### 安装步骤
1. 安装 Fabric Loader（参见 [Fabric 官网](https://fabricmc.net/)）。
2. 下载 Fabric API 对应版本的 jar 文件。
3. 下载本模组的 jar 文件（见 [Releases](https://github.com/ChaYeWuu/dieiftossed/releases)）。
4. 将 Fabric API 与本模组的 jar 一并放入 `.minecraft/mods` 文件夹。
5. 启动 Minecraft 26.2（Fabric），在「Mods」列表中可见 `Die If Tossed`。

---

## 使用方法

### 触发掉落确认
1. 进入世界后，确保游戏规则 `dieIfTossedDropPrompt` 处于开启状态（默认开启）。
   - 可在「创建世界 → 游戏规则」中切换；或使用指令 `/gamerule dieIfTossedDropPrompt true|false`。
2. 在物品栏中按 `Q` 或通过拖拽丢弃物品，即可弹出「你掉落物品了！你死不死？」GUI。
3. 根据意愿点击按钮：
   - 点「死！！！」立即死亡并保留物品栏。
   - 点「补药啊！我不想死！」取消本次掉落流程。

### 触发死亡拦截
1. 当血量归零、即将真正死亡时，自动弹出「哈哈你真死了？但你真的想死吗？」GUI。
2. 根据意愿点击按钮：
   - 「死！！！」确认死亡并保留物品栏，进入正常重生流程。
   - 「补药啊！我不想死！」取消死亡，原地满血复活。
   - 「听天由命」交由 50/50 概率决定走向。

---

## 编译方法

### 环境要求
- **JDK 25**。
- 仓库自带 `gradlew` 包装器，无需单独安装 Gradle。

### 构建命令

在仓库根目录执行：

```bash
# macOS / Linux
./gradlew build

# Windows
./gradlew.bat build
```

### 构建产物
- 构建成功后，jar 文件位于：
  ```
  build/libs/dieiftossed-1.0.0.jar
  ```

> 说明：首次构建时，Fabric Loom 会自动下载 Minecraft 及其依赖、映射文件等，耗时较长，请耐心等待或配置镜像加速。

---

## 开源协议

本项目基于 **MIT License** 开源，版权所有者 **ChaYeWuu**，年份 2026。

详见 [LICENSE](./LICENSE) 文件。

---

## 相关链接

- GitHub 仓库：<https://github.com/ChaYeWuu/dieiftossed>
- 哔哩哔哩主页：<https://space.bilibili.com/698351214>
- Fabric 官方文档：<https://docs.fabricmc.net/>

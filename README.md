# checkMember

## 更新日志

- 0.1.0
    - 搭建基础模板
    - 新增：`/gp <group>` 可获取指定群成员列表
- 0.1.2
    - 新增：`/invalidMember` 可获取资源群无效群成员列表
- 0.1.3
    - 新增：Ban掉资源群中无效言论
- 0.1.5
    - 新增：`/clearMember <group>` 根据对应规则，清除指定群聊僵尸成员
- 0.1.6
    - 新增：管理组关键字触发 可获得 群管命令列表
    - 新增：新成员欢迎
- 0.1.7
    - 新增：`/find <[QQ]|[NickName]|[Nick]|[SpecialTitle]>` 通过 QQ号、昵称、备注、头衔 获取群成员信息
    - 新增：新人进群 会同时发送头像
    - 支持：媒体消息的转发
    - 支持：常用聊天媒体形式的转发
    - 支持：监听所有群组的闪照
    - 优化：群组成员获取方式
    - 优化：日志记录方式
- 0.1.8
    - 优化 机器人单例模式
    - 修复 未禁言的多余展示
    - 移除 命令展示的@
    - 优化 成员信息展示
- 0.2.0
    - 新增 资源群文件转存
- 0.2.1
    - 新增 群员求文时 机器人会主动回查归档数据库
- 0.2.2
    - 新增 归档文件日报、周报、月报
    - 修复 Alist token缓存失败
    - 新增 求文次数记录并监控、警告
- 0.2.5 [2023-01-07]
    - 优化 群判断机制
    - 恢复 归档文件日报、周报、月报发送通知
    - 简化 sql处理
    - 修复 短连接不过期
    - 添加 其他的测试类
- 0.2.6 [2023-01-08]
    - 新增 求文日志记录
    - 新增 定时追踪关闭求文日志
    - 新增 定时反馈每日求文未成功记录
    - 新增 反馈x天求文未成功记录 指令
    - 新增 求文不成功不算次数
    - 新增 求文格式错误 5次/周 给予警告
    - 修改 求文匹配规则
- 0.2.7 [2023-01-08]
    - 新增 部分信息以excel形式上传群文件
    - 新增 求文情况日报
    - 优化 归档文件之后的检验策略
package com.fis.web.init.threadwork;

public enum AppAction
{
    /// <summary>
    /// 强制更新全部
    /// </summary>
    All,
    /// <summary>
    /// 增加一条
    /// </summary>
    Add,
    /// <summary>
    /// 删除一条
    /// </summary>
    Remove,
    /// <summary>
    /// 打印
    /// </summary>
    Print,
    /// <summary>
    /// 起步加载(无缓存自动加载，有则不加载)
    /// </summary>
    Load,
    /// <summary>
    /// 检查加载是否成功
    /// </summary>
    Check,
}
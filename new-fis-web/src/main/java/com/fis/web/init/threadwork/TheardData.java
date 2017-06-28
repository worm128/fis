package com.fis.web.init.threadwork;

public class TheardData {
	/// <summary>
    /// 表类型
    /// </summary>
    public AppTheardType TheardType ;
    /// <summary>
    /// 动作类型
    /// </summary>
    public AppAction Action;
    /// 操作值
    /// </summary>
    public String Value;
    
	public AppTheardType getTheardType() {
		return TheardType;
	}
	public void setTheardType(AppTheardType theardType) {
		TheardType = theardType;
	}
	public AppAction getAction() {
		return Action;
	}
	public void setAction(AppAction action) {
		Action = action;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
    
}
